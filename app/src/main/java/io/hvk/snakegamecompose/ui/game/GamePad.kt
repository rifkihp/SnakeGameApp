package io.hvk.snakegamecompose.ui.game

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GamePad(
    onDirectionChange: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            DirectionButton(
                direction = Direction.UP,
                icon = Icons.Default.KeyboardArrowUp,
                onDirectionChange = onDirectionChange
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                DirectionButton(
                    direction = Direction.LEFT,
                    icon = Icons.Default.KeyboardArrowLeft,
                    onDirectionChange = onDirectionChange
                )
                
                DirectionButton(
                    direction = Direction.RIGHT,
                    icon = Icons.Default.KeyboardArrowRight,
                    onDirectionChange = onDirectionChange
                )
            }
            
            DirectionButton(
                direction = Direction.DOWN,
                icon = Icons.Default.KeyboardArrowDown,
                onDirectionChange = onDirectionChange
            )
        }
    }
}

@Composable
private fun DirectionButton(
    direction: Direction,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onDirectionChange: (Direction) -> Unit
) {
    IconButton(
        onClick = { onDirectionChange(direction) },
        modifier = Modifier
            .shadow(4.dp, CircleShape)
            .background(Color.White.copy(alpha = 0.2f), CircleShape)
            .size(64.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = direction.name,
            tint = Color.White,
            modifier = Modifier.size(48.dp)
        )
    }
} 