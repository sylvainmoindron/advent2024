package aoc2024

import utils.Orientation
import utils.Point
import utils.toGrid
import java.util.PriorityQueue

class Day16 : Day(16) {

    val grid = inputReader(false).toGrid { it }
    val start = grid.entries.first { it.value == 'S' }.key
    val end = grid.entries.first { it.value == 'E' }.key


    data class State(
        val point: Point,
        val orientation: Orientation
    )

    data class Path(val state: State, val cost: Int, val previous: List<State>)


    override fun part01() {
        val (cost, _) = bestPlacesCount(false)
        println("cost $cost")
    }

    override fun part02() {
        val (_, number) = bestPlacesCount(true)
        println("bestPlacesCount $number")
    }


    private fun bestPlacesCount(exploreAll: Boolean): Pair<Int, Int> {
        val visited = mutableSetOf<State>()
        val unvisited = PriorityQueue<Path>(compareBy { it.cost })
        unvisited += Path(State(start, Orientation.RIGHT), 0, emptyList())

        val bestPoints = mutableSetOf(end)
        var bestCost = Int.MAX_VALUE

        while (unvisited.isNotEmpty()) {
            val (state, cost, path) = unvisited.remove().also { visited += it.state }
            if (state.point == end) {
                if (cost <= bestCost) {
                    bestCost = cost
                    bestPoints += path.map { it.point }
                } else break
                if (!exploreAll) break
            }

            unvisited += buildList {
                val left = Path(State(state.point, state.orientation.turnLeft()), cost + 1000, path + state)
                if (grid[left.state.point.move(left.state.orientation)] != '#' && left.state !in visited) {
                    // don't add if face wall
                    add(left)
                }
                val right = Path(State(state.point, state.orientation.turnRight()), cost + 1000, path + state)
                if (grid[right.state.point.move(right.state.orientation)] != '#' && right.state !in visited) {
                    // don't add if face wall
                    add(right)
                }
                val front = Path(State(state.point.move(state.orientation), state.orientation), cost + 1, path + state)
                if (grid[front.state.point] != '#' && front.state !in visited ) {
                    // don't add if it goes into wall
                    add(front)
                }
            }


        }
        return Pair(bestCost, bestPoints.size)
    }


}

fun main() {
    val day = Day16()
    day.solve()
}
