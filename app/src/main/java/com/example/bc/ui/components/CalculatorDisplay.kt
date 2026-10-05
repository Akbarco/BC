package com.example.bc.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bc.logic.CalculatorState
import com.example.bc.ui.theme.ErrorColor
import com.example.bc.ui.theme.ExpressionColor
import com.example.bc.ui.theme.ResultColor

@Composable
fun CalculatorDisplay(
    state: CalculatorState,
    modifier: Modifier = Modifier
) {
    val topScrollState = rememberScrollState()
    val bottomScrollState = rememberScrollState()

    val topText = state.topDisplay
    val isOperating = topText.isNotEmpty()

    // Auto-scroll baris atas saat rumus/ekspresi bertambah
    LaunchedEffect(topText) {
        topScrollState.scrollTo(topScrollState.maxValue)
    }

    // Auto-scroll baris bawah saat hasil/angka berubah
    LaunchedEffect(state.bottomDisplay) {
        bottomScrollState.scrollTo(bottomScrollState.maxValue)
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Baris Atas: Rumus Operasi Aktif (berwarna putih saat tahap operasi)
        AnimatedVisibility(
            visible = isOperating,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Text(
                text = topText,
                fontSize = if (topText.length > 18) 24.sp else 32.sp,
                fontWeight = FontWeight.Normal,
                color = ResultColor, // Teks atas putih saat sedang operasi
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
                    .horizontalScroll(topScrollState)
            )
        }

        // Baris Bawah: Preview Pudar saat Operasi, berubah Putih Terang saat Selesai / Single Number
        val bottomText = state.bottomDisplay
        val isError = state.isError

        val bottomTextColor = when {
            isError -> ErrorColor
            isOperating -> ExpressionColor // Abu-abu pudar saat tahap operasi
            else -> ResultColor // Putih terang saat hasil final atau mengetik angka mandiri
        }

        val bottomFontSize = when {
            bottomText.length > 12 -> 34.sp
            bottomText.length > 8 -> 44.sp
            else -> 56.sp
        }

        Text(
            text = bottomText,
            fontSize = bottomFontSize,
            fontWeight = FontWeight.Normal,
            color = bottomTextColor,
            textAlign = TextAlign.End,
            maxLines = 1,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(bottomScrollState)
        )
    }
}
