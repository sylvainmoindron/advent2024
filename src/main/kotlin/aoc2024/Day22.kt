package aoc2024

class Day22 : Day(22) {


    private val input = inputReader(false).readLines().map { it.toLong() }

    private val numberOfNewSecret = 2000

    override fun part01() {
        val resul = input
            .sumOf {
                pseudoSequence(it).take(numberOfNewSecret + 1).last()
            }

        println("sum of sectet $resul")
    }


    override fun part02() {

        val result = buildMap {
            input.map { seed ->
                changeSequence(seed).distinctBy { it.first } // collect all unique chanset for a vendor sequence
                    .forEach { (chg, num) ->
                        this[chg] = (this[chg] ?: 0L) + num
                    }
            }
        }.values.max()

        println("bets sequence = $result")

    }

    data class ChangeSequence(val a: Long, val b: Long, val c: Long, val d: Long)


    private fun pseudoSequence(seed: Long) = generateSequence(seed) {
        var next = (it * 64).mix(it).prune()
        next = (next / 32).mix(next).prune()
        next = (next * 2048).mix(next).prune()
        next
    }

    private fun changeSequence(seed: Long) = pseudoSequence(seed)
        .take(numberOfNewSecret + 1)
        .windowed(5) { five -> five.map { it % 10 } }
        .map {
            val key = ChangeSequence(it[1] - it[0], it[2] - it[1], it[3] - it[2], it[4] - it[3])
            val bananas = it[4]
            key to bananas
        }


    private fun Long.mix(b: Long) = this.xor(b)
    private fun Long.prune() = this % 16777216L

}

fun main() {
    val day = Day22()
    day.solve()
}