package aoc2024

class Day11 : Day(11) {

    val input = inputReader(false).readText().split(" ").map { it.toLong() }


    override fun part01() {
        println("initial = $input")
        cache.clear()
        val sum =input.sumOf {
            countStone(it,0,25)
        }

        println("stone after 25 blinks = $sum")
    }

    override fun part02() {
        println("initial = $input")
        cache.clear()
        val sum =input.sumOf {
            countStone(it,0,75)
        }

        println("stone after 75 blinks = $sum")
    }

    data class clefCache(val stone: Long, val nbItt: Int)

    val cache = mutableMapOf<clefCache, Long>()

   fun countStone(stone: Long, nbItt: Int, limit: Int): Long =
        cache.getOrPut(clefCache(stone, nbItt )) {
            if (nbItt == limit) return@getOrPut 1
            stone.blink().sumOf { countStone(it, nbItt+1, limit) }
        }


    fun Long.blink() = when {
        this == 0L -> listOf(1L)
        this.toString().length % 2 == 0 -> {
            val chaine = this.toString()
            val mid = chaine.length / 2
            listOf(
                chaine.substring(0, mid).toLong(),
                chaine.substring(mid).toLong(),
            )
        }
        else -> listOf(this * 2024)
    }


}


fun main() {
    val day = Day11()
    day.solve()
}