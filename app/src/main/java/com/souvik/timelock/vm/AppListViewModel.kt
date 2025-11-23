package com.souvik.timelock.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.souvik.timelock.data.model.AppInfo
import com.souvik.timelock.data.repository.AppListRepository
import com.souvik.timelock.data.repository.AppLimitsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppListViewModel @Inject constructor(
    private val appListRepo: AppListRepository,      // loads installed apps
    private val limitsRepo: AppLimitsRepository      // saves selected apps
) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps

    fun loadApps() {
        viewModelScope.launch {
            appListRepo.loadInstalledApps().collect { installed ->
                _apps.value = installed
            }
        }
    }

    fun saveSelectedApp(pkg: String) {
        viewModelScope.launch {
            limitsRepo.addSelectedApp(pkg)
        }
    }
}
