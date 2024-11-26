package io.hvk.snakegamecompose.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction

@Composable
fun MenuButton(
    text: String,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    
    Box(
        modifier = Modifier
            .shadow(
                elevation = if (isPressed) 8.dp else 16.dp,
                shape = RoundedCornerShape(28.dp),
                spotColor = Color(0xFF00FF00).copy(alpha = 0.5f)
            )
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .width(250.dp)
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF002000)
            ),
            shape = RoundedCornerShape(28.dp),
            border = ButtonDefaults.outlinedButtonBorder.copy(
                width = 2.dp,
                brush = Brush.linearGradient(
                    listOf(
                        Color(0xFF00FF00).copy(alpha = 0.7f),
                        Color(0xFF00FF00).copy(alpha = 0.3f)
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
                color = Color(0xFF00FF00),
                fontWeight = FontWeight.Bold
            )
        }
    }
} 