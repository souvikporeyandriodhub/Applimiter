package com.souvik.timelock.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.souvik.timelock.vm.SetLimitViewModel

@Composable
fun SetLimitScreen(
    packageName: String,
    onLimitSet: () -> Unit,
    vm: SetLimitViewModel = hiltViewModel()
) {

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {

        Text(
            "Set Daily Limit for:",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            packageName,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = vm.minutes.collectAsState().value,
            onValueChange = vm::onMinutesChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Limit (minutes)") }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
                vm.saveLimit(packageName) {
                    onLimitSet()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Limit")
        }
    }
}
