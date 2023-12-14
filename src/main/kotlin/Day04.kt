import java.io.File
import kotlin.math.pow

data class Card(
    val winningNumbers: Set<Int>,
    val cardNumbers: Set<Int>
) {

    fun calculateScore(): Int {
        val count = (winningNumbers intersect cardNumbers).count()

        if (count == 0)
            return 0

        return 2.0.pow((count - 1).toDouble()).toInt()
    }
}

fun main() {
    val result = File("data/day04_input.txt")
        .readLines()
        .sumOf { line ->
            val nums = line.substringAfter(':')

            val winning = nums.substringBefore('|')
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toInt() }
                .toSet()

            val cardNums = nums.substringAfter('|')
                .split(' ')
                .filter { it.isNotBlank() }
                .map { it.toInt() }
                .toSet()

            val card = Card(winning, cardNums)
            card.calculateScore()
        }

    println(result)
}