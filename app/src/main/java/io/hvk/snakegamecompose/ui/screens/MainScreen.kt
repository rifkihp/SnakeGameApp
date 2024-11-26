package io.hvk.snakegamecompose.ui.screens

import android.content.Context
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextAlign.Companion
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.hvk.snakegamecompose.R
import io.hvk.snakegamecompose.ui.components.AnimatedBackground
import io.hvk.snakegamecompose.ui.components.MenuButton
import io.hvk.snakegamecompose.ui.game.model.GameLevel
import io.hvk.snakegamecompose.ui.theme.GameColors.NeonGreen

@Composable
fun MainScreen(onPlayClick: () -> Unit) {
    val context = LocalContext.current
    var showResetDialog by remember { mutableStateOf(false) }
    var currentLevelIndex by remember { mutableIntStateOf(0) }
    val levels = GameLevel.entries.toTypedArray()

    // Animation for level transition
    val slideAnim = rememberInfiniteTransition(label = "")
    val scale by slideAnim.animateFloat(
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
            .background(Color.Black)
    ) {
        AnimatedBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo container with neon effect
            Box(
                modifier = Modifier
                    .scale(scale)
                    .shadow(
                        elevation = 16.dp,
                        spotColor = Color(0xFF00FF00).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF002000))
                    .padding(16.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Snake Game Icon",
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Title with neon effect
            Text(
                text = "Snake Game",
                fontSize = 55.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF00FF00),
                modifier = Modifier.shadow(
                    elevation = 20.dp,
                    spotColor = Color(0xFF00FF00).copy(alpha = 0.5f)
                )
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Level Selector with neon theme
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        currentLevelIndex = (currentLevelIndex - 1 + levels.size) % levels.size
                    },
                    modifier = Modifier
                        .shadow(8.dp, CircleShape, spotColor = Color(0xFF00FF00).copy(alpha = 0.5f))
                        .background(Color(0xFF002000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Previous Level",
                        tint = Color(0xFF00FF00)
                    )
                }

                AnimatedContent(
                    targetState = currentLevelIndex,
                    transitionSpec = {
                        if (targetState > initialState) {
                            (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> -width } + fadeOut())
                        } else {
                            (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                                slideOutHorizontally { width -> width } + fadeOut())
                        }
                    }, label = ""
                ) { index ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .size(200.dp, 150.dp)
                            .shadow(
                                8.dp,
                                RoundedCornerShape(8.dp),
                                spotColor = NeonGreen.copy(alpha = 0.3f)
                            )
                            .background(Color(0xFF002000), RoundedCornerShape(8.dp))
                            .padding(16.dp)
                    ) {
                        Text(
                            text = levels[index].title,
                            color = Color(0xFF00FF00),
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = levels[index].description,
                            color = NeonGreen.copy(alpha = 0.7f),
                            fontSize = 20.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                IconButton(
                    onClick = {
                        currentLevelIndex = (currentLevelIndex + 1) % levels.size
                    },
                    modifier = Modifier
                        .shadow(8.dp, CircleShape, spotColor = Color(0xFF00FF00).copy(alpha = 0.5f))
                        .background(Color(0xFF002000), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowRight,
                        contentDescription = "Next Level",
                        tint = Color(0xFF00FF00)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            MenuButton(
                text = "Play Game",
                onClick = onPlayClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            MenuButton(
                text = "Reset Progress",
                onClick = { showResetDialog = true }
            )
        }

        // Reset Progress Dialog remains the same...
    }
}

private fun resetGameProgress(context: Context) {
    val sharedPrefs = context.getSharedPreferences("snake_game_prefs", Context.MODE_PRIVATE)
    sharedPrefs.edit().clear().apply()
} 