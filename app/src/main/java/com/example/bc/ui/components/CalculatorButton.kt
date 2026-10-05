package com.example.bc.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bc.ui.theme.DarkSurface
import com.example.bc.ui.theme.EqualsBtnBg
import com.example.bc.ui.theme.EqualsBtnText
import com.example.bc.ui.theme.FunctionBtnBg
import com.example.bc.ui.theme.FunctionBtnText
import com.example.bc.ui.theme.NumberBtnBg
import com.example.bc.ui.theme.NumberBtnText
import com.example.bc.ui.theme.OperatorBtnBg
import com.example.bc.ui.theme.OperatorBtnText

enum class ButtonType {
    NUMBER,
    FUNCTION,
    OPERATOR,
    EQUALS
}

@Composable
fun CalculatorButton(
    symbol: String,
    modifier: Modifier = Modifier,
    type: ButtonType = ButtonType.NUMBER,
    isWide: Boolean = false,
    fontSize: androidx.compose.ui.unit.TextUnit? = null,
    padding: androidx.compose.ui.unit.Dp = 6.dp,
    onClick: () -> Unit
) {
    val haptic = LocalHapticFeedback.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Smooth press scale effect for delightful UX
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = tween(durationMillis = 100),
        label = "ButtonScale"
    )

    val defaultStyle = when (type) {
        ButtonType.NUMBER -> ButtonStyle(
            bgColor = NumberBtnBg,
            textColor = NumberBtnText,
            fontSize = 30.sp,
            fontWeight = FontWeight.Medium
        )
        ButtonType.FUNCTION -> ButtonStyle(
            bgColor = FunctionBtnBg,
            textColor = FunctionBtnText,
            fontSize = 24.sp,
            fontWeight = FontWeight.SemiBold
        )
        ButtonType.OPERATOR -> ButtonStyle(
            bgColor = OperatorBtnBg,
            textColor = OperatorBtnText,
            fontSize = 32.sp,
            fontWeight = FontWeight.SemiBold
        )
        ButtonType.EQUALS -> ButtonStyle(
            bgColor = EqualsBtnBg,
            textColor = EqualsBtnText,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold
        )
    }

    val finalFontSize = fontSize ?: defaultStyle.fontSize
    val shape = if (isWide) RoundedCornerShape(40.dp) else CircleShape

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .padding(padding)
            .scale(scale)
            .clip(shape)
            .background(defaultStyle.bgColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onClick()
            }
    ) {
        Text(
            text = symbol,
            color = defaultStyle.textColor,
            fontSize = finalFontSize,
            fontWeight = defaultStyle.fontWeight
        )
    }
}

private data class ButtonStyle(
    val bgColor: Color,
    val textColor: Color,
    val fontSize: androidx.compose.ui.unit.TextUnit,
    val fontWeight: FontWeight
)
