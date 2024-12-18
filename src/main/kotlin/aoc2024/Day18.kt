package aoc2024

import utils.Grid
import utils.Point
import java.util.*
import kotlin.math.absoluteValue

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
        val result = memorySPace.take(byteFallen).walk()
        println(result)
    }

    override fun part02() {
        //         bonne recherche dychotomique des familles
        val resulindex = (1..memorySPace.size).map { memorySPace.take(it) }
            .binarySearch(fromIndex = byteFallen) {
                if (it.walk() != -1) -1 else 1
            }
    }


    private fun List<Point>.walk(): Int {
        println("walking ${this.size} obstacle")
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
                if (next.isInbound(origin, outerBound) && next !in map && next !in this) pq.offer(Path(next, cost + 1))
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
