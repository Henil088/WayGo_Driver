package com.poojan.waygo_driver.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// === WayGo Brand Colors ===
val YellowPrimary   = Color(0xFFFFD600)
val YellowDark      = Color(0xFFF9A800)
val YellowAlpha     = Color(0x33FFD600)

// === Dark Palette ===
val BgDark          = Color(0xFF0D0D0D)
val SurfaceDark     = Color(0xFF1A1A1A)
val SurfaceElevated = Color(0xFF242424)
val TextPrimary     = Color(0xFFFFFFFF)
val TextSecondary   = Color(0xFF888888)
val Divider         = Color(0xFF2A2A2A)

// === Light Palette ===
val BgLight           = Color(0xFFF5F5F5)
val SurfaceLight      = Color(0xFFFFFFFF)
val SurfaceElevatedLt = Color(0xFFF0F0F0)
val TextPrimaryLt     = Color(0xFF1A1A1A)
val TextSecondaryLt   = Color(0xFF666666)
val DividerLt         = Color(0xFFE0E0E0)

// === Status ===
val GreenAccent     = Color(0xFF00C853)
val RedAccent       = Color(0xFFFF3B30)
val BlueAccent      = Color(0xFF007AFF)
val OrangeAccent    = Color(0xFFFF9500)

// === Theme-Aware Color Holder ===
data class WayGoColors(
    val background: Color,
    val surface: Color,
    val surfaceElevated: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val divider: Color,
    val primary: Color = YellowPrimary,
    val primaryDark: Color = YellowDark,
    val green: Color = GreenAccent,
    val red: Color = RedAccent,
    val blue: Color = BlueAccent,
    val orange: Color = OrangeAccent,
    val isLight: Boolean = false
)

val DarkWayGoColors = WayGoColors(
    background = BgDark,
    surface = SurfaceDark,
    surfaceElevated = SurfaceElevated,
    textPrimary = TextPrimary,
    textSecondary = TextSecondary,
    divider = Divider,
    isLight = false
)

val LightWayGoColors = WayGoColors(
    background = BgLight,
    surface = SurfaceLight,
    surfaceElevated = SurfaceElevatedLt,
    textPrimary = TextPrimaryLt,
    textSecondary = TextSecondaryLt,
    divider = DividerLt,
    isLight = true
)

val LocalWayGoColors = staticCompositionLocalOf { DarkWayGoColors }