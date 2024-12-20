package aoc2024


import utils.Grid
import utils.Point
import utils.Rect
import utils.toGrid
import java.util.*

class Day20 : Day(20) {

    private val smaller=false

    private val origin = Point.ORIGIN
    private val outerBound = if (smaller) Point(14, 14) else Point(140, 140)

    private val maze = inputReader(smaller).toGrid { it }
    private val start = maze.entries.first { it.value== 'S' }.key
    private val end = maze.entries.first { it.value== 'E' }.key

    private val minSave=if(smaller) 50 else 100

    override fun part01() {
        maze.solve( start, end, minSave, 2)
    }

    override fun part02() {
        maze.solve( start, end, minSave, 20)
    }

    private fun Grid<Char>.solve(start: Point, end: Point, minSave: Int, maxCheat: Int) {
        val mazeBound=Rect(origin,outerBound)
        val fromStart = this.flood( start)
        val fromEnd = this.flood( end)
        val normalTime = fromStart.getValue(end)
        var count = 0

        for (entry in this.entries) {
            if (entry.value == '#') continue
            val index = entry.key
            for (yy in -maxCheat..maxCheat) for (xx in -maxCheat..maxCheat) {
                val twin = Point(index.x + xx, index.y + yy)
                if (twin !in mazeBound || this[twin] == '#') continue
                val distance = index.manhattanDistanceTo(twin)
                if (distance == 0L || distance > maxCheat) continue
                val newTime = fromStart.getValue(index) + distance + fromEnd.getValue(twin)
                if (normalTime - newTime >= minSave) count++
            }
        }
        println(count)
    }


    private fun Grid<Char>.flood(start: Point): Map<Point,Int> {

        val map = hashMapOf<Point, Int>()
        val pq = PriorityQueue<Path> { a, b -> a.cost - b.cost }
        pq.offer(Path(start, 0))
        while (pq.isNotEmpty()) {
            val (to, cost) = pq.poll()
            if (to in map) continue
            map.putIfAbsent(to, cost)
            to.adjacentOrthogonal().forEach { next ->
                if (next.isInbound(origin, outerBound) && next !in map && this[next] !='#') pq.offer(Path(next, cost + 1))
            }
        }
        return map
    }
    private data class Path(val to: Point, val cost: Int)


}
fun main() {
    val day = Day20()
    day.solve()
}