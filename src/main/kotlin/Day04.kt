import java.io.File
import kotlin.math.pow

data class Card(
    val winningNumbers: Set<Int>,
    val cardNumbers: Set<Int>
) {
    val matchCount = (winningNumbers intersect cardNumbers).count()

    var copies = 1

    fun calculateScore(): Int {
        if (matchCount == 0)
            return 0

        return 2.0.pow((matchCount - 1).toDouble()).toInt()
    }
}

fun main() {
    val cards = File("data/day04_input.txt")
        .readLines()
        .map { line ->
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

            Card(winning, cardNums)
        }

    val part1 = cards.sumOf { it.calculateScore() }
    println(part1)

    for (i in cards.indices) {
        val curr = cards[i]

        cards.subList(i + 1, i + 1 + curr.matchCount)
            .forEach { card ->
                card.copies += curr.copies
            }
    }

    val part2 = cards.sumOf { it.copies }
    println(part2)
}