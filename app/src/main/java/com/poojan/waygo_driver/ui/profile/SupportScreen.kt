package com.poojan.waygo_driver.ui.profile

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.ui.theme.*

@Composable
fun SupportScreen(onBackClick: () -> Unit = {}) {
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
            Text("Help & Support", color = colors.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Emergency
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = RedAccent.copy(alpha = 0.12f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(CircleShape).background(RedAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Warning, null, tint = RedAccent, modifier = Modifier.size(24.dp))
                    }
                    Spacer(Modifier.width(14.dp))
                    Column(Modifier.weight(1f)) {
                        Text("Emergency SOS", color = RedAccent, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Text("Tap for immediate help", color = colors.textSecondary, fontSize = 12.sp)
                    }
                    Icon(Icons.Default.ChevronRight, null, tint = RedAccent, modifier = Modifier.size(20.dp))
                }
            }

            // Contact Options
            Text("CONTACT US", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column {
                    ContactRow(Icons.Default.Call, "Call Support", "1800-419-WAYGO", "Available 24/7")
                    HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                    ContactRow(Icons.Default.Email, "Email Support", "driversupport@waygo.com", "Responds within 2 hours")
                    HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                    ContactRow(Icons.Default.Chat, "Live Chat", "Chat with us", "Average wait: 2 mins")
                }
            }

            // FAQ
            Text("FREQUENTLY ASKED QUESTIONS", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

            val faqs = listOf(
                "How do I update my vehicle details?" to "Go to Profile → Vehicle Details to update your vehicle information.",
                "When do I receive my earnings?" to "Earnings are settled every week. You can also withdraw anytime from Earnings → Withdraw.",
                "How does surge pricing work?" to "During high demand, ride fares increase. You earn more during surge hours.",
                "What if a passenger cancels?" to "You'll receive a cancellation fee if the passenger cancels after 5 minutes.",
                "How to contact passenger?" to "Tap the call icon on the ride card to reach the passenger.",
                "How to report an issue with a trip?" to "Go to Trips → Select the trip → Report Issue to file a complaint."
            )

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column {
                    faqs.forEachIndexed { index, (question, answer) ->
                        FAQItem(question, answer)
                        if (index < faqs.size - 1) {
                            HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                        }
                    }
                }
            }

            // Safety Center
            Text("SAFETY CENTER", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = colors.surface)
            ) {
                Column {
                    SafetyRow(Icons.Default.Shield, "Safety Guidelines", "Best practices for safe driving")
                    HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                    SafetyRow(Icons.Default.HealthAndSafety, "COVID-19 Safety", "Updated health guidelines")
                    HorizontalDivider(color = colors.divider, modifier = Modifier.padding(horizontal = 16.dp))
                    SafetyRow(Icons.Default.Report, "Report Safety Issue", "Report a safety concern")
                }
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun ContactRow(icon: ImageVector, title: String, detail: String, subtitle: String) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(YellowPrimary.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = YellowPrimary, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(detail, color = YellowPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(subtitle, color = colors.textSecondary, fontSize = 10.sp)
        }
        Icon(Icons.Default.ChevronRight, null, tint = colors.textSecondary, modifier = Modifier.size(20.dp))
    }
}

@Composable
fun FAQItem(question: String, answer: String) {
    val colors = LocalWayGoColors.current
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(question, color = colors.textPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f))
            Icon(
                if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                null, tint = colors.textSecondary, modifier = Modifier.size(20.dp)
            )
        }
        AnimatedVisibility(visible = expanded) {
            Text(
                answer,
                color = colors.textSecondary,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
fun SafetyRow(icon: ImageVector, title: String, subtitle: String) {
    val colors = LocalWayGoColors.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(GreenAccent.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, null, tint = GreenAccent, modifier = Modifier.size(20.dp))
        }
        Spacer(Modifier.width(14.dp))
        Column(Modifier.weight(1f)) {
            Text(title, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = colors.textSecondary, fontSize = 11.sp)
        }
        Icon(Icons.Default.ChevronRight, null, tint = colors.textSecondary, modifier = Modifier.size(20.dp))
    }
}
