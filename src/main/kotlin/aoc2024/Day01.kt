package aoc2024

import aoc2024.Day
import java.io.File

class Day01 :Day() {

    private val list01: List<Int>
    private val list02: List<Int>

    init {


        val input =
            File("src/main/resources/day01.txt").bufferedReader()
                .lineSequence()
                .map { line ->
                    val splited = line.split("   ")
                    arrayOf(
                        splited[0].toInt(), splited[1].toInt()
                    )
                }.toList()

        list01 = input.map { it[0] }
        list02 = input.map { it[1] }

    }

    override fun part01() {
        val list1Sorted = list01.sorted()
        val list2Sorted = list02.sorted()

        val sumOfDistance = list1Sorted.zip(list2Sorted).map { Math.abs(it.first - it.second) }.sum()
        println(" sumOfDistance= $sumOfDistance")
    }


    override fun part02() {
        val countList02 = mutableMapOf<Int,Int>()
        list02.asSequence().forEach {
            countList02[it] = 1 + (countList02[it]?:0)
        }

       val similarityScore= list01.asSequence().map {
            it*(countList02[it]?:0)
        }.sum()
        println ("similarityScore = $similarityScore")
    }
}

fun main() {
    val day = Day01()
    day.solve()
}