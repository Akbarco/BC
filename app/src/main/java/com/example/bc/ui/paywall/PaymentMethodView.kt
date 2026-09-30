package com.example.bc.ui.paywall

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bc.ui.theme.OperatorBtnBg

enum class PaymentBrand(val id: String, val brandName: String) {
    GOPAY("gopay", "GoPay"),
    QRIS("qris", "QRIS"),
    DANA("dana", "DANA"),
    BCA("bca", "BCA / VA")
}

@Composable
fun PaymentMethodSelector(
    selectedBrand: PaymentBrand,
    onSelect: (PaymentBrand) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PaymentBrand.entries.forEach { brand ->
            val isSelected = brand == selectedBrand
            PaymentBrandChip(
                brand = brand,
                isSelected = isSelected,
                onClick = { onSelect(brand) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun PaymentBrandChip(
    brand: PaymentBrand,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) OperatorBtnBg else Color(0xFF27272A)
    val bgColor = if (isSelected) Color(0xFF242428) else Color(0xFF161618)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor), RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(vertical = 10.dp, horizontal = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when (brand) {
                PaymentBrand.GOPAY -> GoPayLogo()
                PaymentBrand.QRIS -> QrisLogo()
                PaymentBrand.DANA -> DanaLogo()
                PaymentBrand.BCA -> BcaLogo()
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = brand.brandName,
                fontSize = 10.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = if (isSelected) Color.White else Color(0xFFA1A1AA)
            )
        }
    }
}

@Composable
fun GoPayLogo() {
    // Authentic GoPay Blue Circle with white Ring
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(Color(0xFF00AED6))
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .border(BorderStroke(2.5.dp, Color.White), CircleShape)
        )
    }
}

@Composable
fun QrisLogo() {
    // Official QRIS Red & Black Badge
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .background(Color.White)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Text(
            text = "QRIS",
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFFEE2E24),
            letterSpacing = (-0.5).sp
        )
    }
}

@Composable
fun DanaLogo() {
    // Official DANA Vivid Blue capsule
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFF118EEA))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            text = "DANA",
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            letterSpacing = 0.5.sp
        )
    }
}

@Composable
fun BcaLogo() {
    // Official BCA Navy Blue Badge
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(5.dp))
            .background(Color(0xFF005EAA))
            .padding(horizontal = 5.dp, vertical = 2.dp)
    ) {
        Text(
            text = "BCA",
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White
        )
    }
}
