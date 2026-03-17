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

data class NotificationItem(
    val title: String,
    val message: String,
    val time: String,
    val icon: ImageVector,
    val type: NotificationType,
    val isRead: Boolean = false
)

enum class NotificationType { TRIP, EARNING, PROMO, SYSTEM }

@Composable
fun NotificationsScreen(onBackClick: () -> Unit = {}) {
    val colors = LocalWayGoColors.current

    var notifications by remember {
        mutableStateOf(
            listOf(
                NotificationItem("Trip Completed", "₹350 earned from trip to Vastrapur Lake", "2 min ago", Icons.Default.CheckCircle, NotificationType.TRIP),
                NotificationItem("Trip Cancelled", "Rider cancelled the trip. Cancellation fee of ₹25 applied.", "15 min ago", Icons.Default.Cancel, NotificationType.TRIP, true),
                NotificationItem("Weekly Payout", "₹8,420 has been transferred to your bank account.", "1 hour ago", Icons.Default.AccountBalanceWallet, NotificationType.EARNING),
                NotificationItem("🎉 Bonus Zone Active!", "Earn 1.5x on all trips in SG Highway area until 8 PM!", "2 hours ago", Icons.Default.Stars, NotificationType.PROMO),
                NotificationItem("Document Verified", "Your driving license has been verified successfully.", "5 hours ago", Icons.Default.VerifiedUser, NotificationType.SYSTEM, true),
                NotificationItem("New Feature", "Dark mode is here! Go to Settings to try it out.", "Yesterday", Icons.Default.DarkMode, NotificationType.SYSTEM, true),
                NotificationItem("Peak Hours Bonus", "Extra ₹50 per trip during 8-10 AM and 6-9 PM", "Yesterday", Icons.Default.TrendingUp, NotificationType.PROMO),
                NotificationItem("Insurance Renewal", "Your vehicle insurance expires in 30 days. Renew now.", "2 days ago", Icons.Default.Security, NotificationType.SYSTEM),
                NotificationItem("High Rating!", "Congrats! You maintained 4.9★ rating this week.", "3 days ago", Icons.Default.Star, NotificationType.SYSTEM, true)
            )
        )
    }

    val unreadCount = notifications.count { !it.isRead }

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
            Column(Modifier.weight(1f)) {
                Text("Notifications", color = colors.textPrimary, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold)
                if (unreadCount > 0) {
                    Text("$unreadCount unread", color = YellowPrimary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Mark all read
            if (unreadCount > 0) {
                TextButton(
                    onClick = {
                        notifications = notifications.map { it.copy(isRead = true) }
                    }
                ) {
                    Text("Mark all read", color = YellowPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // Notification List
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            notifications.forEachIndexed { index, notification ->
                NotificationCard(
                    notification = notification,
                    onClick = {
                        notifications = notifications.toMutableList().apply {
                            this[index] = notification.copy(isRead = true)
                        }
                    }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
fun NotificationCard(notification: NotificationItem, onClick: () -> Unit) {
    val colors = LocalWayGoColors.current
    val typeColor = when (notification.type) {
        NotificationType.TRIP -> GreenAccent
        NotificationType.EARNING -> YellowPrimary
        NotificationType.PROMO -> OrangeAccent
        NotificationType.SYSTEM -> BlueAccent
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (notification.isRead) colors.surface else colors.surfaceElevated
        ),
        elevation = CardDefaults.cardElevation(if (notification.isRead) 0.dp else 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(typeColor.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(notification.icon, null, tint = typeColor, modifier = Modifier.size(20.dp))
            }

            Spacer(Modifier.width(12.dp))

            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        notification.title,
                        color = colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = if (notification.isRead) FontWeight.Medium else FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    if (!notification.isRead) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(YellowPrimary)
                        )
                    }
                }
                Spacer(Modifier.height(2.dp))
                Text(
                    notification.message,
                    color = colors.textSecondary,
                    fontSize = 12.sp,
                    maxLines = 2
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    notification.time,
                    color = colors.textSecondary.copy(alpha = 0.6f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
