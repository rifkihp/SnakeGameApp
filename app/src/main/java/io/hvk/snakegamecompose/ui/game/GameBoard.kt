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
import io.hvk.snakegamecompose.ui.theme.GameColors

@Composable
fun GameBoard(
    gameState: GameState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .background(
                GameColors.DarkGreen,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cellSize = size.width / GameState.BOARD_SIZE
            val padding = cellSize * 0.1f // 10% padding

            // Draw grid
            for (i in 0..GameState.BOARD_SIZE) {
                drawLine(
                    color = GameColors.GridLine,
                    start = Offset(i * cellSize, 0f),
                    end = Offset(i * cellSize, size.height),
                    strokeWidth = 1f
                )
                drawLine(
                    color = GameColors.GridLine,
                    start = Offset(0f, i * cellSize),
                    end = Offset(size.width, i * cellSize),
                    strokeWidth = 1f
                )
            }

            // Draw snake with rounded segments and glow
            gameState.snake.forEach { point ->
                // Draw glow effect
                drawRect(
                    color = GameColors.NeonGreen.copy(alpha = 0.3f),
                    topLeft = Offset(
                        point.x * cellSize + padding - padding,
                        point.y * cellSize + padding - padding
                    ),
                    size = Size(
                        cellSize - padding * 0.5f,
                        cellSize - padding * 0.5f
                    )
                )
                
                // Draw snake segment
                drawRect(
                    color = GameColors.SnakeBody,
                    topLeft = Offset(
                        point.x * cellSize + padding,
                        point.y * cellSize + padding
                    ),
                    size = Size(
                        cellSize - padding * 2,
                        cellSize - padding * 2
                    )
                )
            }

            // Draw food with glow effect
            val foodCenterX = gameState.food.x * cellSize + cellSize / 2
            val foodCenterY = gameState.food.y * cellSize + cellSize / 2
            val foodRadius = (cellSize - padding * 2) / 2

            // Draw food glow
            drawCircle(
                color = GameColors.FoodColor.copy(alpha = 0.3f),
                radius = foodRadius + padding,
                center = Offset(foodCenterX, foodCenterY)
            )
            
            // Draw food
            drawCircle(
                color = GameColors.FoodColor,
                radius = foodRadius,
                center = Offset(foodCenterX, foodCenterY)
            )
        }
    }
} 