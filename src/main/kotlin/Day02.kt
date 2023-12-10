import java.io.File

data class Game(val id: Int, val rounds: List<Round>) {

    // checks if it would be possible if the bag was loaded with the provided configuration
    // if max from round is <= available for each colour we're all good
    fun isPossible(red: Int, green: Int, blue: Int): Boolean {
        return rounds.maxOf { it.red } <= red &&
                rounds.maxOf { it.green } <= green &&
                rounds.maxOf { it.blue } <= blue
    }

    companion object {
        // Input example(s):
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        fun fromString(input: String): Game {
            // ID
            val idMatch = requireNotNull(Regex("""Game (\d+):""").find(input))
            val id = idMatch.groupValues[1].toInt()

            // Rounds
            val rounds = input
                .substringAfter(':')
                .split(';')
                .map {
                    Round.fromString(it)
                }

            return Game(id, rounds)
        }
    }

    data class Round(
        val red: Int,
        val green: Int,
        val blue: Int
    ) {
        companion object {
            // Input example(s):
            // 8 green, 6 blue, 20 red
            fun fromString(input: String): Round {
                val colorRegex = Regex("""(\d+) (red|green|blue)""")

                var red = 0
                var green = 0
                var blue = 0

                colorRegex
                    .findAll(input)
                    .forEach {
                        val (count, color) = it.destructured

                        when (color) {
                            "red" -> red = count.toInt()
                            "green" -> green = count.toInt()
                            "blue" -> blue = count.toInt()
                        }
                    }

                return Round(red, green, blue)
            }
        }
    }
}

private const val AVAILABLE_RED = 12
private const val AVAILABLE_GREEN = 13
private const val AVAILABLE_BLUE = 14

fun main() {
    val result = File("data/day02_input.txt")
        .readLines()
        .map { line ->
            Game.fromString(line)
        }.filter { game ->
            game.isPossible(
                red = AVAILABLE_RED,
                green = AVAILABLE_GREEN,
                blue = AVAILABLE_BLUE
            )
        }.sumOf {
            it.id
        }

    println(result)
}