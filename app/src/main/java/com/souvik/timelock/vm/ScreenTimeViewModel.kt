package com.souvik.timelock.vm

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souvik.timelock.data.model.AppInfo
import com.souvik.timelock.data.model.AppLimit
import com.souvik.timelock.data.repository.AppLimitsRepository
import com.souvik.timelock.util.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScreenTimeViewModel @Inject constructor(
    private val repo: AppLimitsRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps.asStateFlow()

    private val _limits = MutableStateFlow<Map<String, AppLimit>>(emptyMap())
    val limits: StateFlow<Map<String, AppLimit>> = _limits.asStateFlow()

    private val _timeUpEvent = MutableSharedFlow<AppLimit>(extraBufferCapacity = 1)
    val timeUpEvent: SharedFlow<AppLimit> = _timeUpEvent.asSharedFlow()

    private val warned5Min = mutableSetOf<String>()
    private val timeUpHandled = mutableSetOf<String>()

    private var job: Job? = null

    init {

        // STEP 1 ❗ Only show SELECTED APPS
        viewModelScope.launch {
            combine(
                repo.selectedApps(),    // Set<String>
                repo.appsFlow()         // List<AppInfo>
            ) { selected, installed ->

                installed.filter { it.packageName in selected }

            }.collect { filtered ->
                _apps.value = filtered
            }
        }

        // STEP 2 — Start limit & usage monitoring
        startChecker()
    }

    fun startChecker() {
        job?.cancel()
        job = viewModelScope.launch {

            combine(
                repo.selectedApps(),   // Only apps user selected
                repo.appsFlow()
            ) { selectedPkgs, installedApps ->
                installedApps.filter { it.packageName in selectedPkgs }
            }.collect { selectedApps ->

                selectedApps.forEach { app ->

                    launch {
                        repo.limitFor(app.packageName).collect { appLimit ->

                            appLimit?.let { limit ->
                                _limits.update { it + (app.packageName to limit) }

                                val remaining = limit.limitMinutes - limit.usedMinutes

                                if (remaining <= 5 && remaining > 0 &&
                                    !warned5Min.contains(app.packageName)
                                ) {
                                    NotificationHelper.sendFiveMinutesLeft(context, app.appName)
                                    warned5Min.add(app.packageName)

                                } else if (remaining <= 0 &&
                                    !timeUpHandled.contains(app.packageName)
                                ) {
                                    NotificationHelper.sendTimeUpNotification(context, app.appName)

                                    _timeUpEvent.tryEmit(limit)
                                    timeUpHandled.add(app.packageName)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    fun totalUsedMinutes(): Long {
        return _limits.value.values.sumOf { it.usedMinutes }
    }

    fun remainingMinutes(pkg: String): Long {
        val item = _limits.value[pkg] ?: return 0
        return item.limitMinutes - item.usedMinutes
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}
