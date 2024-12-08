package aoc2024


data class Point(val x: Int, val y: Int) {
    operator fun minus(second: Point) = Point(x - second.x, y - second.y)
    operator fun plus(second: Point) = Point(x + second.x, y + second.y)

    fun isInbound(innerBound: Point, outerBound: Point) =
        x >= innerBound.x && x <= outerBound.x && y >= innerBound.y && y <= outerBound.y


}