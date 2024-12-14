package aoc2024

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.runBlocking
import utils.Point
import utils.parallelMap

class Day06 : Day(6) {


    private val outerBound: Point

    //    private val obstacleMap: List<List<Boolean>>
    private val obstacles: Set<Point>
    private lateinit var originalPlace: Point
    private lateinit var originalOrientation: Orientation

    init {
        val input =
            inputReader().readLines()

        obstacles = input
            .flatMapIndexed { indexX, ligne ->
                ligne.toCharArray().mapIndexed { indexY, it ->
                    if (it == '#') {
                        Point(indexX.toLong(), indexY.toLong())
                    } else {
                        null
                    }
                }.filterNotNull()
            }.toSet()

        outerBound = Point(input.size - 1L, input[0].length - 1L)

        input.forEachIndexed { indexX, ligne ->
            ligne.forEachIndexed { indexY, char ->
                if (char == '^') {
                    originalPlace = Point(indexX.toLong(), indexY.toLong())
                    originalOrientation = Orientation.UP
                }
            }
        }


    }

    private lateinit var placeBeenSol01: MutableSet<Point>

    override fun part01() {
        val gard = Gard(originalPlace, originalOrientation, obstacles, outerBound)
        try {
            while (true) {
                gard.advanceOrTurn()
            }
        } catch (e: ExitingExeption) {
            println("Exiting the map")
        }

        placeBeenSol01 = gard.placeBeen

        println("Number of places visited ${placeBeenSol01.size}")

    }

    override fun part02() {

        val numberOfSafableRoute =
            runBlocking {
                placeBeenSol01
                    .asFlow()
                    .filter { it != originalPlace }
                    .parallelMap(Dispatchers.Default) {
                            val newObstacles = setOf(it) + obstacles
                            val gard = Gard(originalPlace, originalOrientation, newObstacles, outerBound)
                            try {
                                while (true) {
                                    gard.advanceOrTurn()
                                }
                            } catch (e: ExitingExeption) {
                                null
                            } catch (e: LoopExeption) {
                                it
                            }

                    }
                    .count()
            }

        println("number of safable route $numberOfSafableRoute")
    }
}

enum class Orientation {
    UP, DOWN, LEFT, RIGHT;

    fun turnRight() = when (this) {
        UP -> RIGHT
        RIGHT -> DOWN
        DOWN -> LEFT
        LEFT -> UP
    }

}


class ExitingExeption : Exception()
class LoopExeption : Exception()

data class Position(val place: Point, val orientation: Orientation)

class Gard(
    var place: Point,
    var orientation: Orientation,
    val obstacles: Set<Point>,
    val outerBound: Point
) {
    val placeBeen = mutableSetOf(place)
    private val positionBeen = mutableSetOf(Position(place, orientation))


    fun advanceOrTurn() {
        val inFront = inFront()
        if (inFront.x < 0 || inFront.x > outerBound.x || inFront.y < 0 || inFront.y > outerBound.y) {
            // go outside Map end of patrol
            throw ExitingExeption()
        }
        if (obstacles.contains(inFront)) {
            orientation = orientation.turnRight()
        } else {
            if (isEnteringLoop(inFront)) {
                throw LoopExeption()
            }
            place = inFront
            placeBeen.add(place)
            positionBeen.add(Position(inFront, orientation))
        }
    }

    private fun isEnteringLoop(inFront: Point): Boolean {
        return positionBeen.contains(Position(inFront, orientation))

    }


    private fun inFront(): Point {
        return when (orientation) {
            Orientation.UP -> Point(place.x - 1, place.y)
            Orientation.DOWN -> Point(place.x + 1, place.y)
            Orientation.LEFT -> Point(place.x, place.y - 1)
            Orientation.RIGHT -> Point(place.x, place.y + 1)
        }
    }

}


fun main() {
    val day = Day06()
    day.solve()
}