package com.example.bc.ui.paywall

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Particle(
    val startX: Float,
    val startY: Float,
    val angle: Double,
    val speed: Float,
    val size: Float,
    val color: Color,
    val rotationSpeed: Float,
    val shapeType: Int // 0: rect, 1: circle, 2: ribbon
)

@Composable
fun ConfettiExplosion(
    modifier: Modifier = Modifier,
    particleCount: Int = 65
) {
    val animProgress = remember { Animatable(0f) }

    val colors = listOf(
        Color(0xFFFFD700), // Gold
        Color(0xFFFF9F0A), // Neon Orange
        Color(0xFF30D158), // Vibrant Green
        Color(0xFF00C7BE), // Teal
        Color(0xFFBF5AF2), // Purple
        Color(0xFFFF375F), // Pink
        Color(0xFF5E5CE6), // Indigo
        Color(0xFFFFFFFF)  // Crisp White
    )

    val particles = remember {
        val random = Random(42)
        List(particleCount) {
            val angle = random.nextDouble(0.0, Math.PI * 2.0)
            val speed = random.nextFloat() * 450f + 250f
            val size = random.nextFloat() * 12f + 8f
            val color = colors[random.nextInt(colors.size)]
            val rotationSpeed = (random.nextFloat() - 0.5f) * 720f
            val shapeType = random.nextInt(3)
            Particle(
                startX = 0f,
                startY = 0f,
                angle = angle,
                speed = speed,
                size = size,
                color = color,
                rotationSpeed = rotationSpeed,
                shapeType = shapeType
            )
        }
    }

    LaunchedEffect(Unit) {
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 1400,
                easing = FastOutLinearInEasing
            )
        )
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val progress = animProgress.value

        particles.forEach { p ->
            val distance = p.speed * progress
            val gravity = 300f * progress * progress
            val x = centerX + (distance * cos(p.angle)).toFloat()
            val y = centerY + (distance * sin(p.angle)).toFloat() + gravity
            val currentAlpha = (1f - progress).coerceIn(0f, 1f)
            val rotation = p.rotationSpeed * progress

            rotate(degrees = rotation, pivot = Offset(x, y)) {
                val drawColor = p.color.copy(alpha = currentAlpha)
                when (p.shapeType) {
                    0 -> { // Rectangle / Confetti paper
                        drawRect(
                            color = drawColor,
                            topLeft = Offset(x - p.size / 2, y - p.size / 2),
                            size = Size(p.size, p.size * 0.6f)
                        )
                    }
                    1 -> { // Circle
                        drawCircle(
                            color = drawColor,
                            radius = p.size / 2.5f,
                            center = Offset(x, y)
                        )
                    }
                    2 -> { // Ribbon
                        drawRect(
                            color = drawColor,
                            topLeft = Offset(x - p.size / 4, y - p.size),
                            size = Size(p.size / 2, p.size * 1.5f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SuccessPulseRings(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "PulseTransition")
    val scale1 by transition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Scale1"
    )
    val alpha1 by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Alpha1"
    )

    Canvas(modifier = modifier) {
        drawCircle(
            color = Color(0xFF30D158).copy(alpha = alpha1),
            radius = (size.minDimension / 2f) * scale1
        )
    }
}
