package com.souvik.timelock.data.model

data class AppLimit(
    val packageName: String,
    val limitMinutes: Long,
    val usedMinutes: Long
)
