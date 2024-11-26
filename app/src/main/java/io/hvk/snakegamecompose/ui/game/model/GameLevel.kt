package io.hvk.snakegamecompose.ui.game.model

enum class GameLevel(val title: String, val description: String) {
    CLASSIC("Classic", "The original snake game experience"),
    ARCADE("Arcade", "Special power-ups and obstacles"),
    SPEED_RACE("Speed Race", "Increasingly faster gameplay"),
    TIME_ATTACK("Against Time", "Race against the clock")
} 