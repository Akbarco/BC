package com.example.bc.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = OperatorBtnBg,
    onPrimary = OperatorBtnText,
    secondary = FunctionBtnBg,
    onSecondary = FunctionBtnText,
    surface = DarkSurface,
    onSurface = ResultColor,
    background = DarkBackground,
    onBackground = ResultColor
)

@Composable
fun BCTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}