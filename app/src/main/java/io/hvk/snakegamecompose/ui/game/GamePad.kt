package io.hvk.snakegamecompose.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlin.math.atan2
import kotlin.math.sqrt

@Composable
fun GamePad(
    onDirectionChange: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    var stickPosition by remember { mutableStateOf(Offset.Zero) }
    var currentDirection by remember { mutableStateOf<Direction?>(null) }
    val maxRadius = 50f

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        // Outer circle (base)
        Box(
            modifier = Modifier
                .size(200.dp)
                .shadow(8.dp, CircleShape)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.1f))
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val centerX = size.width / 2
                                val centerY = size.height / 2
                                val deltaX = offset.x - centerX
                                val deltaY = offset.y - centerY
                                val distance = sqrt(deltaX * deltaX + deltaY * deltaY)
                                val angle = atan2(deltaY, deltaX)
                                
                                // Determine direction based on angle
                                val direction = when {
                                    angle in -Math.PI/4..Math.PI/4 -> Direction.RIGHT
                                    angle in Math.PI/4..3*Math.PI/4 -> Direction.DOWN
                                    angle in -3*Math.PI/4..-Math.PI/4 -> Direction.UP
                                    else -> Direction.LEFT
                                }
                                
                                if (direction != currentDirection) {
                                    currentDirection = direction
                                    onDirectionChange(direction)
                                }
                                
                                // Limit stick position to maxRadius
                                val limitedDistance = minOf(distance, maxRadius)
                                stickPosition = Offset(
                                    (limitedDistance * kotlin.math.cos(angle)).toFloat(),
                                    (limitedDistance * kotlin.math.sin(angle)).toFloat()
                                )
                            },
                            onDrag = { change, dragAmount ->
                                val centerX = size.width / 2
                                val centerY = size.height / 2
                                val newX = stickPosition.x + dragAmount.x
                                val newY = stickPosition.y + dragAmount.y
                                val distance = sqrt(newX * newX + newY * newY)
                                val angle = atan2(newY, newX)
                                
                                // Determine direction based on angle
                                val direction = when {
                                    angle in -Math.PI/4..Math.PI/4 -> Direction.RIGHT
                                    angle in Math.PI/4..3*Math.PI/4 -> Direction.DOWN
                                    angle in -3*Math.PI/4..-Math.PI/4 -> Direction.UP
                                    else -> Direction.LEFT
                                }
                                
                                if (direction != currentDirection) {
                                    currentDirection = direction
                                    onDirectionChange(direction)
                                }
                                
                                // Limit stick position to maxRadius
                                val limitedDistance = minOf(distance, maxRadius)
                                stickPosition = Offset(
                                    (limitedDistance * kotlin.math.cos(angle)).toFloat(),
                                    (limitedDistance * kotlin.math.sin(angle)).toFloat()
                                )
                            },
                            onDragEnd = {
                                stickPosition = Offset.Zero
                                currentDirection = null
                            }
                        )
                    }
            ) {
                // Draw direction indicators
                val centerX = size.width / 2
                val centerY = size.height / 2
                val indicatorLength = 60f
                val indicatorColor = Color.White.copy(alpha = 0.3f)
                
                // Up indicator
                drawLine(
                    color = if (currentDirection == Direction.UP) Color.White else indicatorColor,
                    start = Offset(centerX, centerY - indicatorLength),
                    end = Offset(centerX, centerY),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
                
                // Down indicator
                drawLine(
                    color = if (currentDirection == Direction.DOWN) Color.White else indicatorColor,
                    start = Offset(centerX, centerY),
                    end = Offset(centerX, centerY + indicatorLength),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
                
                // Left indicator
                drawLine(
                    color = if (currentDirection == Direction.LEFT) Color.White else indicatorColor,
                    start = Offset(centerX - indicatorLength, centerY),
                    end = Offset(centerX, centerY),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
                
                // Right indicator
                drawLine(
                    color = if (currentDirection == Direction.RIGHT) Color.White else indicatorColor,
                    start = Offset(centerX, centerY),
                    end = Offset(centerX + indicatorLength, centerY),
                    strokeWidth = 4f,
                    cap = StrokeCap.Round
                )
            }
            
            // Joystick handle
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .offset(
                        x = (stickPosition.x * 1.5).dp,
                        y = (stickPosition.y * 1.5).dp
                    )
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f))
                    .align(Alignment.Center)
            )
        }
    }
} 