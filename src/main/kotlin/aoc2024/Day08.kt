package aoc2024


import java.io.File


class Day08 : Day() {

    private val innerBound = Point(0, 0)
    private val outerBound: Point

    private val antennas: List<Antenna>

    init {
        val input =
            File("src/main/resources/day08.txt").readLines()
        outerBound = Point(input.size - 1, input[0].length - 1)
        antennas = input.asSequence()
            .flatMapIndexed { x, line ->
                line.asSequence().mapIndexed { y, c ->
                    if (c.isLetterOrDigit()) {
                        Antenna(Point(x, y), c)
                    } else {
                        null
                    }
                }
            }
            .filterNotNull()
            .toList()
    }


    override fun part01() {


        val antiNodes = antennas.indices.flatMap { first ->
            (first + 1..<antennas.size).flatMap { second -> computeAntinodes(antennas[first], antennas[second]) }
        }
            .filter { it.isInbound(innerBound, outerBound) }
            .toSet()

        println("number of antiNodes ${antiNodes.size}")

    }

    override fun part02() {
        val antiNodes = antennas.indices.flatMap { first ->
            (first + 1..<antennas.size).flatMap { second -> computeAntinodes(antennas[first], antennas[second], true) }
        }
            .filter { it.isInbound(innerBound, outerBound) }
            .toSet()

        println("number of antiNodes ${antiNodes.size}")

    }


    private fun computeAntinodes(a: Antenna, b: Antenna, hasRessonance: Boolean = false): Set<Point> {
        if (a.frequency != b.frequency) return emptySet()

        val diff = b.position - a.position

        val n1 = a.position - diff
        val n2 = b.position + diff

        return if (hasRessonance) {
            generateSequence(b.position) { it + diff }.takeWhile { it.isInbound(innerBound, outerBound) }
                .toSet() + generateSequence(a.position) { it - diff }.takeWhile { it.isInbound(innerBound, outerBound) }
                .toSet()
        } else {
            setOf(n1, n2)
        }
    }


}


data class Antenna(val position: Point, val frequency: Char)

fun main() {
    val day = Day08()
    day.solve()
}
