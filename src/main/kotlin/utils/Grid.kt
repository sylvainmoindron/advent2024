package utils

import java.io.BufferedReader

typealias Grid<Tile> = Map<Point, Tile>
typealias MutableGrid<Tile> = MutableMap<Point, Tile>

fun <Tile> BufferedReader.toGrid(tileMapper: (Char) -> Tile?): Grid<Tile> = this.lineSequence().toGrid(tileMapper)
fun <Tile> List<String>.toGrid(tileMapper: (Char) -> Tile?): Grid<Tile> = this.asSequence().toGrid(tileMapper)


fun <Tile> Sequence<String>.toGrid(tileMapper: (Char) -> Tile?): Grid<Tile> = this.flatMapIndexed { y, line ->
    line.mapIndexedNotNull { x, char ->
        val value = tileMapper(char)
        if (value != null) {
            Point(x.toLong(), y.toLong()) to value
        } else {
            null
        }
    }
}.toMap()


fun <Tile> Grid<Tile>.toMutableGrid() = this.toMutableMap()

