package aoc2024

import utils.Point
import utils.toGrid

class Day10 : Day(10) {


    private val mountains =
        inputReader(false).toGrid { it.digitToIntOrNull() }



    override fun part01() {
        val numberOfNine = exploreMountain(true)
        println("reachable summit :$numberOfNine")

    }

    override fun part02() {
        val numberOfNine = exploreMountain(false)
        println("distinct hiking trails :$numberOfNine")
    }

    private fun exploreMountain(countUniqueTrails: Boolean): Int {
        return mountains.entries.filter { it.value == 0 }
            .sumOf { (start, _) ->
                val reachedNines = mutableListOf<Point>()
                visit(-1, start, reachedNines)
                if (countUniqueTrails) reachedNines.toSet().size else reachedNines.size
            }
    }


    private fun visit(
        currentHeight: Int,
        point: Point,
        reachedNines: MutableList<Point>,
    ) {
        val heightAtPoint = mountains[point] ?: return
        if (heightAtPoint == currentHeight + 1) {
            if (heightAtPoint == 9) {
                reachedNines += point
            } else {
                point.adjacentOrthogonal()
                    .filter { mountains.contains(it) }
                    .forEach { adjacent ->
                        visit(heightAtPoint, adjacent, reachedNines)
                    }
            }
        }
    }


}


fun main() {
    val day = Day10()
    day.solve()
}