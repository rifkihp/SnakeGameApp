package io.hvk.snakegamecompose.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import io.hvk.snakegamecompose.ui.game.model.Point
import kotlinx.coroutines.delay
import kotlin.random.Random

@Composable
fun AnimatedBackground() {
    var snakePoints by remember { mutableStateOf(generateInitialSnake()) }
    var direction by remember { mutableStateOf(getRandomDirection()) }
    
    LaunchedEffect(Unit) {
        while (true) {
            delay(200L) // Snake movement speed
            snakePoints = moveSnake(snakePoints, direction)
            
            // Randomly change direction occasionally
            if (Random.nextFloat() < 0.1f) { // 10% chance to change direction
                direction = getRandomDirection()
            }
        }
    }
    
    Canvas(modifier = Modifier.fillMaxSize()) {
        val cellSize = size.width / 30 // Smaller grid for background

        // Draw snake
        snakePoints.forEach { point ->
            drawRect(
                color = Color.White.copy(alpha = 0.1f),
                topLeft = Offset(point.x * cellSize, point.y * cellSize),
                size = Size(cellSize, cellSize)
            )
        }
    }
}

private fun generateInitialSnake(): List<Point> {
    val startX = Random.nextInt(5, 25)
    val startY = Random.nextInt(5, 25)
    return List(8) { index -> Point(startX, startY + index) }
}

private fun getRandomDirection(): Point {
    return when (Random.nextInt(4)) {
        0 -> Point(0, -1) // Up
        1 -> Point(0, 1)  // Down
        2 -> Point(-1, 0) // Left
        else -> Point(1, 0) // Right
    }
}

private fun moveSnake(snake: List<Point>, direction: Point): List<Point> {
    val head = snake.first()
    var newHead = head + direction
    
    // Wrap around screen edges
    newHead = Point(
        (newHead.x + 30) % 30,
        (newHead.y + 30) % 30
    )
    
    return listOf(newHead) + snake.dropLast(1)
} 