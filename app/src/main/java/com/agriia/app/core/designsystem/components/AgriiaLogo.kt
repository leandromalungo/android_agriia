package com.agriia.app.core.designsystem.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.agriia.app.core.designsystem.LimeAccent
import kotlin.math.cos
import kotlin.math.sin

/**
 * Ícone orgânico do agriia: composto por pétalas/folhas radiais orgânicas
 * inspiradas na natureza e na inteligência artificial conectada.
 */
@Composable
fun AgriiaIcon(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    tint: Color = MaterialTheme.colorScheme.primary,
    accentTint: Color = LimeAccent,
    animated: Boolean = false
) {
    val rotation = remember { Animatable(0f) }

    LaunchedEffect(animated) {
        if (animated) {
            rotation.animateTo(
                targetValue = 360f,
                animationSpec = infiniteRepeatable(
                    animation = tween(12000, easing = LinearEasing)
                )
            )
        }
    }

    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2f, this.size.height / 2f)
        val outerRadius = this.size.minDimension * 0.44f
        val innerRadius = this.size.minDimension * 0.16f
        val petalCount = 10
        val strokeWidth = this.size.minDimension * 0.09f

        rotate(rotation.value, pivot = center) {
            for (i in 0 until petalCount) {
                val angleDeg = (i * 360f / petalCount)
                val angleRad = Math.toRadians(angleDeg.toDouble())
                val isAccent = i % 2 == 0
                val color = if (isAccent) tint else accentTint

                val startX = center.x + (innerRadius * cos(angleRad)).toFloat()
                val startY = center.y + (innerRadius * sin(angleRad)).toFloat()
                val endX = center.x + (outerRadius * cos(angleRad)).toFloat()
                val endY = center.y + (outerRadius * sin(angleRad)).toFloat()

                drawLine(
                    color = color,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = strokeWidth,
                    cap = StrokeCap.Round
                )

                // Nó externo sutil (conexão neural orgânica)
                val nodeRadius = strokeWidth * 0.6f
                drawCircle(
                    color = color,
                    radius = nodeRadius,
                    center = Offset(endX, endY)
                )
            }

            // Nó central
            drawCircle(
                color = tint,
                radius = innerRadius * 0.7f,
                center = center
            )
        }
    }
}

/**
 * Logo completo do agriia (ícone + tipografia serifada sofisticada)
 */
@Composable
fun AgriiaLogo(
    modifier: Modifier = Modifier,
    iconSize: Dp = 32.dp,
    fontSize: TextUnit = 24.sp,
    textColor: Color = MaterialTheme.colorScheme.onBackground,
    showText: Boolean = true,
    animatedIcon: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AgriiaIcon(
            size = iconSize,
            animated = animatedIcon
        )
        if (showText) {
            Text(
                text = "agriia",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Medium,
                fontSize = fontSize,
                color = textColor,
                letterSpacing = (-0.5).sp
            )
        }
    }
}
