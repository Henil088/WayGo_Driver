package com.poojan.waygo_driver.ui.profile

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.ui.theme.*

@Composable
fun SettingsScreen(
    currentTheme: ThemeMode = ThemeMode.DARK,
    onThemeChange: (ThemeMode) -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    val colors = LocalWayGoColors.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(colors.surface)
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = colors.textPrimary)
            }
            Spacer(Modifier.width(12.dp))
            Text("Settings", color = colors.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // ── Appearance ──
            SettingsSection("APPEARANCE") {
                Text("Theme", color = colors.textPrimary, fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    ThemeOption(
                        label = "Light",
                        icon = "☀️",
                        isSelected = currentTheme == ThemeMode.LIGHT,
                        onClick = { onThemeChange(ThemeMode.LIGHT) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOption(
                        label = "Dark",
                        icon = "🌙",
                        isSelected = currentTheme == ThemeMode.DARK,
                        onClick = { onThemeChange(ThemeMode.DARK) },
                        modifier = Modifier.weight(1f)
                    )
                    ThemeOption(
                        label = "System",
                        icon = "📱",
                        isSelected = currentTheme == ThemeMode.SYSTEM,
                        onClick = { onThemeChange(ThemeMode.SYSTEM) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // ── Notifications ──
            SettingsSection("NOTIFICATIONS") {
                var pushEnabled by remember { mutableStateOf(true) }
                var soundEnabled by remember { mutableStateOf(true) }
                var vibrationEnabled by remember { mutableStateOf(true) }

                SettingsToggle("Push Notifications", "Receive ride and earning alerts", pushEnabled) { pushEnabled = it }
                HorizontalDivider(color = colors.divider)
                SettingsToggle("Sound", "Play notification sounds", soundEnabled) { soundEnabled = it }
                HorizontalDivider(color = colors.divider)
                SettingsToggle("Vibration", "Vibrate on new notifications", vibrationEnabled) { vibrationEnabled = it }
            }

            // ── Location ──
            SettingsSection("LOCATION") {
                var highAccuracy by remember { mutableStateOf(true) }
                var bgLocation by remember { mutableStateOf(false) }

                SettingsToggle("High Accuracy GPS", "Use GPS + Network for precise location", highAccuracy) { highAccuracy = it }
                HorizontalDivider(color = colors.divider)
                SettingsToggle("Background Location", "Allow location tracking when app is minimized", bgLocation) { bgLocation = it }
            }

            // ── Language ──
            SettingsSection("LANGUAGE") {
                var selectedLang by remember { mutableStateOf("English") }
                val languages = listOf("English", "Hindi", "Gujarati")

                languages.forEach { lang ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .clickable { selectedLang = lang }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = selectedLang == lang,
                            onClick = { selectedLang = lang },
                            colors = RadioButtonDefaults.colors(selectedColor = YellowPrimary)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(lang, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            // ── About ──
            SettingsSection("ABOUT") {
                InfoRow("App Version", "1.0.0")
                HorizontalDivider(color = colors.divider)
                InfoRow("Build", "2026.03.17")
                HorizontalDivider(color = colors.divider)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Terms & Conditions", color = colors.textPrimary, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, null, tint = colors.textSecondary, modifier = Modifier.size(20.dp))
                }
                HorizontalDivider(color = colors.divider)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Privacy Policy", color = colors.textPrimary, fontSize = 14.sp, modifier = Modifier.weight(1f))
                    Icon(Icons.Default.ChevronRight, null, tint = colors.textSecondary, modifier = Modifier.size(20.dp))
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun ThemeOption(label: String, icon: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalWayGoColors.current
    Card(
        modifier = modifier
            .clickable { onClick() }
            .then(
                if (isSelected) Modifier.border(2.dp, YellowPrimary, RoundedCornerShape(14.dp))
                else Modifier
            ),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) YellowPrimary.copy(alpha = 0.12f) else colors.surfaceElevated
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(icon, fontSize = 24.sp)
            Spacer(Modifier.height(6.dp))
            Text(
                label,
                color = if (isSelected) YellowPrimary else colors.textSecondary,
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
            )
        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    val colors = LocalWayGoColors.current
    Column {
        Text(
            title,
            color = YellowPrimary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsToggle(title: String, subtitle: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(subtitle, color = colors.textSecondary, fontSize = 11.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = YellowPrimary,
                checkedTrackColor = YellowPrimary.copy(alpha = 0.3f),
                uncheckedThumbColor = colors.textSecondary,
                uncheckedTrackColor = colors.divider
            )
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = colors.textSecondary, fontSize = 14.sp)
        Text(value, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
    }
}
