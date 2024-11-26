package io.hvk.snakegamecompose.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.hvk.snakegamecompose.ui.game.Direction
import io.hvk.snakegamecompose.ui.game.GameBoard
import io.hvk.snakegamecompose.ui.game.GamePad
import io.hvk.snakegamecompose.ui.game.model.GameLevel
import io.hvk.snakegamecompose.ui.game.model.GameState
import io.hvk.snakegamecompose.ui.theme.GameColors
import kotlinx.coroutines.delay

@Composable
fun GameScreen(
    gameLevel: GameLevel,
    onBackToMenu: () -> Unit
) {
    val context = LocalContext.current
    var gameState by remember { mutableStateOf(GameState()) }
    var isGamePaused by remember { mutableStateOf(false) }
    var countdown by remember { mutableIntStateOf(3) }
    var isGameStarted by remember { mutableStateOf(false) }
    var shouldRestartGame by remember { mutableStateOf(true) }
    var highScore by remember {
        mutableIntStateOf(
            context.getSharedPreferences("snake_game_prefs", 0)
                .getInt("high_score_${gameLevel.name}", 0)
        )
    }
    var remainingTime by remember { mutableIntStateOf(if (gameLevel == GameLevel.TIME_ATTACK) 30 else 0) }
    var previousScore by remember { mutableIntStateOf(0) }

    // Update high score when game ends
    fun updateHighScore() {
        if (gameState.score > highScore) {
            highScore = gameState.score
            context.getSharedPreferences("snake_game_prefs", 0)
                .edit()
                .putInt("high_score_${gameLevel.name}", highScore)
                .apply()
        }
    }

    // Game initialization and countdown
    LaunchedEffect(shouldRestartGame) {
        if (shouldRestartGame) {
            countdown = 3
            isGameStarted = false
            gameState = GameState()

            // Countdown phase
            while (countdown > 0) {
                delay(1000L)
                countdown--
            }
            isGameStarted = true
            shouldRestartGame = false
        }
    }

    // Game loop
    LaunchedEffect(isGameStarted) {
        while (isGameStarted) {
            delay(200L)
            if (!isGamePaused && !gameState.isGameOver) {
                gameState = gameState.move()
            }
        }
    }

    // Timer for Speed Race mode
    LaunchedEffect(isGameStarted, gameLevel) {
        if (isGameStarted && gameLevel == GameLevel.TIME_ATTACK) {
            while (remainingTime > 0 && !gameState.isGameOver) {
                delay(1000L)
                remainingTime--
                if (remainingTime <= 0) {
                    gameState = gameState.copy(isGameOver = true)
                }
            }
        }
    }

    // Check for food eaten and add time
    LaunchedEffect(gameState.score) {
        if (gameLevel == GameLevel.TIME_ATTACK && gameState.score > previousScore) {
            remainingTime += 3 // Add 3 seconds for each food eaten
            previousScore = gameState.score
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GameColors.BackgroundDark)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (isGameStarted && !gameState.isGameOver) {
                            isGamePaused = true
                        } else {
                            onBackToMenu()
                        }
                    },
                    modifier = Modifier
                        .shadow(
                            8.dp,
                            RoundedCornerShape(8.dp),
                            spotColor = GameColors.NeonGreenAlpha30
                        )
                        .background(GameColors.DarkGreen, RoundedCornerShape(8.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GameColors.NeonGreen
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .shadow(
                            8.dp,
                            RoundedCornerShape(8.dp),
                            spotColor = GameColors.NeonGreenAlpha30
                        )
                        .background(GameColors.DarkGreen, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Score: ${gameState.score}",
                        color = GameColors.NeonGreen,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Best: $highScore",
                        color = GameColors.NeonGreenAlpha70,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Placeholder for symmetry
                Box(modifier = Modifier.width(48.dp))
            }

            // Add Timer for Speed Race mode
            if (gameLevel == GameLevel.TIME_ATTACK) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Time",
                            color = GameColors.NeonGreenAlpha70,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "$remainingTime s",
                            color = GameColors.NeonGreen,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = remainingTime / 30f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(4.dp),
                        color = GameColors.NeonGreen,
                        trackColor = GameColors.DarkGreen,
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            // Game Board
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            ) {
                GameBoard(
                    gameState = gameState,
                    modifier = Modifier.fillMaxSize()
                )

                // Countdown Overlay
                if (!isGameStarted) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(GameColors.BlackAlpha70),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = countdown.toString(),
                            color = GameColors.NeonGreen,
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.shadow(
                                elevation = 20.dp,
                                spotColor = GameColors.NeonGreenAlpha50
                            )
                        )
                    }
                }
            }

            // Game Controls
            GamePad(
                onDirectionChange = { newDirection ->
                    if (isGameStarted && !gameState.isGameOver && !isGamePaused) {
                        val isValidMove = when (newDirection) {
                            Direction.UP -> gameState.direction != Direction.DOWN
                            Direction.DOWN -> gameState.direction != Direction.UP
                            Direction.LEFT -> gameState.direction != Direction.RIGHT
                            Direction.RIGHT -> gameState.direction != Direction.LEFT
                        }
                        if (isValidMove) {
                            gameState = gameState.copy(direction = newDirection)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }

        // Game Over Dialog
        if (gameState.isGameOver) {
            LaunchedEffect(Unit) {
                updateHighScore()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GameColors.BlackAlpha80),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = GameColors.NeonGreenAlpha50
                        )
                        .background(GameColors.DarkGreen, RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "GAME OVER",
                        color = GameColors.NeonGreen,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.shadow(
                            elevation = 16.dp,
                            spotColor = GameColors.NeonGreenAlpha50
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                GameColors.WhiteAlpha10,
                                RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Score: ${gameState.score}",
                            color = GameColors.NeonGreen,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )

                        if (gameState.score >= highScore) {
                            Text(
                                text = "New High Score!",
                                color = GameColors.TextHighlight,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        } else {
                            Text(
                                text = "Best: $highScore",
                                color = GameColors.NeonGreenAlpha70,
                                fontSize = 20.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = {
                                shouldRestartGame = true
                                isGamePaused = false
                                if (gameLevel == GameLevel.TIME_ATTACK) {
                                    remainingTime = 30
                                    previousScore = 0
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GameColors.WhiteAlpha10
                            ),
                            modifier = Modifier
                                .shadow(8.dp, RoundedCornerShape(24.dp))
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Try Again",
                                color = GameColors.NeonGreen,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Button(
                            onClick = onBackToMenu,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GameColors.WhiteAlpha10
                            ),
                            modifier = Modifier
                                .shadow(8.dp, RoundedCornerShape(24.dp))
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Main Menu",
                                color = GameColors.NeonGreen,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Pause Dialog
        if (isGamePaused && !gameState.isGameOver) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(GameColors.BlackAlpha70),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .shadow(
                            elevation = 16.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = GameColors.NeonGreenAlpha50
                        )
                        .background(GameColors.DarkGreen, RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "PAUSED",
                        color = GameColors.NeonGreen,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.shadow(
                            elevation = 8.dp,
                            spotColor = GameColors.NeonGreenAlpha50
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Do you want to quit?",
                        color = GameColors.NeonGreenAlpha70,
                        fontSize = 16.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = onBackToMenu,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GameColors.WhiteAlpha10
                            ),
                            modifier = Modifier.shadow(4.dp, RoundedCornerShape(24.dp))
                        ) {
                            Text(
                                text = "Quit",
                                color = GameColors.NeonGreen,
                                fontSize = 16.sp
                            )
                        }

                        Button(
                            onClick = { isGamePaused = false },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = GameColors.WhiteAlpha10
                            ),
                            modifier = Modifier.shadow(4.dp, RoundedCornerShape(24.dp))
                        ) {
                            Text(
                                text = "Resume",
                                color = GameColors.NeonGreen,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
} 