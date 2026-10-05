package com.example.bc.ui

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bc.logic.CalculatorAction
import com.example.bc.logic.CalculatorOperation
import com.example.bc.logic.CalculatorState
import com.example.bc.ui.components.ButtonType
import com.example.bc.ui.components.CalculatorButton
import com.example.bc.ui.components.CalculatorDisplay
import com.example.bc.ui.theme.DarkBackground

@Composable
fun CalculatorScreen(
    state: CalculatorState,
    onAction: (CalculatorAction) -> Unit,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        // Mode Lanskap: Layar terbagi 2 (Kiri: Display, Kanan: Keypad)
        Row(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBackground)
                .systemBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Kolom Kiri: Display Area Luas
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                contentAlignment = Alignment.BottomEnd
            ) {
                CalculatorDisplay(
                    state = state,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Kolom Kanan: Keypad 5 Baris yang mengisi tinggi layar
            KeypadGrid(
                onAction = onAction,
                isLandscape = true,
                modifier = Modifier
                    .weight(1.3f)
                    .fillMaxHeight()
            )
        }
    } else {
        // Mode Potret: Layout standar (Atas: Display, Bawah: Keypad)
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(DarkBackground)
                .systemBarsPadding()
                .padding(12.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            CalculatorDisplay(
                state = state,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(16.dp))

            KeypadGrid(
                onAction = onAction,
                isLandscape = false,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun KeypadGrid(
    onAction: (CalculatorAction) -> Unit,
    isLandscape: Boolean,
    modifier: Modifier = Modifier
) {
    val spacing = if (isLandscape) 4.dp else 8.dp
    val btnPadding = if (isLandscape) 2.dp else 6.dp

    val numFontSize = if (isLandscape) 22.sp else 30.sp
    val funcFontSize = if (isLandscape) 18.sp else 24.sp
    val opFontSize = if (isLandscape) 24.sp else 32.sp

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        @Composable
        fun RowScope.KeypadButton(
            symbol: String,
            type: ButtonType = ButtonType.NUMBER,
            fontSize: androidx.compose.ui.unit.TextUnit? = null,
            onClick: () -> Unit
        ) {
            val btnModifier = if (isLandscape) {
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
            } else {
                Modifier
                    .weight(1f)
                    .aspectRatio(1f)
            }
            CalculatorButton(
                symbol = symbol,
                type = type,
                fontSize = fontSize,
                padding = btnPadding,
                modifier = btnModifier,
                onClick = onClick
            )
        }

        // Row 1: AC, ±, %, ÷
        Row(
            modifier = if (isLandscape) Modifier.fillMaxWidth().weight(1f) else Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            KeypadButton(symbol = "AC", type = ButtonType.FUNCTION, fontSize = funcFontSize) {
                onAction(CalculatorAction.Clear)
            }
            KeypadButton(symbol = "±", type = ButtonType.FUNCTION, fontSize = funcFontSize) {
                onAction(CalculatorAction.ToggleSign)
            }
            KeypadButton(symbol = "%", type = ButtonType.FUNCTION, fontSize = funcFontSize) {
                onAction(CalculatorAction.Percentage)
            }
            KeypadButton(symbol = "÷", type = ButtonType.OPERATOR, fontSize = opFontSize) {
                onAction(CalculatorAction.Operation(CalculatorOperation.DIVIDE))
            }
        }

        // Row 2: 7, 8, 9, ×
        Row(
            modifier = if (isLandscape) Modifier.fillMaxWidth().weight(1f) else Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            KeypadButton(symbol = "7", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(7))
            }
            KeypadButton(symbol = "8", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(8))
            }
            KeypadButton(symbol = "9", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(9))
            }
            KeypadButton(symbol = "×", type = ButtonType.OPERATOR, fontSize = opFontSize) {
                onAction(CalculatorAction.Operation(CalculatorOperation.MULTIPLY))
            }
        }

        // Row 3: 4, 5, 6, −
        Row(
            modifier = if (isLandscape) Modifier.fillMaxWidth().weight(1f) else Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            KeypadButton(symbol = "4", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(4))
            }
            KeypadButton(symbol = "5", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(5))
            }
            KeypadButton(symbol = "6", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(6))
            }
            KeypadButton(symbol = "−", type = ButtonType.OPERATOR, fontSize = opFontSize) {
                onAction(CalculatorAction.Operation(CalculatorOperation.SUBTRACT))
            }
        }

        // Row 4: 1, 2, 3, +
        Row(
            modifier = if (isLandscape) Modifier.fillMaxWidth().weight(1f) else Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            KeypadButton(symbol = "1", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(1))
            }
            KeypadButton(symbol = "2", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(2))
            }
            KeypadButton(symbol = "3", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(3))
            }
            KeypadButton(symbol = "+", type = ButtonType.OPERATOR, fontSize = opFontSize) {
                onAction(CalculatorAction.Operation(CalculatorOperation.ADD))
            }
        }

        // Row 5: 0, ., ⌫, =
        Row(
            modifier = if (isLandscape) Modifier.fillMaxWidth().weight(1f) else Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing)
        ) {
            KeypadButton(symbol = "0", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Number(0))
            }
            KeypadButton(symbol = ".", type = ButtonType.NUMBER, fontSize = numFontSize) {
                onAction(CalculatorAction.Decimal)
            }
            KeypadButton(symbol = "⌫", type = ButtonType.FUNCTION, fontSize = funcFontSize) {
                onAction(CalculatorAction.Delete)
            }
            KeypadButton(symbol = "=", type = ButtonType.EQUALS, fontSize = opFontSize) {
                onAction(CalculatorAction.Calculate)
            }
        }
    }
}
