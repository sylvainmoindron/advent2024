package aoc2024


import java.io.File
import kotlin.time.measureTime

abstract class Day(val day: Int) {
    internal abstract fun part01(): Unit
    internal abstract fun part02(): Unit

    fun inputReader(smaller: Boolean = false) =
        File(
            "src/main/resources/day${day.toString().padStart(2, '0')}${
                if (smaller) {
                    "_small"
                } else {
                    ""
                }
            }.txt"
        ).bufferedReader()


    fun solve() {
        println("\n******* ${this.javaClass.simpleName}    *******")
        println("-----part01-----")
        val time01 = measureTime {
            part01()
        }
        println("time: $time01")

        println("-----part02-----")
        val time02 = measureTime {
            part02()
        }
        println("time: $time02")
    }
}


fun main() {
    val days = listOf(
        Day01(),
        Day02(),
        Day03(),
        Day04(),
        Day05(),
        Day06(),
        Day07(),
        Day08(),
        Day09(),
        Day10(),
        Day11(),
        Day12(),
        Day13(),
        Day14(),
        Day15(),
        Day16(),
        Day17(),
        Day18(),
        Day19(),
        Day20(),
        Day21(),
        Day22(),
        Day23(),
        Day24(),
        Day25(),
    )
    val totalTime = measureTime {
        days.forEach { it.solve() }
    }

    println(" \n -> total time: $totalTime")
}
