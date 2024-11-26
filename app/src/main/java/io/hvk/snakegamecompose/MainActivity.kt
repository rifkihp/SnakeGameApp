package io.hvk.snakegamecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import io.hvk.snakegamecompose.ui.theme.SnakeGameComposeTheme
import io.hvk.snakegamecompose.ui.navigation.Screen
import io.hvk.snakegamecompose.ui.screens.GameScreen
import io.hvk.snakegamecompose.ui.screens.MainScreen
import androidx.compose.runtime.*
import io.hvk.snakegamecompose.ui.game.model.GameLevel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnakeGameComposeTheme {
                var currentScreen by remember { mutableStateOf(Screen.MAIN) }
                var selectedGameLevel by remember { mutableStateOf(GameLevel.CLASSIC) }
                
                when (currentScreen) {
                    Screen.MAIN -> MainScreen(
                        onPlayClick = { gameLevel ->
                            selectedGameLevel = gameLevel
                            currentScreen = Screen.GAME
                        }
                    )
                    Screen.GAME -> GameScreen(
                        gameLevel = selectedGameLevel,
                        onBackToMenu = { currentScreen = Screen.MAIN }
                    )
                }
            }
        }
    }
}