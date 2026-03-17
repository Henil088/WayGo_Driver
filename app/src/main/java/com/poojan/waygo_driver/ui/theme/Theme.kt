package com.poojan.waygo_driver.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

enum class ThemeMode { LIGHT, DARK, SYSTEM }

private val WayGoDarkColors = darkColorScheme(
    primary          = YellowPrimary,
    onPrimary        = BgDark,
    secondary        = YellowDark,
    onSecondary      = BgDark,
    background       = BgDark,
    onBackground     = TextPrimary,
    surface          = SurfaceDark,
    onSurface        = TextPrimary,
    surfaceVariant   = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    error            = RedAccent
)

private val WayGoLightColors = lightColorScheme(
    primary          = YellowPrimary,
    onPrimary        = BgDark,
    secondary        = YellowDark,
    onSecondary      = BgDark,
    background       = BgLight,
    onBackground     = TextPrimaryLt,
    surface          = SurfaceLight,
    onSurface        = TextPrimaryLt,
    surfaceVariant   = SurfaceElevatedLt,
    onSurfaceVariant = TextSecondaryLt,
    error            = RedAccent
)

@Composable
fun WayGoDriverTheme(
    themeMode: ThemeMode = ThemeMode.DARK,
    content: @Composable () -> Unit
) {
    val useDark = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = if (useDark) WayGoDarkColors else WayGoLightColors
    val wayGoColors = if (useDark) DarkWayGoColors else LightWayGoColors

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = wayGoColors.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !useDark
        }
    }

    CompositionLocalProvider(LocalWayGoColors provides wayGoColors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}