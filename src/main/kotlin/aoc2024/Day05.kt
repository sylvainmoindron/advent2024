package aoc2024


class Day05 : Day(5) {

    private val input =
        inputReader().readLines()


    private val ruleRegex = """(\d+)\|(\d+)""".toRegex()

    private val rules = input.asSequence()
        .mapNotNull { ruleRegex.matchEntire(it) }
        .map {
            val (before, after) = it.destructured
            Rule(before.toInt(), after.toInt())
        }.toList()

    private val updateRegex = """^\d+(,\d+)*$""".toRegex()

    private val updates = input.asSequence()
        .mapNotNull { updateRegex.matchEntire(it) }
        .map { match ->
            Update(match.value.split(",").map { it.toInt() })
        }.toList()


    override fun part01() {
        val sum = updates.asSequence().filter { it.satisfiesRules(rules) }.sumOf { it.middlePage() }
        println("sum of middle pages $sum")

    }

    override fun part02() {
        val sum = updates.filter { !it.satisfiesRules(rules) }
            .map { it.order(rules) }.sumOf { it.middlePage() }
        println("sum of middle ordered pages $sum")


    }
}

data class Rule(val before: Int, val after: Int) {
    fun isInvolved(pagea: Int, pageB: Int): Boolean {
        return (before == pagea && after == pageB) || (before == pageB && after == pagea)
    }
}


class RulesComparator(private val rules: List<Rule>) : Comparator<Int> {
    override fun compare(left: Int, right: Int): Int {
        val rule = rules.firstOrNull { it.isInvolved(left, right) }
        if (rule == null) return 0
        if (rule.before == left) return -1
        return 1
    }

}


data class Update(val pages: List<Int>) {

    private fun isInvolved(rule: Rule): Boolean {
        val indiceBefore = pages.indexOf(rule.before)
        val indiceAfter = pages.indexOf(rule.after)
        return (indiceBefore != -1 && indiceAfter != -1)
    }

    private fun subsetInvolvedRules(rules: List<Rule>) = rules.filter { isInvolved(it) }


    fun order(rules: List<Rule>): Update {

        val subSet = subsetInvolvedRules(rules)
        val comparator = RulesComparator(subSet)
        val new = pages.sortedWith(comparator)
        return Update(new)

    }


    private fun satisfiesRule(rule: Rule): Boolean {
        val indiceBefore = pages.indexOf(rule.before)
        val indiceAfter = pages.indexOf(rule.after)
        if (indiceBefore == -1 || indiceAfter == -1) return true
        return indiceBefore < indiceAfter
    }

    fun satisfiesRules(rules: List<Rule>) = rules.all { satisfiesRule(it) }


    fun middlePage() =
        pages[pages.count() / 2]
}


fun main() {
    val day = Day05()
    day.solve()
}