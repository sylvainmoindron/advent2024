package utils

import java.io.BufferedReader

typealias Grid<Tile> = Map<Point, Tile>

fun <Tile> BufferedReader.toGrid(tileMapper: (Char) -> Tile): Grid<Tile> = this.lineSequence()
    .flatMapIndexed { y, line ->
        line.mapIndexed { x, char ->
            Point(x.toLong(), y.toLong()) to tileMapper(char)
        }
    }.toMap()


