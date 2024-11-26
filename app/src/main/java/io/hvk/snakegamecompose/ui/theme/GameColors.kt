package io.hvk.snakegamecompose.ui.theme

import androidx.compose.ui.graphics.Color

object GameColors {
    // Base colors
    val Black = Color.Black
    val White = Color.White
    val NeonGreen = Color(0xFF00FF00)
    val DarkGreen = Color(0xFF002000)
    
    // Background colors
    val BackgroundDark = Black
    val BackgroundGreen = Color(0xFF1B5E20)
    val BackgroundMediumGreen = Color(0xFF2E7D32)
    val BackgroundLightGreen = Color(0xFF388E3C)
    
    // Transparent colors
    val WhiteAlpha10 = White.copy(alpha = 0.1f)
    val WhiteAlpha20 = White.copy(alpha = 0.2f)
    val WhiteAlpha30 = White.copy(alpha = 0.3f)
    val WhiteAlpha70 = White.copy(alpha = 0.7f)
    val WhiteAlpha80 = White.copy(alpha = 0.8f)
    
    val BlackAlpha70 = Black.copy(alpha = 0.7f)
    val BlackAlpha80 = Black.copy(alpha = 0.8f)
    
    val NeonGreenAlpha30 = NeonGreen.copy(alpha = 0.3f)
    val NeonGreenAlpha50 = NeonGreen.copy(alpha = 0.5f)
    val NeonGreenAlpha70 = NeonGreen.copy(alpha = 0.7f)
    
    // Game specific colors
    val SnakeBody = White
    val FoodColor = Color.Red
    val GridLine = WhiteAlpha20
    
    // Text colors
    val TextPrimary = NeonGreen
    val TextSecondary = WhiteAlpha70
    val TextHighlight = Color.Yellow
} 