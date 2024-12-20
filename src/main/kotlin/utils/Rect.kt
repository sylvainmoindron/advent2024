package utils

import kotlin.math.max
import kotlin.math.min

class Rect(origin: Point, end: Point) {
    private val xRange = LongRange(min(origin.x, end.x), max(origin.x, end.x))
    private val yRange = LongRange(min(origin.y, end.y), max(origin.y, end.y))

    operator fun contains(other: Point) = other.y in yRange && other.x in xRange
}