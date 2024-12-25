package aoc2024

class Day24 : Day(24) {


    private val lines = inputReader(false).readLines()
    private val logicLines = lines.dropWhile { it != "" }.drop(1)
    private val inputLine = lines.takeWhile { it != "" }


    private val device = Device(inputLine = inputLine, logicLines = logicLines)

    override fun part01() {


        println(device.getOutput())


    }

    override fun part02() {
        // Right now this throws an exception if it detects an inconsistency
        // in the addition logic, which instructs you to change the input to continue.
        val ruleSet = RuleSet(logicLines, device.xInput.size)
        for (i in 0..<ruleSet.numBits) {
            ruleSet.analyze(i)
        }
    }


    sealed interface Gate {
        val value: Boolean
    }

    data class Wire(val name: String, var input: Gate) {}

    data class InputGate(override var value: Boolean) : Gate

    data class AndGate(var a: Wire, var b: Wire) : Gate {
        override val value: Boolean
            get() = a.input.value && b.input.value
    }

    data class OrGate(var a: Wire, var b: Wire) : Gate {
        override val value: Boolean
            get() = a.input.value || b.input.value
    }

    data class XorGate(var a: Wire, var b: Wire) : Gate {
        override val value: Boolean
            get() = a.input.value xor b.input.value
    }

    companion object {
        val logicPattern = Regex("""(\w+) (\w+) (\w+) -> (\w+)""")
    }

    class Device(inputLine: List<String>, logicLines: List<String>) {


        private val wires = buildMap {
            for (definition in inputLine) {
                val (name, value) = definition.split(": ")
                this[name] = Wire(name, InputGate(value == "1"))
            }
        }.toMutableMap()


        val xInput: List<InputGate>
        private val yInput: List<InputGate>
        private var zWires: List<Wire>

        init {
            val (x, y) = wires.entries.sortedBy { it.key }.partition { it.key[0] == 'x' }
            xInput = x.map { it.value.input as InputGate }
            yInput = y.map { it.value.input as InputGate }

            val initialLogic = logicLines.associateBy { it.takeLast(3) }
            for ((name, definition) in initialLogic.entries) {
                addLogic(name, definition, initialLogic)
            }
            zWires = wires.entries.filter { it.key[0] == 'z' }.sortedBy { it.key }.map { it.value }
        }

        private fun addLogic(name: String, definition: String, initialLogic: Map<String, String>): Wire {
            wires[name]?.let { return it } // prevent duplicate Wire creation because of recurtion
            val (a, op, b) = logicPattern.find(definition)!!.destructured
            val aWire = wires[a] ?: addLogic(a, initialLogic.getValue(a), initialLogic)
            val bWire = wires[b] ?: addLogic(b, initialLogic.getValue(b), initialLogic)
            val newWire = when (op) {
                "AND" -> Wire(name, AndGate(aWire, bWire))
                "OR" -> Wire(name, OrGate(aWire, bWire))
                "XOR" -> Wire(name, XorGate(aWire, bWire))
                else -> throw IllegalArgumentException(definition)
            }
            wires[name] = newWire
            return newWire
        }

        fun setInput(x: Long, y: Long) {
            setLong(xInput, x)
            setLong(yInput, y)
        }

        private fun setLong(gates: List<InputGate>, value: Long) {
            for ((i, gate) in gates.withIndex()) {
                gate.value = (value and (1L shl i)) != 0L
            }
        }

        fun getOutput(): Long = zWires
            .mapIndexed { i, it -> if (it.input.value) 1L shl i else 0L }
            .reduce { acc, it -> acc or it }

        fun isCorrect(x: Long, y: Long): Boolean {
            setInput(x, y)
            return getOutput() == x + y
        }
    }

    data class Rule(val input: Set<String>, val op: String, val output: String)
    class RuleSet(logic: List<String>, val numBits: Int) {
        private val rules = logic.map {
            val (a, op, b, c) = logicPattern.find(it)!!.destructured
            Rule(setOf(a, b), op, c)
        }
        private val rulesMap = rules.associateBy { (it.input to it.op) }
        private val finalCarry = Array(numBits) { "" }

        fun analyze(bitIndex: Int) {
            if (bitIndex == 0) {
                if (rulesMap.getValue(setOf("x00", "y00") to "XOR").output != "z00") {
                    throw IllegalArgumentException("Swap z00 into x00 XOR y00")
                }
                println("x00 XOR y00 -> z00")
                finalCarry[0] = rulesMap.getValue(setOf("x00", "y00") to "AND").output
                println("x00 AND y00 -> ${finalCarry[0]}")
            } else {
                val i = if (bitIndex < 10) "0$bitIndex" else "$bitIndex"
                val previousFinalCarry = finalCarry[bitIndex - 1]
                val firstResult = rulesMap.getValue(setOf("x$i", "y$i") to "XOR").output
                println("x$i XOR y$i -> $firstResult")
                val firstCarry = rulesMap.getValue(setOf("x$i", "y$i") to "AND").output
                println("x$i AND y$i -> $firstCarry")
                if (rulesMap.getValue(setOf(firstResult, previousFinalCarry) to "XOR").output != "z$i") {
                    throw IllegalArgumentException("Swap z$i into $firstResult XOR $previousFinalCarry")
                }
                println("$firstResult XOR $previousFinalCarry -> z$i")
                val secondCarry = rulesMap.getValue(setOf(firstResult, previousFinalCarry) to "AND").output
                println("$firstResult AND $previousFinalCarry -> $secondCarry")
                finalCarry[bitIndex] = rulesMap.getValue(setOf(firstCarry, secondCarry) to "OR").output
                println("$firstCarry OR $secondCarry -> ${finalCarry[bitIndex]}")
            }
            println()
        }
    }


}

fun main() {
    val day = Day24()
    day.solve()
}
