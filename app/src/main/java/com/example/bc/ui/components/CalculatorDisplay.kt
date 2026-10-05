package com.example.bc.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bc.logic.CalculatorState
import com.example.bc.ui.theme.ErrorColor
import com.example.bc.ui.theme.ResultColor

@Composable
fun CalculatorDisplay(
    state: CalculatorState,
    modifier: Modifier = Modifier
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val scrollState = rememberScrollState()
    val displayText = state.bottomDisplay
    val isError = state.isError

    // Auto-scroll ke ujung kanan saat angka atau operasi bertambah
    LaunchedEffect(displayText) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    val paddingH = if (isLandscape) 16.dp else 24.dp
    val paddingV = if (isLandscape) 8.dp else 20.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = paddingH, vertical = paddingV),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        val fontSize = when {
            isLandscape && displayText.length > 12 -> 26.sp
            isLandscape && displayText.length > 8 -> 34.sp
            isLandscape -> 44.sp
            displayText.length > 12 -> 36.sp
            displayText.length > 8 -> 46.sp
            else -> 58.sp
        }

        Text(
            text = displayText,
            fontSize = fontSize,
            fontWeight = FontWeight.Light,
            color = if (isError) ErrorColor else ResultColor,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        )
    }
}
