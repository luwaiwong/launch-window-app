package com.nominal.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import com.nominal.ui.components.ColorPicker
import com.nominal.ui.theme.Foreground
import com.nominal.ui.theme.SubForeground
import com.nominal.ui.viewmodels.MainViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SettingsScreen(
    viewModel: MainViewModel
) {
    val settings by viewModel.settings.collectAsState()
    val themeSettings by viewModel.themeSettings.collectAsState()
    var showColorPicker by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium,
                color = Foreground,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        item {
            SectionHeader(text = "Notifications")
        }

        item {
            SettingSwitch(
                title = "Enable Notifications",
                checked = settings.enableNotifications,
                onCheckedChange = { viewModel.updateEnableNotifications(it) }
            )
        }

        item {
            Divider()
        }

        item {
            SectionHeader(text = "Launch Notifications")
        }

        item {
            SettingSwitch(
                title = "24 hours before",
                checked = settings.notifLaunch24h,
                onCheckedChange = { viewModel.updateNotifLaunch24h(it) },
                enabled = settings.enableNotifications
            )
        }

        item {
            SettingSwitch(
                title = "12 hours before",
                checked = settings.notifLaunch12h,
                onCheckedChange = { viewModel.updateNotifLaunch12h(it) },
                enabled = settings.enableNotifications
            )
        }

        item {
            SettingSwitch(
                title = "1 hour before",
                checked = settings.notifLaunch1h,
                onCheckedChange = { viewModel.updateNotifLaunch1h(it) },
                enabled = settings.enableNotifications
            )
        }

        item {
            SettingSwitch(
                title = "30 minutes before",
                checked = settings.notifLaunch30m,
                onCheckedChange = { viewModel.updateNotifLaunch30m(it) },
                enabled = settings.enableNotifications
            )
        }

        item {
            SettingSwitch(
                title = "10 minutes before",
                checked = settings.notifLaunch10m,
                onCheckedChange = { viewModel.updateNotifLaunch10m(it) },
                enabled = settings.enableNotifications
            )
        }

        item {
            Divider()
        }

        item {
            SectionHeader(text = "For You Feed")
        }

        item {
            SettingSwitch(
                title = "Show past launches",
                checked = settings.fyShowPastLaunches,
                onCheckedChange = { viewModel.updateFyShowPastLaunches(it) }
            )
        }

        item {
            SettingSwitch(
                title = "Show past events",
                checked = settings.fyShowPastEvents,
                onCheckedChange = { viewModel.updateFyShowPastEvents(it) }
            )
        }

        item {
            Divider()
        }

        item {
            SectionHeader(text = "Theme & Colors")
        }

        item {
            SettingSwitch(
                title = "Use Dynamic Colors (Android 12+)",
                checked = themeSettings.useDynamicColor,
                onCheckedChange = { viewModel.setUseDynamicColor(it) }
            )
        }

        if (!themeSettings.useDynamicColor) {
            item {
                Button(
                    onClick = { showColorPicker = !showColorPicker },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(if (showColorPicker) "Hide Color Picker" else "Customize Colors")
                }
            }

            if (showColorPicker) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Pick Accent Color",
                                style = MaterialTheme.typography.titleMedium,
                                color = Foreground
                            )

                            ColorPicker(
                                onColorSelected = { color ->
                                    viewModel.setCustomAccentColor(color.toArgb())
                                }
                            )

                            OutlinedButton(
                                onClick = { viewModel.resetThemeToDefaults() },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Reset to Default Colors")
                            }
                        }
                    }
                }
            }
        }

        item {
            Divider()
        }

        item {
            SectionHeader(text = "Developer")
        }

        item {
            SettingSwitch(
                title = "Developer Mode",
                checked = settings.devMode,
                onCheckedChange = { viewModel.updateDevMode(it) }
            )
        }

        if (settings.devMode) {
            item {
                val lastCall = viewModel.getLastCallTimestamp()
                if (lastCall != null) {
                    val dateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss", Locale.getDefault())
                    Text(
                        text = "Last API call: ${dateFormat.format(Date(lastCall))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = SubForeground,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            item {
                var notificationCount by remember { mutableStateOf<Int?>(null) }

                LaunchedEffect(Unit) {
                    notificationCount = viewModel.getScheduledNotificationCount()
                }

                notificationCount?.let { count ->
                    Text(
                        text = "Scheduled notifications: $count",
                        style = MaterialTheme.typography.bodySmall,
                        color = SubForeground,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            item {
                Button(
                    onClick = { viewModel.loadData(forceRefresh = true) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Reload Data")
                }
            }

            item {
                OutlinedButton(
                    onClick = { viewModel.clearCache() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Text("Clear Cache")
                }
            }
        }

        item {
            Divider()
        }

        item {
            Text(
                text = "Nominal v1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = SubForeground,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun SectionHeader(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = Foreground,
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
private fun SettingSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    enabled: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = if (enabled) Foreground else SubForeground,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            enabled = enabled
        )
    }
}

@Composable
private fun Divider() {
    HorizontalDivider(
        modifier = Modifier.padding(vertical = 8.dp),
        color = SubForeground.copy(alpha = 0.3f)
    )
}
