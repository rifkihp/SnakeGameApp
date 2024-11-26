package io.hvk.snakegamecompose.ui.game

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.hvk.snakegamecompose.ui.game.model.GameState
import io.hvk.snakegamecompose.ui.game.model.Point

@Composable
fun GameBoard(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                Color(0xFF2E7D32),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellSize = size.width / GameState.BOARD_SIZE

            // Draw grid
            for (i in 0..GameState.BOARD_SIZE) {
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(i * cellSize, 0f),
                    end = Offset(i * cellSize, size.height),
                    strokeWidth = 1f
                )
                drawLine(
                    color = Color.White.copy(alpha = 0.2f),
                    start = Offset(0f, i * cellSize),
                    end = Offset(size.width, i * cellSize),
                    strokeWidth = 1f
                )
            }

            // Draw snake
            gameState.snake.forEach { point ->
                drawRect(
                    color = Color.White,
                    topLeft = Offset(point.x * cellSize, point.y * cellSize),
                    size = Size(cellSize, cellSize)
                )
            }

            // Draw food
            drawCircle(
                color = Color.Red,
                center = Offset(
                    gameState.food.x * cellSize + cellSize / 2,
                    gameState.food.y * cellSize + cellSize / 2
                ),
                radius = cellSize / 3
            )
        }
    }
} 