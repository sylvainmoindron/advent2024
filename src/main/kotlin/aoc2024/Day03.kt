package aoc2024

class Day03 : Day(3) {


    private val input = inputReader().readText()


    override fun part01() {
        val sum = Regex("mul\\((\\d+),(\\d+)\\)").findAll(input).map {
            it.groupValues[1].toInt() * it.groupValues[2].toInt()
        }.sum()
        println("sum= $sum")
    }

    override fun part02() {
        var enabled = true


        val sum=Regex("mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)").findAll(input).map {
            if(it.value == "do()") {
                enabled = true
                    0
            } else if(it.value == "don't()") {
                enabled = false
                0
            } else if(enabled) {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            } else {
                0
            }
        }.sum()

        println("sumEnabler= $sum")


    }
}

fun main() {
    val day = Day03()
    day.solve()
}