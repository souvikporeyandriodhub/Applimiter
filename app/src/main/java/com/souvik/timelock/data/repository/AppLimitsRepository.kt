package com.souvik.timelock.data.repository

import com.souvik.timelock.data.model.AppInfo
import com.souvik.timelock.data.model.AppLimit
import kotlinx.coroutines.flow.Flow

interface AppLimitsRepository {

    fun appsFlow(): Flow<List<AppInfo>>

    fun limitFor(pkg: String): Flow<AppLimit?>

    suspend fun setLimit(pkg: String, minutes: Long)

    suspend fun clearLimit(pkg: String)

    fun allLimits(): Flow<List<AppLimit>>

    suspend fun addUsage(pkg: String, minutes: Long)

    // NEW
    fun selectedApps(): Flow<Set<String>>
    suspend fun addSelectedApp(pkg: String)

    fun pinFlow(): Flow<String?>
    suspend fun savePin(pin: String)

    fun onboardedFlow(): Flow<Boolean>
    suspend fun markOnboarded()
}
