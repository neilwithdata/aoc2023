import java.io.File

data class Race(val time: Long, val distance: Long) {
    fun numWaysToBeat(): Int {
        var ways = 0

        for (tc in 1 until time) {
            val tg = time - tc
            if (tc * tg > distance)
                ways++
        }

        return ways
    }
}

fun main() {
    val input = File("data/day06_input.txt").readLines()
    val times = input[0].extractNums()
    val distances = input[1].extractNums()

    /* PART 1 */
    val races = convertToRaces(times, distances)

    val result = races.map { race ->
        race.numWaysToBeat()
    }.reduce { acc, i -> acc * i }

    println(result)

    /* PART 2 */
    val race = convertToSingleRace(times, distances)
    println(race.numWaysToBeat())
}

fun convertToRaces(times: List<String>, distances: List<String>): List<Race> {
    return buildList {
        for (i in times.indices) {
            add(Race(times[i].toLong(), distances[i].toLong()))
        }
    }
}

fun convertToSingleRace(times: List<String>, distances: List<String>): Race {
    val time = times.joinToString(separator = "").toLong()
    val distance = distances.joinToString(separator = "").toLong()

    return Race(time, distance)
}

fun String.extractNums(): List<String> {
    return Regex("""(\d+)""").findAll(this).map { it.value }.toList()
}