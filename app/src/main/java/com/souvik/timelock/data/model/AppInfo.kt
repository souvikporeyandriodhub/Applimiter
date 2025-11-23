package com.souvik.timelock.data.model

import android.graphics.drawable.Drawable

data class AppInfo(
    val packageName: String,
    val appName: String,
    val icon: Drawable? = null   // optional to avoid fake icons error
)
