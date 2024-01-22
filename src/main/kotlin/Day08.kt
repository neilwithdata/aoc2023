import java.io.File

class Network(
    private val nodes: Map<String, Pair<String, String>>,
    startNode: String = "AAA",
    private val endCondition: (String) -> Boolean
) {
    private var currentNode = startNode

    fun findCycleLength(instructions: String): Int {
        var index = 0
        var stepCount = 0

        var prevStepCount: Int? = null

        while (true) {
            val instruction = instructions[index]

            stepCount++
            val result = step(instruction)

            if (result) {
                if (prevStepCount == null) {
                    prevStepCount = stepCount
                } else {
                    return stepCount - prevStepCount
                }
            }

            index = (index + 1) % instructions.length
        }
    }

    // Returns true when we've reached END
    fun step(instruction: Char): Boolean {
        val nextPair = nodes[currentNode]!!

        currentNode = if (instruction == 'L') {
            nextPair.first
        } else {
            nextPair.second
        }

        return endCondition(currentNode)
    }
}

fun main() {
    val input = File("data/day08_input.txt")
        .readLines()

    val instructions = input[0]
    val nodes = parseNodes(input.drop(2))

    // Part 1
    val network = Network(nodes, "AAA") { node ->
        node == "ZZZ"
    }

    val steps = stepNetworkToEnd(network, instructions)
    println(steps)

    val result = nodes.keys
        .filter { it.endsWith('A') }
        .map { startNode ->
            Network(nodes, startNode) {
                it.endsWith('Z')
            }
        }.map { network ->
            network.findCycleLength(instructions)
        }.fold(1L) { total, next ->
            lcm(total, next.toLong())
        }

    println(result)
}

fun lcm(a: Long, b: Long): Long {
    return a / gcd(a, b) * b
}

fun gcd(a: Long, b: Long): Long {
    return if (b == 0L) a else gcd(b, a % b)
}

private fun stepNetworkToEnd(network: Network, instructions: String): Int {
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

    return stepCount
}

private fun parseNodes(input: List<String>): Map<String, Pair<String, String>> {
    val nodeRegex = Regex("""([A-Z0-9]+) = \(([A-Z0-9]+), ([A-Z0-9]+)\)""")

    return input
        .associate { line ->
            val match = requireNotNull(nodeRegex.matchEntire(line))
            val (key, left, right) = match.destructured

            Pair(key, left to right)
        }
}