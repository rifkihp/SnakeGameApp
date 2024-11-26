package io.hvk.snakegamecompose.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.rotate
import kotlinx.coroutines.delay
import kotlin.math.cos
import kotlin.math.sin
import io.hvk.snakegamecompose.ui.theme.GameColors

@Composable
fun AnimatedBackground() {
    var rotation by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(16L)
            rotation = (rotation + 0.2f) % 360f
        }
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val centerX = size.width / 2
        val centerY = size.height / 2
        val radius = minOf(size.width, size.height) * 0.4f
        
        rotate(rotation, Offset(centerX, centerY)) {
            // Draw rotating neon circles
            for (i in 0 until 4) {
                val angle = (i * 90f) * (Math.PI / 180f)
                val x = centerX + radius * cos(angle).toFloat()
                val y = centerY + radius * sin(angle).toFloat()
                
                drawCircle(
                    color = GameColors.NeonGreenAlpha30,
                    radius = 20f,
                    center = Offset(x, y)
                )
            }
            
            // Draw connecting lines
            for (i in 0 until 4) {
                val startAngle = (i * 90f) * (Math.PI / 180f)
                val endAngle = ((i + 1) * 90f) * (Math.PI / 180f)
                
                val startX = centerX + radius * cos(startAngle).toFloat()
                val startY = centerY + radius * sin(startAngle).toFloat()
                val endX = centerX + radius * cos(endAngle).toFloat()
                val endY = centerY + radius * sin(endAngle).toFloat()
                
                drawLine(
                    color = GameColors.NeonGreen,
                    start = Offset(startX, startY),
                    end = Offset(endX, endY),
                    strokeWidth = 4f
                )
            }
        }
        
        // Draw static outer glow
        for (i in 0 until 360 step 45) {
            val angle = i * (Math.PI / 180f)
            val outerRadius = radius + 30f
            val x = centerX + outerRadius * cos(angle).toFloat()
            val y = centerY + outerRadius * sin(angle).toFloat()
            
            drawCircle(
                color = GameColors.NeonGreenAlpha30,
                radius = 40f,
                center = Offset(x, y)
            )
        }
    }
} 