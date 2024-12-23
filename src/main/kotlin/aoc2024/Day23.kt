package aoc2024

class Day23 : Day(23) {
    private val input = inputReader(false).lineSequence().map {
        val splited = it.split("-")
        splited[0] to splited[1]
    }.toList()

    private val neighbours = buildMap<String, Set<String>> {
        input.forEach {
            this[it.first] = (this[it.first] ?: emptySet()) + it.second
            this[it.second] = (this[it.second] ?: emptySet()) + it.first
        }
    }

    private val computers = neighbours.keys

    override fun part01() {

        val triplets = buildSet {
            input.asSequence().forEach { pair ->
                neighbours[pair.first]?.intersect(neighbours[pair.second] ?: emptySet())?.forEach {
                    add(setOf(pair.first, pair.second, it))
                }
            }
        }

        val result = triplets.count { triplet ->
            triplet.any { it.startsWith(("t")) }
        }
        println("triplet containing T $result ")
    }

    override fun part02() {
        val networks = buildSet {
            computers.forEach { comp ->
                add(
                    buildSet {
                        add(comp)
                        neighbours[comp]?.forEach { candidate ->
                            if (neighbours[candidate]?.containsAll(this) == true) add(candidate)
                        }
                    }
                )
            }
        }

        val bigger = networks.maxBy { it.size }

        println("bigger networks $bigger")

        println("password ${bigger.sortedBy { it }.joinToString(",")}")

    }

}


fun main() {
    val day = Day23()
    day.solve()
}