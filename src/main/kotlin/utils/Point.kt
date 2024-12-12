package utils


data class Point(val x: Int, val y: Int) {
    operator fun minus(second: Point) = Point(x - second.x, y - second.y)
    operator fun plus(second: Point) = Point(x + second.x, y + second.y)

    fun isInbound(innerBound: Point, outerBound: Point) =
        x >= innerBound.x && x <= outerBound.x && y >= innerBound.y && y <= outerBound.y


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