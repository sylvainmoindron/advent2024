package aoc2024

import java.io.File

class Day09 : Day() {

    private val originalDisk: List<Int>

    init {
        val input = File("src/main/resources/day09.txt").readText()
        originalDisk =
            sequence {
                input
                    .asSequence()
                    .map { it.digitToInt() }
                    .forEachIndexed { index: Int, size: Int ->
                        if (index % 2 == 0) {
                            val fileId = index / 2
                            repeat(size) { _ -> yield(fileId) }
                        } else {
                            repeat(size) { _ -> yield(-1) }
                        }
                    }
            }
                .toList()
    }

    override fun part01() {

        val workingDisk = originalDisk.toMutableList()

        var readingIndex = workingDisk.nextUsedBlock(workingDisk.lastIndex)
        var freespacePointer = workingDisk.nextFreeSpace(0)

        while (readingIndex > freespacePointer) {
            workingDisk.swap(readingIndex, freespacePointer)
            freespacePointer = workingDisk.nextFreeSpace(0)
            readingIndex = workingDisk.nextUsedBlock(readingIndex)
        }


        val checksum: Long = workingDisk.diskChecksum()
        println("checksum $checksum")
    }

    override fun part02() {
        val workingDisk = originalDisk.toMutableList()

        val lastId = workingDisk[workingDisk.indexOfLast { it != -1 }]

        for (idCounter in lastId downTo 0) {

            val file = workingDisk.findFileById(idCounter)
            val space = workingDisk.findSuitableSpace(file.size(), file.first)

            if (space != null) {
                for (ind in 0..<file.size()) {
                    workingDisk.swap(file.first + ind, space.first + ind)
                }
            }
        }

        val checksum: Long = workingDisk.diskChecksum()
        println("checksum $checksum")
    }

    private fun List<Int>.diskChecksum() = this.asSequence()
        .mapIndexed { index, value ->
            if (value == -1) 0 else (value * index).toLong()
        }.sum()


    private fun MutableList<Int>.swap(a: Int, b: Int) {
        val temp = this[a]
        this[a] = this[b]
        this[b] = temp
    }


    private fun Pair<Int, Int>.size() = this.second - this.first + 1


    private fun List<Int>.findFileById(id: Int): Pair<Int, Int> {
        val begin = this.indexOfFirst { it == id }
        var end = begin
        while (end < this.lastIndex && this[end + 1] == id) {
            end++
        }
        return Pair(begin, end)
    }

    private fun List<Int>.findSuitableSpace(size: Int, before: Int): Pair<Int, Int>? {
        var freespacePointer = 0
        return generateSequence {
            try {
                val pointer = nextFreeSpaces(freespacePointer)
                freespacePointer=pointer.second+1
                if(pointer.first >= before) return@generateSequence null
                pointer
            } catch (e: IndexOutOfBoundsException) {
                null
            }
        }.firstOrNull {
            it.size() >= size
        }
    }

    private fun List<Int>.nextFreeSpaces(from: Int): Pair<Int, Int> {
        var counter = from
        while (this[counter] != -1) {
            counter++
        }
        val begin = counter
        while (this[counter] == -1) {
            counter++
        }
        val end = counter - 1

        return Pair(begin, end)
    }


    private fun List<Int>.nextFreeSpace(from: Int): Int {
        var counter = from
        while (this[counter] != -1) {
            counter++
        }
        return counter
    }


    private fun List<Int>.nextUsedBlock(from: Int): Int {
        var counter = from
        while (this[counter] == -1) {
            counter--
        }
        return counter
    }


}

fun main() {
    val day = Day09()
    day.solve()
}
