package com.souvik.timelock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.souvik.timelock.ui.navigation.AppNavGraph
import com.souvik.timelock.ui.theme.ApplimiterTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            ApplimiterTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavGraph()     // ✅ This loads your entire app UI
                }
            }
        }
    }
}
