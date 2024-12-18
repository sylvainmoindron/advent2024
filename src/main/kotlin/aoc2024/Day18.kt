package aoc2024

import utils.Grid
import utils.Point
import java.util.*

class Day18 : Day(18) {

    private val smaller = false

    private val origin = Point.ORIGIN
    private val outerBound = if (smaller) Point(6, 6) else Point(70, 70)
    private val byteFallen = if (smaller) 12 else 1024


    private val memorySPace: List<Point> = inputReader(smaller).lineSequence()
        .map {
            val (x, y) = it.split(",")
            Point(x.toLong(), y.toLong())
        }.toList()


    override fun part01() {
        val result = walk(memorySPace.take(byteFallen).toSet())
        println(result)
    }

    override fun part02() {
        // bonne recherche dychotomique des familles
        var left = byteFallen
        var right = memorySPace.lastIndex
        while (left <= right) {
            val mid = (left + right) / 2
            val walk = walk(memorySPace.take(mid).toSet())
            if (walk == -1) right = mid - 1
            else left = mid + 1
        }
        println(memorySPace[right])
    }

    private fun walk(input: Set<Point>): Int {
        val start = origin
        val end = outerBound

        val map = hashMapOf<Point, Int>()
        val pq = PriorityQueue<Path> { a, b -> a.cost - b.cost }
        pq.offer(Path(start, 0))
        while (pq.isNotEmpty()) {
            val (to, cost) = pq.poll()
            if (to in map) continue
            map.putIfAbsent(to, cost)
            if (to == end) break
            to.adjacentOrthogonal().forEach { next ->
                if (next.isInbound(origin, outerBound) && next !in map && next !in input) pq.offer(Path(next, cost + 1))
            }
        }
        return map.getOrDefault(end, -1)
    }

    private data class Path(val to: Point, val cost: Int)


}

fun main() {
    val day = Day18()
    day.solve()
}
