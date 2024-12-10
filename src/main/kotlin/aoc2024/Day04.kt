package aoc2024

class Day04 : Day(4) {


    val input =
        inputReader()
            .lineSequence()
            .map { line ->
                line.toCharArray().toList()
            }.toList()

    val height = input.count()
    val witdh = input[0].count()


    override fun part01() {
        var count = 0
        for (i in 0..<witdh) {
            for (j in 0..<height) {
                if (input.startXmas(i, j, -1, -1)) count++
                if (input.startXmas(i, j, -1, -0)) count++
                if (input.startXmas(i, j, -1, 1)) count++
                if (input.startXmas(i, j, 0, -1)) count++
                if (input.startXmas(i, j, 0, 0)) count++
                if (input.startXmas(i, j, 0, 1)) count++
                if (input.startXmas(i, j, 1, -1)) count++
                if (input.startXmas(i, j, 1, 0)) count++
                if (input.startXmas(i, j, 1, 1)) count++
            }
        }

        println("number of XMAS $count")
    }

    private fun List<List<Char>>.startXmas(posX: Int, posY: Int, incX: Int, incY: Int) =
        pickWord(posX, posY, incX, incY) == "XMAS"

    private fun List<List<Char>>.pickWord(posX: Int, posY: Int, incX: Int, incY: Int): String {
        return try {
            "${this[posX][posY]}${this[posX + incX][posY + incY]}${this[posX + incX * 2][posY + incY * 2]}${this[posX + incX * 3][posY + incY * 3]}"
        } catch (e: IndexOutOfBoundsException) {
            ""
        }

    }


    override fun part02() {
        var count = 0
        for (i in 0..<witdh) {
            for (j in 0..<height) {
                if (input.isCenterA(i, j)) count++
            }
        }
        println("number of cross $count")
    }

    private fun List<List<Char>>.isCenterA(posX: Int, posY: Int): Boolean {
        try {
            val diag1 = "${this[posX - 1][posY - 1]}${this[posX][posY]}${this[posX + 1][posY + 1]}"
            val diag2 = "${this[posX - 1][posY + 1]}${this[posX][posY]}${this[posX + 1][posY - 1]}"

            return (diag1 == "MAS" || diag1 == "SAM") && (diag2 == "MAS" || diag2 == "SAM")
        } catch (e: IndexOutOfBoundsException) {
            return false
        }
    }


}


fun main() {
    val day = Day04()
    day.solve()
}