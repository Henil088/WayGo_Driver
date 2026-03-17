package com.poojan.waygo_driver.ui.profile

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.ui.theme.*

@Composable
fun VehicleScreen(onBackClick: () -> Unit = {}) {
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
                modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(colors.surface)
            ) {
                Icon(Icons.Default.ArrowBack, null, tint = colors.textPrimary)
            }
            Spacer(Modifier.width(12.dp))
            Text("Vehicle Details", color = colors.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Vehicle Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Vehicle Icon
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.verticalGradient(
                                    listOf(YellowPrimary.copy(alpha = 0.2f), YellowPrimary.copy(alpha = 0.05f))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("🚗", fontSize = 40.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Text("Maruti Swift Dzire", color = colors.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                    Text("White • 2022 Model", color = colors.textSecondary, fontSize = 13.sp)
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(YellowPrimary)
                            .padding(horizontal = 14.dp, vertical = 5.dp)
                    ) {
                        Text("GJ 01 MF 7890", color = BgDark, fontSize = 14.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                    }
                }
            }

            // Vehicle Info
            Text("VEHICLE INFORMATION", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column {
                    VehicleInfoRow("Make", "Maruti Suzuki", true)
                    VehicleInfoRow("Model", "Swift Dzire", true)
                    VehicleInfoRow("Year", "2022", true)
                    VehicleInfoRow("Color", "White", true)
                    VehicleInfoRow("Fuel Type", "Petrol + CNG", true)
                    VehicleInfoRow("Transmission", "Manual", true)
                    VehicleInfoRow("Seating", "4 + Driver", false)
                }
            }

            // Registration & Permits
            Text("REGISTRATION & PERMITS", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column {
                    VehicleInfoRow("Registration No.", "GJ 01 MF 7890", true)
                    VehicleInfoRow("Registration Date", "15 Jan 2022", true)
                    VehicleInfoRow("Registration Valid", "14 Jan 2037", true)
                    VehicleInfoRow("Commercial Permit", "Valid ✓", false)
                }
            }

            // Fitness & Insurance
            Text("FITNESS & INSURANCE", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column {
                    VehicleStatusRow("Fitness Certificate", "Valid until Dec 2026", true)
                    HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                    VehicleStatusRow("Insurance", "ICICI Lombard • Valid until Mar 2027", true)
                    HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                    VehicleStatusRow("PUC Certificate", "Valid until Sep 2026", true)
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun VehicleInfoRow(label: String, value: String, showDivider: Boolean) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = colors.textSecondary, fontSize = 13.sp)
        Text(value, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
    }
    if (showDivider) HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
}

@Composable
fun VehicleStatusRow(title: String, subtitle: String, isValid: Boolean) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (isValid) GreenAccent.copy(alpha = 0.12f) else RedAccent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                if (isValid) Icons.Default.CheckCircle else Icons.Default.Error,
                null,
                tint = if (isValid) GreenAccent else RedAccent,
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = colors.textSecondary, fontSize = 11.sp)
        }
    }
}
