package com.souvik.timelock.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.res.painterResource
import com.souvik.timelock.R
import com.souvik.timelock.util.toBitmap
import com.souvik.timelock.vm.AppListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseAppScreen(
    onAppSelected: (String) -> Unit,
    viewModel: AppListViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadApps()
    }

    val apps by viewModel.apps.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Select an App") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            items(apps) { app ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            // SAVE SELECTED APP HERE 🔥
                            viewModel.saveSelectedApp(app.packageName)

                            // THEN MOVE TO SET LIMIT SCREEN
                            onAppSelected(app.packageName)
                        }
                        .padding(16.dp)
                ) {

                    val iconBitmap = app.icon?.toBitmap()

                    if (iconBitmap != null) {
                        Image(
                            bitmap = iconBitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.ic_placeholder),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = app.appName,
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = app.packageName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Divider()
            }
        }
    }
}
