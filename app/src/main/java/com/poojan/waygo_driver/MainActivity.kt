package com.poojan.waygo_driver

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.poojan.waygo_driver.ui.auth.LoginScreen
import com.poojan.waygo_driver.ui.auth.SignupScreen
import com.poojan.waygo_driver.ui.home.EarningsScreen
import com.poojan.waygo_driver.ui.home.HomeScreen
import com.poojan.waygo_driver.ui.home.WayGoBottomNav
import com.poojan.waygo_driver.ui.profile.*
import com.poojan.waygo_driver.ui.theme.ThemeMode
import com.poojan.waygo_driver.ui.theme.WayGoDriverTheme
import com.poojan.waygo_driver.ui.trip.TripsScreen

object Routes {
    const val LOGIN     = "login"
    const val SIGNUP    = "signup"
    const val HOME      = "home"
    const val EARNINGS  = "earnings"
    const val TRIPS     = "trips"
    const val PROFILE   = "profile"
    // New profile sub-pages
    const val SETTINGS       = "settings"
    const val SUPPORT        = "support"
    const val DOCUMENTS      = "documents"
    const val VEHICLE        = "vehicle"
    const val NOTIFICATIONS  = "notifications"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var themeMode by remember { mutableStateOf(ThemeMode.DARK) }

            WayGoDriverTheme(themeMode = themeMode) {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { WayGoBottomNav(navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.LOGIN,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // ── Auth ──
                        composable(Routes.LOGIN) {
                            LoginScreen(
                                onLoginClick = { _, _ ->
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(Routes.LOGIN) { inclusive = true }
                                    }
                                },
                                onSignUpClick = { navController.navigate(Routes.SIGNUP) }
                            )
                        }

                        composable(Routes.SIGNUP) {
                            SignupScreen(
                                onBackClick = { navController.popBackStack() },
                                onNextClick = {
                                    navController.navigate(Routes.HOME) {
                                        popUpTo(Routes.LOGIN) { inclusive = true }
                                    }
                                },
                                onLoginClick = { navController.popBackStack() }
                            )
                        }

                        // ── Main Tabs ──
                        composable(Routes.HOME) {
                            HomeScreen(
                                onProfileClick = { navController.navigate(Routes.PROFILE) },
                                onNotificationClick = { navController.navigate(Routes.NOTIFICATIONS) }
                            )
                        }

                        composable(Routes.EARNINGS) {
                            EarningsScreen()
                        }

                        composable(Routes.TRIPS) {
                            TripsScreen()
                        }

                        composable(Routes.PROFILE) {
                            DriverProfileScreen(
                                onBackClick = { navController.popBackStack() },
                                onEarningsClick = { navController.navigate(Routes.EARNINGS) },
                                onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                                onSupportClick = { navController.navigate(Routes.SUPPORT) },
                                onDocumentsClick = { navController.navigate(Routes.DOCUMENTS) },
                                onVehicleClick = { navController.navigate(Routes.VEHICLE) },
                                onNotificationsClick = { navController.navigate(Routes.NOTIFICATIONS) },
                                onLogoutClick = {
                                    navController.navigate(Routes.LOGIN) {
                                        popUpTo(0) { inclusive = true }
                                    }
                                }
                            )
                        }

                        // ── Profile Sub-Pages ──
                        composable(Routes.SETTINGS) {
                            SettingsScreen(
                                currentTheme = themeMode,
                                onThemeChange = { themeMode = it },
                                onBackClick = { navController.popBackStack() }
                            )
                        }

                        composable(Routes.SUPPORT) {
                            SupportScreen(onBackClick = { navController.popBackStack() })
                        }

                        composable(Routes.DOCUMENTS) {
                            DocumentsScreen(onBackClick = { navController.popBackStack() })
                        }

                        composable(Routes.VEHICLE) {
                            VehicleScreen(onBackClick = { navController.popBackStack() })
                        }

                        composable(Routes.NOTIFICATIONS) {
                            NotificationsScreen(onBackClick = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}
