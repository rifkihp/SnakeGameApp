package io.hvk.snakegamecompose.ui.screens

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.animation.core.*
import io.hvk.snakegamecompose.R
import io.hvk.snakegamecompose.ui.components.MenuButton

@Composable
fun MainScreen(onPlayClick: () -> Unit) {
    val context = LocalContext.current
    var showResetDialog by remember { mutableStateOf(false) }
    
    // Logo animation
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
            // Animated Logo Container
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
                onClick = onPlayClick
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            MenuButton(
                text = "Select Level",
                onClick = { /* TODO: Implement level selection */ }
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
    val sharedPrefs = context.getSharedPreferences("snake_game_prefs", Context.MODE_PRIVATE)
    sharedPrefs.edit().clear().apply()
} 