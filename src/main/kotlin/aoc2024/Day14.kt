package aoc2024

import utils.Point

class Day14 : Day(14) {

    val small = false
    val uppberBound: Point

    val robotRegex = Regex("""p=(\d+),(\d+) v=(-?\d+),(-?\d+)""")

    init {
        if (small) {
            uppberBound = Point(11, 7)

        } else {
            uppberBound = Point(101, 103)
        }
    }

    private fun loadRobots() = inputReader(small).lineSequence()
        .map {
            val values = robotRegex.matchEntire(it)?.groupValues ?: throw IllegalArgumentException("bad Robot $it")
            Robot(
                Point(values[1].toLong(), values[2].toLong()),
                Point(values[3].toLong(), values[4].toLong()),
                uppberBound
            )
        }.toList()


    override fun part01() {
        val robots = loadRobots()
        repeat(100) {
            robots.forEach { it.move() }
        }

        val quadrantOccupied = robots.groupBy { it.quadrant }
        val factor = (1..4).map { quadrantOccupied[it]?.size ?: 0 }.fold(1) { acc: Int, i: Int -> acc * i }

        println("factor $factor")
    }

    override fun part02() {
        val robots = loadRobots()
        var elapsedTime = 0
        while (robots.hasOverlap()) {
            robots.forEach { it.move() }
            elapsedTime++
        }
        println("easter in $elapsedTime seconds")

    }

    private fun List<Robot>.hasOverlap(): Boolean {
        val uniquePosition = this.asSequence().map { it.position }.toSet()
        return uniquePosition.size != this.size
    }


    data class Robot(var position: Point, val velocity: Point, val uppberBound: Point) {
        val quadrant: Int
            get() =
                when {
                    position.x < (uppberBound.x / 2) && position.y < (uppberBound.y / 2) -> 1
                    position.x > (uppberBound.x / 2) && position.y < (uppberBound.y / 2) -> 2
                    position.x < (uppberBound.x / 2) && position.y > (uppberBound.y / 2) -> 3
                    position.x > (uppberBound.x / 2) && position.y > (uppberBound.y / 2) -> 4
                    else -> 0
                }

        fun move() {
            val increment = position + velocity
            val warped =
                Point((increment.x + uppberBound.x) % uppberBound.x, (increment.y + uppberBound.y) % uppberBound.y)
            position = warped
        }
    }


}

fun main() {
    val day = Day14()
    day.solve()
}
