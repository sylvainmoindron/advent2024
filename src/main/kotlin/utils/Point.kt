package utils

import kotlin.math.abs


data class Point(val x: Long, val y: Long) {
    operator fun minus(second: Point) = Point(x - second.x, y - second.y)
    operator fun plus(second: Point) = Point(x + second.x, y + second.y)
    operator fun times(i: Long) = Point(x * i, y * i)

    fun manhattanDistanceTo(other: Point) = abs(x - other.x) + abs(y - other.y)

    fun isInbound(innerBound: Point, outerBound: Point) =
        x >= innerBound.x && x <= outerBound.x && y >= innerBound.y && y <= outerBound.y

    fun move(orientation: Orientation): Point =
        when (orientation) {
            Orientation.UP -> copy(y = y - 1)
            Orientation.DOWN -> copy(y = y + 1)
            Orientation.LEFT -> copy(x = x - 1)
            Orientation.RIGHT -> copy(x = x + 1)
        }

    fun adjacentOrthogonal(): List<Point> = listOf(
        Point(x = x, y = y - 1),
        Point(x = x, y = y + 1),
        Point(x = x - 1, y = y),
        Point(x = x + 1, y = y),
    )

    companion object {
        val ORIGIN = Point(0, 0)
    }

}