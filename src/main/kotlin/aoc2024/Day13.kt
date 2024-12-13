package aoc2024

import utils.Point

class Day13 : Day(13) {


    val btnRegex = Regex("Button .: X\\+(\\d+), Y\\+(\\d+)")
    val priceRegex = Regex("Prize: X=(\\d+), Y=(\\d+)")


    private fun buildMachines(priceOffset: Long = 0) = inputReader(false).lineSequence().chunked(4) { fourLines ->
        if (fourLines.size < 3) {
            throw IllegalArgumentException("Invalid machine input")
        } else {
            Machine(
                a = btnRegex.matchEntire(fourLines[0])
                    ?.let { Point(it.groupValues[1].toLong(), it.groupValues[2].toLong()) }
                    ?: throw IllegalArgumentException("Invalid machine input"),
                b = btnRegex.matchEntire(fourLines[1])
                    ?.let { Point(it.groupValues[1].toLong(), it.groupValues[2].toLong()) }
                    ?: throw IllegalArgumentException("Invalid machine input"),
                prize = priceRegex.matchEntire(fourLines[2])
                    ?.let { Point(it.groupValues[1].toLong() + priceOffset, it.groupValues[2].toLong() + priceOffset) }
                    ?: throw IllegalArgumentException("Invalid machine input"),
            )
        }
    }.toList()


    override fun part01() {
        val summToWin = buildMachines().sumOf { it.bestPrice() }

        println("sum To win $summToWin")
    }

    override fun part02() {
        val summToWin = buildMachines(10000000000000L).sumOf { it.bestPrice() }

        println("sum To win $summToWin")
    }


    data class Machine(
        val a: Point,
        val b: Point,
        val prize: Point
    ) {

        fun bestPrice(): Long {
            val db = (a.x * prize.y - a.y * prize.x) / (a.x * b.y - a.y * b.x)
            val da = (prize.y - b.y * db) / a.y
            return if (a * da + b * db == prize) da * 3 + db else 0
        }
    }

}


fun main() {
    val day = Day13()
    day.solve()
}
