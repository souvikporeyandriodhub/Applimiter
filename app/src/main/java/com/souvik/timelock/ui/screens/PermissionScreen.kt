package com.souvik.timelock.ui.screens

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PermissionScreen(
    onPermissionsGranted: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Permission Needed",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(20.dp))

        Text(
            text = "We need usage access permission to track app usage.",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(30.dp))

        Button(
            onClick = {
                val intent = Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                onPermissionsGranted()
                // You will open the permission screen in MainActivity later
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Grant Permission")
        }
    }
}
