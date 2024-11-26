package io.hvk.snakegamecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import io.hvk.snakegamecompose.ui.theme.SnakeGameComposeTheme
import androidx.compose.material3.AlertDialog
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import android.content.Context
import androidx.compose.ui.platform.LocalContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnakeGameComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val context = LocalContext.current
    var showResetDialog by remember { mutableStateOf(false) }
    
    // Animation for the logo
    val infiniteTransition = rememberInfiniteTransition(label = "")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1B5E20),
                        Color(0xFF2E7D32),
                        Color(0xFF388E3C)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Animated Game Logo Container
            Box(
                modifier = Modifier
                    .scale(scale)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        spotColor = Color.Black.copy(alpha = 0.3f)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.1f))
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Snake Game Icon",
                    modifier = Modifier.size(120.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Snake Game",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.shadow(
                    elevation = 4.dp,
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            MenuButton(
                text = "Play Game",
                onClick = { /* TODO: Start game */ }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MenuButton(
                text = "Select Level",
                onClick = { /* TODO: Show levels */ }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MenuButton(
                text = "Reset Progress",
                onClick = { showResetDialog = true }
            )
        }

        // Reset Progress Dialog
        if (showResetDialog) {
            AlertDialog(
                onDismissRequest = { showResetDialog = false },
                title = {
                    Text(
                        "Reset Progress",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        "Are you sure you want to reset all progress? This action cannot be undone.",
                        color = Color.White
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            resetGameProgress(context)
                            showResetDialog = false
                        }
                    ) {
                        Text(
                            "Reset",
                            color = Color.Red,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { showResetDialog = false }
                    ) {
                        Text(
                            "Cancel",
                            color = Color.White
                        )
                    }
                },
                containerColor = Color(0xFF2E7D32),
                shape = RoundedCornerShape(16.dp)
            )
        }
    }
}

private fun resetGameProgress(context: Context) {
    // Clear SharedPreferences
    val sharedPrefs = context.getSharedPreferences("snake_game_prefs", Context.MODE_PRIVATE)
    sharedPrefs.edit().clear().apply()
    
    // You can add more reset logic here as needed:
    // - Clear high scores
    // - Reset unlocked levels
    // - Reset any other game state
}

@Composable
fun MenuButton(
    text: String,
    onClick: () -> Unit
) {
    // Button hover animation
    var isPressed by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .shadow(
                elevation = if (isPressed) 4.dp else 8.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color.Black.copy(alpha = 0.3f)
            )
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .width(250.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White.copy(alpha = 0.2f)
            ),
            shape = RoundedCornerShape(28.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                width = 2.dp,
                brush = Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.5f),
                        Color.White.copy(alpha = 0.2f)
                    )
                )
            ),
            interactionSource = remember { MutableInteractionSource() }
                .also { interactionSource ->
                    LaunchedEffect(interactionSource) {
                        interactionSource.interactions.collect { interaction ->
                            when (interaction) {
                                is PressInteraction.Press -> isPressed = true
                                is PressInteraction.Release -> isPressed = false
                                is PressInteraction.Cancel -> isPressed = false
                            }
                        }
                    }
                }
        ) {
            Text(
                text = text,
                fontSize = 20.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}