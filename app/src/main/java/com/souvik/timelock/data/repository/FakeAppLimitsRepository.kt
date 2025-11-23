package com.souvik.timelock.data.repository

import com.souvik.timelock.data.model.AppInfo
import com.souvik.timelock.data.model.AppLimit
import com.souvik.timelock.data.prefs.DataStoreManager
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAppLimitsRepository @Inject constructor(
    private val ds: DataStoreManager
) : AppLimitsRepository {

    // Fake installed apps
    private val installed = listOf(
        AppInfo("com.instagram.android", "Instagram"),
        AppInfo("com.google.android.youtube", "YouTube"),
        AppInfo("com.facebook.katana", "Facebook"),
        AppInfo("com.whatsapp", "WhatsApp"),
        AppInfo("com.google.android.apps.photos", "Photos")
    )

    // Usage store (runtime only)
    private val usedMinutes = MutableStateFlow<Map<String, Long>>(emptyMap())

    override fun appsFlow(): Flow<List<AppInfo>> =
        flow { emit(installed) }

    override fun selectedApps(): Flow<Set<String>> = ds.selectedAppsFlow

    override suspend fun addSelectedApp(pkg: String) =
        ds.addSelectedApp(pkg)

    override fun limitFor(pkg: String): Flow<AppLimit?> =
        combine(
            ds.limitFlow(pkg),
            usedMinutes
        ) { limit, used ->
            limit?.let {
                AppLimit(pkg, it, used[pkg] ?: 0)
            }
        }

    override suspend fun setLimit(pkg: String, minutes: Long) {
        ds.setLimit(pkg, minutes)
    }

    override suspend fun clearLimit(pkg: String) {
        ds.clearLimit(pkg)
    }

    override fun allLimits(): Flow<List<AppLimit>> =
        flow { emit(emptyList()) }

    override suspend fun addUsage(pkg: String, minutes: Long) {
        usedMinutes.value =
            usedMinutes.value + (pkg to ((usedMinutes.value[pkg] ?: 0) + minutes))
    }

    override fun pinFlow() = ds.pinFlow
    override suspend fun savePin(pin: String) = ds.savePin(pin)

    override fun onboardedFlow() = ds.onboardedFlow
    override suspend fun markOnboarded() = ds.setOnboarded()
}
