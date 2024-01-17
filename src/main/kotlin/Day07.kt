import Hand.Type.*
import java.io.File

private const val JOKER = 'J'

data class Hand(
    val cards: String,
    val bid: Int,
    val wildcardJoker: Boolean = false
) : Comparable<Hand> {
    private val type = findStrongestHand(cards)

    private fun findStrongestHand(cards: String): Type {
        if (wildcardJoker && cards.contains(JOKER)) {
            // convert all the jokers to whatever is the most frequent card
            // (this in turn produces the strongest hand)
            val counts = cards
                .groupingBy { it }.eachCount()
                .toMutableMap()

            counts.remove(JOKER)

            val mostFrequent = counts.maxByOrNull { it.value }
            val replacement = mostFrequent?.key ?: 'Q'

            val updatedCards = cards.replace(JOKER, replacement)
            return evaluateType(updatedCards)
        } else {
            return evaluateType(cards)
        }
    }

    private fun evaluateType(cards: String): Type {
        val counts = cards.groupingBy { it }.eachCount()

        return when (counts.size) {
            1 -> FIVE_OF_A_KIND
            2 -> if (counts.any { it.value == 4 }) FOUR_OF_A_KIND else FULL_HOUSE
            3 -> if (counts.any { it.value == 3 }) THREE_OF_A_KIND else TWO_PAIR
            4 -> ONE_PAIR
            5 -> HIGH_CARD
            else -> throw IllegalStateException()
        }
    }

    // zero if equal, positive if > argument, negative if < argument
    override fun compareTo(other: Hand): Int {
        // Same hand
        if (cards == other.cards)
            return 0

        if (type == other.type) {
            // Same type - evaluate on a card-by-card basis
            for (i in 0 until 5) {
                val c = CardLabel.fromChar(cards[i])
                val otherC = CardLabel.fromChar(other.cards[i])

                if (c != otherC) {
                    if (wildcardJoker) {
                        if (c == CardLabel.J) return -1
                        else if (otherC == CardLabel.J) return 1
                    }

                    return c.ordinal - otherC.ordinal
                }
            }

            // Impossible to get to here
            throw IllegalStateException()
        } else {
            return type.ordinal - other.type.ordinal
        }
    }

    enum class Type {
        HIGH_CARD,
        ONE_PAIR,
        TWO_PAIR,
        THREE_OF_A_KIND,
        FULL_HOUSE,
        FOUR_OF_A_KIND,
        FIVE_OF_A_KIND
    }

    enum class CardLabel {
        N2, N3, N4, N5, N6, N7, N8, N9, T, J, Q, K, A;

        companion object {
            fun fromChar(c: Char): CardLabel =
                enumValueOf<CardLabel>(if (c.isDigit()) "N$c" else "$c")
        }
    }
}


fun main() {
    /* Part 1 */
    val hands = readInput()
    val part1Result = hands.sorted().withIndex().sumOf { (i, hand) ->
        (i + 1L) * hand.bid
    }

    println(part1Result)

    /* Part 2 */
    val part2Hands = hands.map { hand -> hand.copy(wildcardJoker = true) }
    val part2Result = part2Hands.sorted().withIndex().sumOf { (i, hand) ->
        (i + 1L) * hand.bid
    }

    println(part2Result)
}

fun readInput(): List<Hand> {
    return File("data/day07_input.txt")
        .readLines()
        .map { line ->
            val (cards, bid) = line.split(' ')
            Hand(cards, bid.toInt())
        }
}