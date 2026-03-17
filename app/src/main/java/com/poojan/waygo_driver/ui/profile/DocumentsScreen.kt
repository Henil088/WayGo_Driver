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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.poojan.waygo_driver.ui.theme.*

data class DocumentItem(
    val name: String,
    val icon: ImageVector,
    val status: DocumentStatus,
    val detail: String
)

enum class DocumentStatus { VERIFIED, PENDING, NOT_UPLOADED }

@Composable
fun DocumentsScreen(onBackClick: () -> Unit = {}) {
    val colors = LocalWayGoColors.current

    val documents = remember {
        listOf(
            DocumentItem("Aadhaar Card", Icons.Default.Badge, DocumentStatus.VERIFIED, "XXXX XXXX 4521"),
            DocumentItem("Driving License", Icons.Default.CreditCard, DocumentStatus.VERIFIED, "GJ01 20190054321"),
            DocumentItem("Vehicle RC", Icons.Default.DirectionsCar, DocumentStatus.PENDING, "Submitted on 10 Mar 2026"),
            DocumentItem("Vehicle Insurance", Icons.Default.Security, DocumentStatus.PENDING, "Under review"),
            DocumentItem("PAN Card", Icons.Default.AccountBalance, DocumentStatus.NOT_UPLOADED, "Required for tax purposes"),
            DocumentItem("Profile Photo", Icons.Default.Person, DocumentStatus.VERIFIED, "Last updated 5 Mar 2026"),
            DocumentItem("Police Verification", Icons.Default.LocalPolice, DocumentStatus.NOT_UPLOADED, "Required for onboarding")
        )
    }

    val verified = documents.count { it.status == DocumentStatus.VERIFIED }
    val total = documents.size

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
            Text("Documents", color = colors.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Progress Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = YellowPrimary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("📋", fontSize = 24.sp)
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text("Verification Progress", color = BgDark, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            Text("$verified of $total documents verified", color = BgDark.copy(alpha = 0.7f), fontSize = 12.sp)
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    LinearProgressIndicator(
                        progress = { verified.toFloat() / total },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)),
                        color = BgDark,
                        trackColor = BgDark.copy(alpha = 0.2f)
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        "${(verified * 100 / total)}% Complete",
                        color = BgDark,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Documents List
            Text("ALL DOCUMENTS", color = YellowPrimary, fontSize = 10.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)

            documents.forEach { doc ->
                DocumentCard(doc)
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun DocumentCard(doc: DocumentItem) {
    val colors = LocalWayGoColors.current
    val statusColor = when (doc.status) {
        DocumentStatus.VERIFIED -> GreenAccent
        DocumentStatus.PENDING -> OrangeAccent
        DocumentStatus.NOT_UPLOADED -> colors.textSecondary
    }
    val statusText = when (doc.status) {
        DocumentStatus.VERIFIED -> "Verified ✓"
        DocumentStatus.PENDING -> "Pending ⏳"
        DocumentStatus.NOT_UPLOADED -> "Not Uploaded"
    }
    val statusBg = when (doc.status) {
        DocumentStatus.VERIFIED -> GreenAccent.copy(alpha = 0.12f)
        DocumentStatus.PENDING -> OrangeAccent.copy(alpha = 0.12f)
        DocumentStatus.NOT_UPLOADED -> colors.surfaceElevated
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = colors.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(statusBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(doc.icon, null, tint = statusColor, modifier = Modifier.size(22.dp))
            }
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(doc.name, color = colors.textPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Text(doc.detail, color = colors.textSecondary, fontSize = 11.sp)
            }
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(statusBg)
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(statusText, color = statusColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
