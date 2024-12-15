package aoc2024

import utils.*


class Day15 : Day(15) {

    private val grid: Grid<Char>
    private val directions: List<Orientation>
    private val smaller = false
    private val robot: Point

    init {
        grid = inputReader(smaller)
            .lineSequence().filter { it.startsWith("#") }.toGrid {
                if (it != '.') {
                    it
                } else {
                    null
                }
            }.toMutableMap()
            .also { mutableMap ->
                robot = mutableMap.entries.first { it.value == '@' }.key
                mutableMap.remove(robot)
            }

        directions = inputReader(smaller).lineSequence().filter { !it.startsWith("#") }
            .flatMap { line ->
                line.mapNotNull { Orientation.of(it) }
            }
            .toList()

    }


    override fun part01() {
        solve(grid, robot, directions)
    }

    override fun part02() {
        solve(grid.enlarge(), Point(robot.x * 2, robot.y), directions)
    }

    private fun solve(wh: Grid<Char>, robot: Point, orientations: List<Orientation>) {
        val warehouse = wh.toMutableGrid()
        fun move(start: Point, direction: Point): Boolean {
            fun collectDeps(box: Point, map: MutableGrid<Char>) {
                val parts = buildList {
                    add(box)
                    if (warehouse[box] == '[') add(box + Point(1, 0))
                    if (warehouse[box] == ']') add(box + Point(-1, 0))
                }
                val deps = parts.mapNotNull { part -> (part + direction).takeIf { warehouse[it]?.isBox() == true } }
                for (part in parts) map[part] = warehouse.getValue(part)
                for (dep in deps) if (dep !in map) collectDeps(dep, map)
            }

            if (start !in warehouse) return true
            if (warehouse[start] == '#') return false
            val deps = buildMap { collectDeps(start, this) }
            if (deps.keys.any { warehouse[it + direction] == '#' }) {
                return false
            }

            deps.entries.sortedBy { (k, _) -> k.y * direction.y * -1 + k.x * direction.x * -1 }
                .forEach { (part, value) ->
                    warehouse[part + direction] = value
                    warehouse.remove(part)
                }
            return true
        }

        var current = robot
        orientations.forEach { orientation ->
            val diff = when (orientation) {
                Orientation.UP -> Point(0, -1)
                Orientation.DOWN -> Point(0, 1)
                Orientation.LEFT -> Point(-1, 0)
                Orientation.RIGHT -> Point(1, 0)
            }


            val next = current + diff
            if (move(next, diff)) {
                current = next
            }
        }

        val sum = warehouse.entries.filter { it.value.isBox(includeClose = false) }.sumOf { 100 * it.key.y + it.key.x }
        println("sum of GPS $sum")
    }

    private fun Grid<Char>.enlarge() = buildMap {
        for ((k, v) in this@enlarge) {
            put(Point(k.x * 2, k.y), if (v == 'O') '[' else '#')
            put(Point(k.x * 2 + 1, k.y), if (v == 'O') ']' else '#')
        }
    }

    private fun Char.isBox(includeClose: Boolean = true) = this == 'O' || this == '[' || (includeClose && this == ']')

}


fun main() {
    val day = Day15()
    day.solve()
}