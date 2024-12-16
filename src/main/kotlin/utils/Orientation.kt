package utils

enum class Orientation {
    UP, DOWN, LEFT, RIGHT;

    fun turnRight() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

    fun turnLeft() = when (this) {
        UP -> LEFT
        RIGHT -> UP
        DOWN -> RIGHT
        LEFT -> DOWN
    }


    companion object {
        fun of(char: Char): Orientation =
            when (char) {
                '<' -> LEFT
                '>' -> RIGHT
                'v' -> DOWN
                'V' -> DOWN
                '^' -> UP
                else -> throw IllegalArgumentException("wrong turn $char")
            }

    }

}