import java.io.File

class Grid(input: List<String>) {
    private val rows = input.size
    private val cols = input[0].length

    private val data = Array(rows) { row ->
        CharArray(cols) { col ->
            input[row][col]
        }
    }

    operator fun get(row: Int, col: Int): Char? {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return null

        return data[row][col]
    }

    // returns true if any adjacent symbols to the number in the specified location
    private fun isPartNumber(row: Int, cols: IntRange): Boolean {
        var isNearbySymbol = false

        // top left -> bottom right scan
        for (i in (row - 1)..(row + 1)) {
            for (j in (cols.first - 1)..(cols.last + 1)) {
                val c = this[i, j]

                // Some nearby symbol found
                if (c != null && c != '.' && !c.isDigit()) {
                    return true
                }
            }
        }

        return false
    }

    fun findPartNumbers(): List<Int> {
        val partNumbers = mutableListOf<Int>()

        var currNumber = ""

        for (row in 0 until rows) {
            for (col in 0 until cols + 1) {
                val c = this[row, col]

                if (c?.isDigit() == true) {
                    currNumber += c
                } else {
                    if (currNumber.isNotEmpty()) {
                        if (isPartNumber(row, (col - currNumber.length) until col)) {
                            partNumbers += currNumber.toInt()
                        }

                        currNumber = ""
                    }
                }
            }
        }

        return partNumbers
    }

    fun print() {
        for (row in 0 until rows) {
            for (col in 0 until cols) {
                print(data[row][col])
            }
            println()
        }
    }
}

fun main() {
    val input = File("data/day03_input.txt")
        .readLines()

    val grid = Grid(input)
    val result = grid.findPartNumbers().sum()

    println(result)
}