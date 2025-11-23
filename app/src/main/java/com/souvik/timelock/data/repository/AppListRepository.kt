package com.souvik.timelock.data.repository

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.souvik.timelock.data.model.AppInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppListRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun loadInstalledApps(): Flow<List<AppInfo>> = flow {
        val pm = context.packageManager
        val apps = pm.getInstalledApplications(PackageManager.GET_META_DATA)

        val filtered = apps
            .filter { it.flags and ApplicationInfo.FLAG_SYSTEM == 0 } // remove system apps
            .map { appInfo ->
                val name = pm.getApplicationLabel(appInfo).toString()
                val icon = pm.getApplicationIcon(appInfo)
                AppInfo(
                    packageName = appInfo.packageName,
                    appName = name,
                    icon = icon
                )
            }
            .sortedBy { it.appName.lowercase() }

        emit(filtered)
    }
}
