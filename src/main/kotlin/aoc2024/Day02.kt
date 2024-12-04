import aoc2024.Day
import java.io.File
import kotlin.math.abs


class Day02 : Day() {


    val input =
        File("src/main/resources/day02.txt").bufferedReader()
            .lineSequence()
            .map { line ->
                line.split(" ").map { it.toInt() }
            }.toList()


    override fun part01() {
       val numberSafeReport= input.count { it.isSafe() }
        println("number of safe report= $numberSafeReport")
    }

    override fun part02() {
        val numberSafeReport= input.count { it.isSafe() || it.isSafeDampened() }
        println("number of safe report dampened= $numberSafeReport")
    }


    private fun List<Int>.isSafe() : Boolean {
        return this.isOrdered() && this.isSafeRange()
    }

    private fun List<Int>.isOrdered() : Boolean {
        return this.sorted() == this || this.sortedDescending() == this
    }

    private fun List<Int>.isSafeRange() : Boolean {
        return this.asSequence().windowed(2).all {
            val diff = abs(it[0] - it[1])
            diff in 1..3
        }
    }

    private fun List<Int>.isSafeDampened() : Boolean {
        for  (removed in indices) {
            val removedList = this.toMutableList()
            removedList.removeAt(removed)
            if (removedList.isOrdered() && removedList.isSafeRange()) {
                return true
            }
        }
        return false
    }



}


fun main() {
    val day = Day02()
    day.solve()
}