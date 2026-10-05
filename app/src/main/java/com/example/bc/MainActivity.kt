package com.example.bc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.bc.logic.CalculatorState
import com.example.bc.logic.CalculatorViewModel
import com.example.bc.ui.CalculatorScreen
import com.example.bc.ui.theme.BCTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CalculatorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BCTheme {
                val state by viewModel.state.collectAsState()
                CalculatorScreen(
                    state = state,
                    onAction = viewModel::onAction,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Preview(name = "Portrait", showBackground = true, backgroundColor = 0xFF121214)
@Composable
fun CalculatorPreview() {
    BCTheme {
        CalculatorScreen(
            state = CalculatorState(
                expression = "1,250 + 75 × 2",
                liveResult = "1,400"
            ),
            onAction = {}
        )
    }
}

@Preview(
    name = "Landscape",
    showBackground = true,
    backgroundColor = 0xFF121214,
    widthDp = 840,
    heightDp = 390
)
@Composable
fun CalculatorLandscapePreview() {
    BCTheme {
        CalculatorScreen(
            state = CalculatorState(
                expression = "1,250 + 75 × 2",
                liveResult = "1,400"
            ),
            onAction = {}
        )
    }
}