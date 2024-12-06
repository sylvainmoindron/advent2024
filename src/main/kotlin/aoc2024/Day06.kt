package aoc2024

import java.io.File

class Day06 : Day() {


    private val outerBound: Place

    //    private val obstacleMap: List<List<Boolean>>
    private val obstacles: Set<Place>
    private lateinit var originalPlace: Place
    private lateinit var originalOrientation: Orientation

    init {
        val input =
            File("src/main/resources/day06.txt").readLines()

        obstacles = input
            .flatMapIndexed { indexX, ligne ->
                ligne.toCharArray().mapIndexed { indexY, it ->
                    if (it == '#') {
                        Place(indexX, indexY)
                    } else {
                        null
                    }
                }.filterNotNull()
            }.toSet()

        outerBound = Place(input.size - 1, input[0].length - 1)

        input.forEachIndexed { indexX, ligne ->
            ligne.forEachIndexed { indexY, char ->
                if (char == '^') {
                    originalPlace = Place(indexX, indexY)
                    originalOrientation = Orientation.UP
                }
            }
        }


    }

    override fun part01() {
        val gard = Gard(originalPlace, originalOrientation, obstacles, outerBound)
        try {
            while (true) {
                gard.advanceOrTurn()
            }
        } catch (e: ExitingExeption) {
            println("Exiting the map")
        }

        println("Number of places visited ${gard.placeBeen.size}")

    }

    override fun part02() {

        val numberOfSafableRoute =

            (0..outerBound.x).sumOf { indexX ->
                (0..outerBound.y).map { indexY ->
                    val place = Place(indexX, indexY)
                    if (obstacles.contains(place)) {
                        0
                    } else {
                        if (place == originalPlace) {
                            0
                        } else {
                            val newObstacles = setOf(place) + obstacles
                            val gard = Gard(originalPlace,originalOrientation,newObstacles , outerBound)
                            try {
                                while (true) {
                                    gard.advanceOrTurn()
                                }
                                0
                            } catch (e: ExitingExeption) {
                                0
                            } catch (e: LoopExeption) {
                                1
                            }
                        }
                    }
                }.sum()
            }

        println("number of safable route $numberOfSafableRoute")
    }
}

enum class Orientation {
    UP, DOWN, LEFT, RIGHT
}


class ExitingExeption : Exception()
class LoopExeption : Exception()

data class Position(val place: Place, val orientation: Orientation)

class Gard(
    var place: Place,
    var orientation: Orientation,
    val obstacles: Set<Place>,
    val outerBound: Place
) {
    val placeBeen = mutableSetOf(place)
    val positionBeen = mutableSetOf(Position(place, orientation))


    fun advanceOrTurn() {
        val inFront = infront()
        if (inFront.x < 0 || inFront.x > outerBound.x || inFront.y < 0 || inFront.y > outerBound.y) {
            // go outside Map end of patrol
            throw ExitingExeption()
        }
        if (obstacles.contains(inFront)) {
            turnRight()
        } else {
            if (isenteringLoop(inFront)) {
                throw LoopExeption()
            }
            place = inFront
            placeBeen.add(place)
            positionBeen.add(Position(inFront, orientation))
        }
    }

    fun isenteringLoop(inFront: Place): Boolean {
        return positionBeen.contains(Position(inFront, orientation))

    }


    fun turnRight() {
        orientation = when (orientation) {
            Orientation.UP -> Orientation.RIGHT
            Orientation.DOWN -> Orientation.LEFT
            Orientation.LEFT -> Orientation.UP
            Orientation.RIGHT -> Orientation.DOWN
        }
    }


    fun infront(): Place {
        return when (orientation) {
            Orientation.UP -> Place(place.x - 1, place.y)
            Orientation.DOWN -> Place(place.x + 1, place.y)
            Orientation.LEFT -> Place(place.x, place.y - 1)
            Orientation.RIGHT -> Place(place.x, place.y + 1)
        }
    }

}

data class Place(val x: Int, val y: Int)

fun main() {
    val day = Day06()
    day.solve()
}