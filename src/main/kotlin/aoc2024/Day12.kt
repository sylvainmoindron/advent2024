package aoc2024

import utils.Point
import utils.toGrid

typealias Zone = Set<Point>

class Day12 : Day(12) {

    val input = inputReader(false).toGrid { it }

    private val zones = input.map { it.key to it.value }.findZones()

    override fun part01() {
        zones.forEachIndexed { ind, it ->
            println("zones $ind : $it perimeter ${it.perimeter()} surface ${it.surface()}")
        }
        val fenPrice = zones.sumOf {
            it.perimeter() * it.surface()
        }
        println("fencePrice $fenPrice")
    }

    override fun part02() {
        val fenPrice = zones.sumOf {
            it.sides() * it.surface()
        }
        println("fencePrice $fenPrice")
    }

    private fun Zone.surface() = this.size

    private fun Zone.perimeter(): Int {
        return this.sumOf { point ->
            point.adjacentOrthogonal().count { !this.contains(it) }
        }
    }

    private fun Zone.sides() = flatMap { o -> o.adjacentOrthogonal().filter { it !in this }.map { it to it - o } }
        .findZones().size

    private fun Set<Point>.regroupByZone(): Set<Zone> {
        return buildSet {
            fun grow(candidate: Point) = buildSet<Point> {
                add(candidate)
                val candidates = LinkedHashSet(this)
                while (candidates.isNotEmpty())
                    for (neighbour in candidates.removeFirst().adjacentOrthogonal())
                        if (neighbour in this@regroupByZone && add(neighbour))
                            candidates.add(neighbour)
            }

            val candidates = this@regroupByZone.toCollection(LinkedHashSet())
            while (candidates.isNotEmpty()) {
                val group = grow(candidates.removeFirst())
                add(group)
                candidates.removeAll(group)
            }
        }
    }

    private fun <T> List<Pair<Point, T>>.findZones() =
        groupBy({ it.second }, { it.first }).values.flatMap { it.toSet().regroupByZone() }


}


fun main() {
    val day = Day12()
    day.solve()
}