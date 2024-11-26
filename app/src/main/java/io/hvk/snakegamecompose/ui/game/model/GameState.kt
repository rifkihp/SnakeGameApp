package io.hvk.snakegamecompose.ui.game.model

import io.hvk.snakegamecompose.ui.game.Direction
import kotlin.random.Random

data class GameState(
    val snake: List<Point> = listOf(Point(5, 5)),
    val food: Point = Point(10, 10),
    val direction: Direction = Direction.RIGHT,
    val isGameOver: Boolean = false,
    val score: Int = 0
) {
    companion object {
        const val BOARD_SIZE = 20
    }

    fun move(): GameState {
        if (isGameOver) return this

        val newHead = snake.first() + when (direction) {
            Direction.UP -> Point(0, -1)
            Direction.DOWN -> Point(0, 1)
            Direction.LEFT -> Point(-1, 0)
            Direction.RIGHT -> Point(1, 0)
        }

        // Check for collisions
        if (hasCollision(newHead)) {
            return copy(isGameOver = true)
        }

        // Check if snake ate food
        val newSnake = if (newHead == food) {
            listOf(newHead) + snake
        } else {
            listOf(newHead) + snake.dropLast(1)
        }

        // Generate new food if eaten
        val newFood = if (newHead == food) {
            generateFood(newSnake)
        } else {
            food
        }

        val newScore = if (newHead == food) score + 1 else score

        return copy(
            snake = newSnake,
            food = newFood,
            score = newScore
        )
    }

    private fun hasCollision(point: Point): Boolean {
        return point.x !in 0 until BOARD_SIZE ||
                point.y !in 0 until BOARD_SIZE ||
                point in snake
    }

    private fun generateFood(snake: List<Point>): Point {
        var newFood: Point
        do {
            newFood = Point(
                Random.nextInt(BOARD_SIZE),
                Random.nextInt(BOARD_SIZE)
            )
        } while (newFood in snake)
        return newFood
    }
} 