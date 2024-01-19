import java.io.File

class Network(private val nodes: Map<String, Pair<String, String>>) {
    private var currentNode = START

    // Returns true when we've reached END
    fun step(instruction: Char): Boolean {
        val nextPair = nodes[currentNode]!!

        currentNode = if (instruction == 'L') {
            nextPair.first
        } else {
            nextPair.second
        }

        return currentNode == END
    }

    companion object {
        private const val START = "AAA"
        private const val END = "ZZZ"
    }
}


fun main() {
    val input = File("data/day08_input.txt")
        .readLines()

    val instructions = input[0]
    val nodes = parseNodes(input.drop(2))

    val network = Network(nodes)

    // Continue repeating the instructions until we reach the end
    var index = 0
    var stepCount = 0
    while (true) {
        val instruction = instructions[index]

        stepCount++
        if (network.step(instruction))
            break

        index = (index + 1) % instructions.length
    }

    println(stepCount)
}

private fun parseNodes(input: List<String>): Map<String, Pair<String, String>> {
    val nodeRegex = Regex("""([A-Z]+) = \(([A-Z]+), ([A-Z]+)\)""")

    return input
        .associate { line ->
            val match = requireNotNull(nodeRegex.matchEntire(line))
            val (key, left, right) = match.destructured

            Pair(key, left to right)
        }
}