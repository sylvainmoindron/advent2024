package aoc2024

class Day19 : Day(19) {

    private val patterns:List<String>
    private val designs:List<String>
    init {
        val buff= inputReader(false).readLines()
        patterns=buff[0].split(",").map { it.trim() }
        designs=buff.drop(2)
    }


    override fun part01() {

        println(" towels $patterns")
        println(" patterns $designs")
        val possiblesDesigns= designs.count { isPossible(patterns,it) }
        println(" possibles designs $possiblesDesigns")
    }

    override fun part02() {
val possibleArrangements=designs.sumOf { possibleCount(patterns,it, mutableMapOf()) }
        println(" possible arrangements $possibleArrangements")
    }

    private fun isPossible(patterns:List<String>, design:String):Boolean{
        return if(design.isEmpty()) {
            true
        } else {
            patterns.any{pattern -> design.startsWith(pattern) && isPossible(patterns,design.drop(pattern.length))   }
        }
    }


    private fun possibleCount(patterns:List<String>, design:String,cache:MutableMap<String,Long>):Long {
        return cache.getOrPut(design) {
            if(design.isEmpty()) {
                1
            } else {
                patterns.filter { design.startsWith(it) }
                    .sumOf { pattern -> possibleCount(patterns,design.drop(pattern.length), cache) }
            }
        }
    }


}

fun main() {
    val day = Day19()
    day.solve()
}