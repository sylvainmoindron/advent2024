package aoc2024

import kotlin.math.pow

class Day17 : Day(17) {

    private val initialComputer: Computer

    init {
        var a = 0L
        var b = 0L
        var c = 0L
        var instuction: IntArray = intArrayOf()
        val aRegexp = Regex("""Register A: (-?\d+)""")
        val bRegexp = Regex("""Register B: (-?\d+)""")
        val cRegexp = Regex("""Register C: (-?\d+)""")
        val programRegexp = Regex("""Program: (.+)?""")

        inputReader(false).lineSequence().forEach { line ->
            aRegexp.matchEntire(line)?.apply {
                a = this.groupValues[1].toLong()
            }
            bRegexp.matchEntire(line)?.apply {
                b = this.groupValues[1].toLong()
            }
            cRegexp.matchEntire(line)?.apply {
                c = this.groupValues[1].toLong()
            }
            programRegexp.matchEntire(line)?.apply {
                instuction = this.groupValues[1].split(",").map { it.toInt() }.toIntArray()
            }
        }


        initialComputer = Computer(a, b, c, instuction)

    }


    override fun part01() {
        val toto = initialComputer.copy()
        toto.run()
        toto.printOutput()
    }

    override fun part02() {
        val target = initialComputer.instuction.toMutableList()
        val unvisited = (0L..7L).map { listOf(it) }.toMutableList()
        while (unvisited.isNotEmpty()) {
            val next = unvisited.removeFirst()
            val a = next.reversed().reduceIndexed { index, acc, l -> acc + l * (8.0).pow(index).toLong() }
            val output = initialComputer.runOnce(a)
            val isMatching = output.size == next.size && target.takeLast(output.size) == output
            if (isMatching) {
                if (output == target) {
                    println("a : $a")
                    break
                }
                unvisited += (0L..7L).map { next + listOf(it) }
            }
        }




    }

    fun recursive(target: List<Int>): Long {
        println("called : $target")
        var a = when (target.size) {
            1 -> 0
            else -> recursive(target.drop(1)) shl 3
        }
        println("rnui a $a")
        while (initialComputer.runOnce(a) != target) {
            a++
        }
        println("found $a")
        return a
    }


    fun Computer.runOnce(a: Long) = this.copy(a = a).let {
        it.run()
        it.output
    }

    data class Computer(
        var a: Long,
        var b: Long,
        var c: Long,
        val instuction: IntArray
    ) {
        var pointer: Int = 0
        val output: MutableList<Int> = mutableListOf()

        fun printOutput() {
            println("output ${output.joinToString(",")}")
        }

        fun run() {
            try {
                while (pointer < instuction.size) {
                    val instructionCode = instuction[pointer]
                    val operand = instuction[pointer + 1]

                    val inst = Instruction.getByCode(instructionCode)
                    inst.block.invoke(this, operand)
                    pointer += 2
                }
            } catch (_: Exception) {
            }
        }


        enum class Instruction(val code: Int, val block: Computer.(Int) -> Unit) {
            ADV(0, { operand ->
                a = a shr operand.comboOperand().toInt()
            }),
            BXL(1, { operand ->
                b = b xor operand.toLong()
            }),
            BST(2, { operand ->
                b = operand.comboOperand() % 8L
            }),
            JNZ(3, { operand ->
                if (a != 0L) {
                    pointer = operand - 2  //comme ça on fait toujour le +2 en fin de boucle
                }
            }),
            BXC(4, { _ ->
                b = b.xor(c)
            }),
            OUT(5, { operand ->
                val out = operand.comboOperand() % 8
                //  dirty hack
                if (output.size > instuction.size + 2) kotlin.error("explosion buffer")
                output.add(out.toInt())
            }),
            BDV(6, { operand ->
                b = a shr operand.comboOperand().toInt()
            }),
            CDV(7, { operand ->
                c = a shr operand.comboOperand().toInt()
            });

            companion object {
                private val INSTRUCTION = arrayOf(ADV, BXL, BST, JNZ, BXC, OUT, BDV, CDV)
                fun getByCode(code: Int): Instruction = INSTRUCTION[code]
            }

        }

        private fun Int.comboOperand(): Long =
            when (this) {
                in 0..3 -> this.toLong()
                4 -> a
                5 -> b
                6 -> c
                else -> throw IllegalArgumentException("bad combo$this")
            }


    }


}

fun main() {
    val day = Day17()
    day.solve()
}