package com.example.bc.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .systemBarsPadding()
            .padding(12.dp),
        verticalArrangement = Arrangement.Bottom
    ) {
        // Display Area
        CalculatorDisplay(
            state = state,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Keypad Rows
        // Row 1: AC, ±, %, ÷
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val acText = if (state.expression.isNotEmpty() || state.isError) "AC" else "AC"
            CalculatorButton(
                symbol = acText,
                type = ButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Clear) }
            )
            CalculatorButton(
                symbol = "±",
                type = ButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.ToggleSign) }
            )
            CalculatorButton(
                symbol = "%",
                type = ButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Percentage) }
            )
            CalculatorButton(
                symbol = "÷",
                type = ButtonType.OPERATOR,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.DIVIDE)) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 2: 7, 8, 9, ×
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "7",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(7)) }
            )
            CalculatorButton(
                symbol = "8",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(8)) }
            )
            CalculatorButton(
                symbol = "9",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(9)) }
            )
            CalculatorButton(
                symbol = "×",
                type = ButtonType.OPERATOR,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.MULTIPLY)) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 3: 4, 5, 6, −
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "4",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(4)) }
            )
            CalculatorButton(
                symbol = "5",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(5)) }
            )
            CalculatorButton(
                symbol = "6",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(6)) }
            )
            CalculatorButton(
                symbol = "−",
                type = ButtonType.OPERATOR,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.SUBTRACT)) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 4: 1, 2, 3, +
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "1",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(1)) }
            )
            CalculatorButton(
                symbol = "2",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(2)) }
            )
            CalculatorButton(
                symbol = "3",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(3)) }
            )
            CalculatorButton(
                symbol = "+",
                type = ButtonType.OPERATOR,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Operation(CalculatorOperation.ADD)) }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 5: 0, ., ⌫, =
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorButton(
                symbol = "0",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Number(0)) }
            )
            CalculatorButton(
                symbol = ".",
                type = ButtonType.NUMBER,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Decimal) }
            )
            CalculatorButton(
                symbol = "⌫",
                type = ButtonType.FUNCTION,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Delete) }
            )
            CalculatorButton(
                symbol = "=",
                type = ButtonType.EQUALS,
                modifier = Modifier
                    .weight(1f)
                    .aspectRatio(1f),
                onClick = { onAction(CalculatorAction.Calculate) }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}
