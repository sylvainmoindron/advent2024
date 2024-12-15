package utils

enum class Orientation {
    UP, DOWN, LEFT, RIGHT;

    fun turnRight() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
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