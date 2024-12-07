package aoc2024

import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import java.io.File

class Day07 : Day() {


    val input =
        File("src/main/resources/day07.txt").bufferedReader().lineSequence()
            .map { line ->
                val (result, numbers) = line.split(":")
                Equation(result.toLong(), numbers.split(" ").filter { it.isNotBlank() }.map { it.toLong() })
            }
            .toList()


    override fun part01() {
        runBlocking {
            val sumPossible = input.asFlow()
                .parallelMap {
                    if (it.isFeasibleWith(listOf(Operator.ADD, Operator.MULTIPLY))) {
                        it.result
                    } else {
                        0
                    }
                }.toList()
                .sum()


            println(" sumPossible $sumPossible")
        }
    }

    override fun part02() {
        runBlocking {
            val sumPossible = input.asFlow()
                .parallelMap {
                    if (it.isFeasibleWith(listOf(Operator.ADD, Operator.MULTIPLY, Operator.CONCATENATE))) {
                        it.result
                    } else {
                        0
                    }
                }.toList()
                .sum()


            println(" sumPossible $sumPossible")
        }
    }

}

enum class Operator(private val operation: (Long, Long) -> Long) {
    ADD({ a: Long, b: Long -> a + b }),
    MULTIPLY({ a: Long, b: Long -> a * b }),
    CONCATENATE({ a: Long, b: Long -> "$a$b".toLong() })
    ;

    fun operate(a: Long, b: Long) = operation.invoke(a, b)
}

data class Equation(val result: Long, val numbers: List<Long>) {


    fun isFeasibleWith(operators: List<Operator>) = computeAllPossibilities(numbers, operators).contains(result)


    fun computeAllPossibilities(numbers: List<Long>, operators: List<Operator>): List<Long> {
        if (numbers.size == 1) {
            return listOf(numbers[0])
        }
        val partialResults = operators.map { it.operate(numbers[0], numbers[1]) }

        if (numbers.size == 2) {
            return partialResults
        }

        return partialResults.flatMap { partialResul ->
            val acc = listOf(partialResul) + numbers.drop(2)
            computeAllPossibilities(acc, operators)
        }

    }

}


fun main() {
    val day = Day07()
    day.solve()
}
