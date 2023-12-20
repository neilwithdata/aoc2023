import java.io.File
import java.util.*

data class CategoryMapping(
    val from: String,
    val to: String,
    val ranges: List<RangeMapping>
) {
    fun map(source: Long): Long {
        for (range in ranges) {
            if (source in range) {
                return range.map(source)
            }
        }

        // Unmapped
        return source
    }
}

data class RangeMapping(
    val sourceStart: Long,
    val destStart: Long,
    val range: Long
) {
    operator fun contains(source: Long): Boolean {
        return (source - sourceStart) in 0 until range
    }

    fun map(source: Long): Long {
        return destStart + (source - sourceStart)
    }
}

fun main() {
    val scanner = Scanner(File("data/day05_input.txt"))

    /* INPUT PARSING */
    val seeds = parseSeeds(scanner)
    val seedRanges = buildList {
        for (i in seeds.indices step 2) {
            add(seeds[i] to seeds[i + 1])
        }
    }

    // Advance to category mappings
    scanner.nextLine()

    val categories = mutableListOf<CategoryMapping>()
    while (scanner.hasNextLine()) {
        categories += parseCategoryMapping(scanner)
    }

    /* CALCULATION */
    var lowestLocation = Long.MAX_VALUE
    for ((start, range) in seedRanges) {
        for (seed in (start until start + range)) {
            // Step through each category mapping until we get to the end 'location'
            var result = seed
            for (category in categories) {
                result = category.map(result)
            }

            if (result < lowestLocation) {
                lowestLocation = result
            }
        }
    }

    println("Lowest location is $lowestLocation")
}

fun parseSeeds(scanner: Scanner): List<Long> {
    return scanner.nextLine()
        .substringAfter(':')
        .split(' ')
        .filter { it.isNotBlank() }
        .map { it.trim().toLong() }
}

fun parseCategoryMapping(scanner: Scanner): CategoryMapping {
    val titleRegex = Regex("""([a-z]+)-to-([a-z]+)""")

    val titleLine = scanner.nextLine()
    val ranges = mutableListOf<RangeMapping>()

    while (true) {
        var line: String? = null
        if (scanner.hasNextLine()) {
            line = scanner.nextLine()
        }

        if (line.isNullOrBlank()) {
            val (from, to) = titleRegex.find(titleLine)!!.destructured
            return CategoryMapping(from, to, ranges)
        } else {
            val (dest, source, range) = line.split(' ').map { it.toLong() }
            ranges += RangeMapping(source, dest, range)
        }
    }
}