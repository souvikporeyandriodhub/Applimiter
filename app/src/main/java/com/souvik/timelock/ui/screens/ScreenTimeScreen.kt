package com.souvik.timelock.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.souvik.timelock.data.model.AppInfo
import com.souvik.timelock.vm.ScreenTimeViewModel
import kotlinx.coroutines.flow.collectLatest
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenTimeScreen(
    navController: NavController,
    onOpenSetLimit: (String) -> Unit,
    viewModel: ScreenTimeViewModel = hiltViewModel()
) {
    val apps by viewModel.apps.collectAsState()
    val limits by viewModel.limits.collectAsState()

    // Listen for TIME-UP events
    LaunchedEffect(Unit) {
        viewModel.timeUpEvent.collectLatest { appLimit ->
            val pkg = appLimit.packageName
            val appName = apps.find { it.packageName == pkg }?.appName ?: pkg

            navController.navigate(
                "time_up/$pkg/${java.net.URLEncoder.encode(appName, "UTF-8")}"
            )
        }
    }


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Screen Time") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // ------- TOTAL SCREEN TIME CARD --------
            TotalScreenCard(limits = limits)

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Apps",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(Modifier.height(8.dp))

            // ------- APP LIST --------
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(apps) { app ->
                    AppRow(
                        app = app,
                        used = limits[app.packageName]?.usedMinutes ?: 0L,
                        limit = limits[app.packageName]?.limitMinutes,
                        onSetLimit = { onOpenSetLimit(app.packageName) }
                    )
                    Divider()
                }
            }
        }
    }
}

@Composable
private fun TotalScreenCard(
    limits: Map<String, com.souvik.timelock.data.model.AppLimit>
) {
    val totalUsed = limits.values.sumOf { it.usedMinutes }
    val totalLimit = limits.values.sumOf { it.limitMinutes }.coerceAtLeast(1L)
    val progress = (totalUsed.toFloat() / totalLimit.toFloat()).coerceIn(0f, 1f)

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Total screen time", style = MaterialTheme.typography.titleMedium)

            Spacer(Modifier.height(16.dp))

            Box(contentAlignment = Alignment.Center) {
                CircularScreenProgress(progress = progress)

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = formatMinutes(totalUsed),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text("Total today")
                }
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun CircularScreenProgress(progress: Float) {
    Canvas(modifier = Modifier.size(140.dp)) {
        val stroke = Stroke(width = 18f, cap = StrokeCap.Round)

        // Background arc
        drawArc(
            color = Color(0xFFE0E0E0),
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            style = stroke
        )

        // Progress arc
        drawArc(
            color = Color(0xFF4285F4),
            startAngle = -90f,
            sweepAngle = 360f * progress,
            useCenter = false,
            style = stroke
        )
    }
}

@Composable
fun AppRow(
    app: AppInfo,
    used: Long,
    limit: Long?,
    onSetLimit: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(app.appName)
        },
        supportingContent = {
            if (limit != null) {
                Text("${formatMinutes(used)} / ${formatMinutes(limit)}")
            } else {
                Text(formatMinutes(used))
            }
        },
        trailingContent = {
            TextButton(onClick = onSetLimit) {
                Text("Set limit")
            }
        }
    )
}

private fun formatMinutes(mins: Long): String {
    val h = mins / 60
    val m = mins % 60
    return if (h > 0) "${h}h ${m}m" else "${m}m"
}
