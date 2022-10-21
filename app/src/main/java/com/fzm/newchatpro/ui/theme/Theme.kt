package com.fzm.newchatpro.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val ChatLightColors = lightColors(
    primary = Slate800,
    onPrimary = Color.White,
    primaryVariant = Slate600,
    secondary = Orange700,
    secondaryVariant = Orange500,
    onSecondary = Color.Black
)

val ChatDarkColors = darkColors(
    primary = Slate200,
    onPrimary = Color.Black,
    secondary = Orange500,
    onSecondary = Color.Black
).withBrandedSurface()

fun Colors.withBrandedSurface() = copy(
    surface = primary.copy(alpha = 0.08f).compositeOver(this.surface)
)

@Composable
fun NewChatProTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        ChatDarkColors
    } else {
        ChatLightColors
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}