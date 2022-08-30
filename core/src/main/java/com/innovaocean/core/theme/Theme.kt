package com.innovaocean.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun StockMarketAppTheme(
    isSystemInDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = getColors(isSystemInDarkTheme),
        typography = Typography,
        content = content
    )
}

internal fun getColors(isSystemInDarkTheme: Boolean): Colors {
    return if (isSystemInDarkTheme) DarkColorPalette else LightColorPalette
}