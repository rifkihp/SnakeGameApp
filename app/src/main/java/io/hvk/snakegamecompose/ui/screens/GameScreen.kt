package io.hvk.snakegamecompose.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.hvk.snakegamecompose.ui.game.Direction
import io.hvk.snakegamecompose.ui.game.GameBoard
import io.hvk.snakegamecompose.ui.game.GamePad

@Composable
fun GameScreen(onBackToMenu: () -> Unit) {
    var direction by remember { mutableStateOf(Direction.RIGHT) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1B5E20))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GameBoard(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp)
            )
            
            GamePad(
                onDirectionChange = { newDirection -> direction = newDirection },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
} 