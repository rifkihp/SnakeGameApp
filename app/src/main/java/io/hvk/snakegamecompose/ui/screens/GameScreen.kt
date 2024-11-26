package io.hvk.snakegamecompose.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.hvk.snakegamecompose.ui.game.Direction
import io.hvk.snakegamecompose.ui.game.GameBoard
import io.hvk.snakegamecompose.ui.game.GamePad
import io.hvk.snakegamecompose.ui.game.model.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GameScreen(onBackToMenu: () -> Unit) {
    val context = LocalContext.current
    var gameState by remember { mutableStateOf(GameState()) }
    var isGamePaused by remember { mutableStateOf(false) }
    var countdown by remember { mutableStateOf(3) }
    var isGameStarted by remember { mutableStateOf(false) }
    var highScore by remember { 
        mutableStateOf(
            context.getSharedPreferences("snake_game_prefs", 0)
                .getInt("high_score", 0)
        )
    }

    // Update high score when game ends
    fun updateHighScore() {
        if (gameState.score > highScore) {
            highScore = gameState.score
            context.getSharedPreferences("snake_game_prefs", 0)
                .edit()
                .putInt("high_score", highScore)
                .apply()
        }
    }

    // Countdown effect
    LaunchedEffect(Unit) {
        while (countdown > 0) {
            delay(1000L)
            countdown--
        }
        isGameStarted = true
    }
    
    // Game loop
    LaunchedEffect(isGameStarted) {
        while (true) {
            delay(200L) // Control game speed
            if (isGameStarted && !isGamePaused && !gameState.isGameOver) {
                gameState = gameState.move()
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B5E20))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar with Back Button and Scores
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back Button
                IconButton(
                    onClick = {
                        if (isGameStarted && !gameState.isGameOver) {
                            isGamePaused = true
                            // Show confirmation dialog
                            // (We'll add this later)
                        } else {
                            onBackToMenu()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                // Scores
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Score: ${gameState.score}",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Best: $highScore",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Placeholder to maintain centering
                Box(modifier = Modifier.width(48.dp))
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
                            .background(Color.Black.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = countdown.toString(),
                            color = Color.White,
                            fontSize = 72.sp,
                            fontWeight = FontWeight.Bold
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
                    .background(Color.Black.copy(alpha = 0.8f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .shadow(
                            elevation = 8.dp,
                            shape = RoundedCornerShape(16.dp),
                            spotColor = Color.Black.copy(alpha = 0.3f)
                        )
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFF388E3C),
                                    Color(0xFF1B5E20)
                                )
                            ),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Game Over Text with glow effect
                    Text(
                        text = "GAME OVER",
                        color = Color.White,
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .shadow(
                                elevation = 8.dp,
                                spotColor = Color.Red.copy(alpha = 0.5f)
                            )
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Score display
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .background(
                                Color.White.copy(alpha = 0.1f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Score: ${gameState.score}",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        if (gameState.score >= highScore) {
                            Text(
                                text = "New High Score!",
                                color = Color.Yellow,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        } else {
                            Text(
                                text = "Best: $highScore",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 20.sp,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Buttons
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { 
                                gameState = GameState()
                                countdown = 3
                                isGameStarted = false
                                isGamePaused = false
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .shadow(4.dp, RoundedCornerShape(24.dp))
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Try Again",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Button(
                            onClick = onBackToMenu,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White.copy(alpha = 0.2f)
                            ),
                            modifier = Modifier
                                .shadow(4.dp, RoundedCornerShape(24.dp))
                                .height(48.dp)
                        ) {
                            Text(
                                text = "Main Menu",
                                color = Color.White,
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
            AlertDialog(
                onDismissRequest = { isGamePaused = false },
                title = { Text("Pause", color = Color.White) },
                text = { Text("Do you want to quit the game?", color = Color.White) },
                confirmButton = {
                    TextButton(onClick = onBackToMenu) {
                        Text("Quit", color = Color.White)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { isGamePaused = false }) {
                        Text("Resume", color = Color.White)
                    }
                },
                containerColor = Color(0xFF2E7D32),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
} 