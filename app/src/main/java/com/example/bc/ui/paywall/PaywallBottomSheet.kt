package com.example.bc.ui.paywall

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bc.ui.theme.DarkBackground
import com.example.bc.ui.theme.DarkSurface
import com.example.bc.ui.theme.ExpressionColor
import com.example.bc.ui.theme.OperatorBtnBg
import com.example.bc.ui.theme.ResultColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class PaymentState {
    SELECTING,
    PROCESSING,
    SUCCESS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaywallBottomSheet(
    onDismissRequest: () -> Unit,
    onPaymentSuccess: () -> Unit,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
) {
    var selectedPlanId by remember { mutableStateOf("pro") }
    var selectedPaymentMethod by remember { mutableStateOf("QRIS Instant") }
    var paymentState by remember { mutableStateOf(PaymentState.SELECTING) }
    var loadingMessage by remember { mutableStateOf("Menghubungi Server Bank...") }

    val haptic = LocalHapticFeedback.current
    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {
            if (paymentState != PaymentState.PROCESSING) {
                onDismissRequest()
            }
        },
        sheetState = sheetState,
        containerColor = DarkSurface,
        dragHandle = {
            Box(
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .width(40.dp)
                    .height(4.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3F3F46))
            )
        }
    ) {
        AnimatedContent(
            targetState = paymentState,
            transitionSpec = {
                fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(300))
            },
            label = "PaymentStateTransition"
        ) { state ->
            when (state) {
                PaymentState.SELECTING -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Header Lock Icon
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFFFF9F0A), Color(0xFFFF453A))
                                    )
                                )
                        ) {
                            Text(text = "🔒", fontSize = 26.sp)
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Buka Hasil Perhitungan",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = ResultColor,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Hasil kalkulasi kamu terkunci fitur Premium. Pilih paket langganan untuk membuka jawaban secara instan.",
                            fontSize = 13.sp,
                            color = ExpressionColor,
                            textAlign = TextAlign.Center,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // 3 Subscription Plans (Biasa, Pro, Ultra)
                        SamplePlans.forEach { plan ->
                            PlanCard(
                                plan = plan,
                                isSelected = plan.id == selectedPlanId,
                                onClick = {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    selectedPlanId = plan.id
                                }
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Payment Methods Selector
                        Text(
                            text = "Metode Pembayaran",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = ExpressionColor,
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            listOf("QRIS Instant", "GoPay", "DANA", "Credit Card").forEach { method ->
                                val isSelectedMethod = method == selectedPaymentMethod
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelectedMethod) Color(0xFF27272A) else Color(0xFF18181B)
                                        )
                                        .border(
                                            BorderStroke(
                                                1.dp,
                                                if (isSelectedMethod) OperatorBtnBg else Color(0xFF3F3F46)
                                            ),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                            selectedPaymentMethod = method
                                        }
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = method,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelectedMethod) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelectedMethod) Color.White else ExpressionColor,
                                        maxLines = 1
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Action Button
                        val selectedPlan = SamplePlans.find { it.id == selectedPlanId } ?: SamplePlans[1]
                        Button(
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                paymentState = PaymentState.PROCESSING
                                coroutineScope.launch {
                                    loadingMessage = "Menghubungi Server Bank..."
                                    delay(900)
                                    loadingMessage = "Memverifikasi Transaksi ${selectedPlan.title}..."
                                    delay(900)
                                    loadingMessage = "Membuka Kunci Jawaban..."
                                    delay(600)
                                    paymentState = PaymentState.SUCCESS
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    delay(1200)
                                    onPaymentSuccess()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = OperatorBtnBg,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp)
                        ) {
                            Text(
                                text = "⚡ Bayar ${selectedPlan.price} & Buka Hasil",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Batalkan kapan saja. Syarat & Ketentuan berlaku.",
                            fontSize = 11.sp,
                            color = Color(0xFF71717A),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                PaymentState.PROCESSING -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = OperatorBtnBg,
                            strokeWidth = 3.dp,
                            modifier = Modifier.size(52.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Memproses Pembayaran...",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = ResultColor
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = loadingMessage,
                            fontSize = 13.sp,
                            color = ExpressionColor,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                PaymentState.SUCCESS -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp, vertical = 48.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF30D158))
                        ) {
                            Text(text = "✓", fontSize = 32.sp, color = Color.White, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        Text(
                            text = "Pembayaran Berhasil! 🎉",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = ResultColor
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Paket aktif! Kunci hasil kalkulator telah dibuka.",
                            fontSize = 13.sp,
                            color = Color(0xFF30D158),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlanCard(
    plan: SubscriptionPlan,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) OperatorBtnBg else Color(0xFF27272A)
    val bgColor = if (isSelected) Color(0xFF27272A) else Color(0xFF18181B)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .border(BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor), RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = plan.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) Color.White else Color(0xFFD4D4D8)
                    )

                    plan.badge?.let { badge ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(if (plan.isPopular) OperatorBtnBg else Color(0xFF3F3F46))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = badge,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // Radio Indicator
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(20.dp)
                        .clip(CircleShape)
                        .border(
                            BorderStroke(2.dp, if (isSelected) OperatorBtnBg else Color(0xFF71717A)),
                            CircleShape
                        )
                        .background(if (isSelected) OperatorBtnBg else Color.Transparent)
                ) {
                    if (isSelected) {
                        Text(text = "✓", fontSize = 11.sp, color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = plan.price,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = if (isSelected) OperatorBtnBg else Color.White
                )
                Text(
                    text = " " + plan.period,
                    fontSize = 12.sp,
                    color = ExpressionColor
                )
                plan.originalPrice?.let { orig ->
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = orig,
                        fontSize = 12.sp,
                        color = Color(0xFF71717A),
                        textDecoration = TextDecoration.LineThrough
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Bullet points
            plan.features.forEach { feat ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "•", color = OperatorBtnBg, fontSize = 14.sp, modifier = Modifier.padding(end = 6.dp))
                    Text(text = feat, color = Color(0xFFA1A1AA), fontSize = 11.sp)
                }
            }
        }
    }
}
