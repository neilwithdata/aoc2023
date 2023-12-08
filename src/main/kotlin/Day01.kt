import java.io.File

private val digits = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

fun main() {
//    val matches = digits.values // Part 1
    val matches = digits.keys + digits.values // Part 2

    val result = File("data/day01_input.txt")
        .readLines()
        .sumOf { line ->
            val first = line.findAnyOf(matches)!!.second
            val last = line.findLastAnyOf(matches)!!.second

            val firstDigit = digits[first] ?: first
            val lastDigit = digits[last] ?: last

            "${firstDigit}${lastDigit}".toInt()
        }

    println(result)
}