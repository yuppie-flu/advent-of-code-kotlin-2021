fun main() {
    val day = 8.toString().padStart(2, '0')

    fun part1(input: List<String>): Int =
        input.flatMap { parseInput(it).output }
            .map { it.length }
            .count { it in Input.UNIQUE_LENGTHS }

    fun part2(input: List<String>) =
        input.map { parseInput(it) }
            .sumOf { decodeInput(it) }

    // test if implementation meets criteria from the description:
    val testInput = readInputAsStrings("Day${day}_test")
    check(part1(testInput) == 26) { "Wrong part 1 answer for the test input" }
    check(part2(testInput) == 61229) { "Wrong part 2 answer for the test input" }

    val input = readInputAsStrings("Day$day")
    println("Part 1 answer: ${part1(input)}")
    println("Part 2 answer: ${part2(input)}")
}

data class Input(
    val pattern: List<String>,
    val output: List<String>
) {
    companion object {
        val DIGIT_TO_ENCODED_LENGTH = mapOf(
            0 to 6,
            1 to 2,
            2 to 5,
            3 to 5,
            4 to 4,
            5 to 5,
            6 to 6,
            7 to 3,
            8 to 7,
            9 to 6
        )
        val UNIQUE_LENGTHS = DIGIT_TO_ENCODED_LENGTH.values
            .groupingBy { it }
            .eachCount()
            .filter { it.value == 1 }
            .keys
    }

    fun getEncodedDigit(n: Int): Set<Set<Char>> {
        check(n in 0..9) { "Not a digit $n!" }
        return pattern.filter { it.length == DIGIT_TO_ENCODED_LENGTH[n] }
            .map { it.toCharArray().toSet() }
            .toSet()
    }
}

data class EncodedDigit(
    val chars: Set<Char>,
    val value: Int
)

private fun decodeInput(input: Input): Int {
    val (one, four, seven, eight) = setOf(1, 4, 7, 8).map { EncodedDigit(input.getEncodedDigit(it).single(), it) }
    val zeroSixNineChars = input.getEncodedDigit(0)

    val six = EncodedDigit(zeroSixNineChars.single { !it.containsAll(one.chars) }, 6)
    val topRightVerticalChar = (one.chars - six.chars).single()

    val topLeftVerticalCharAndMiddleHorizontalChar = four.chars - one.chars
    val zero = EncodedDigit(
        zeroSixNineChars.single { !it.containsAll(topLeftVerticalCharAndMiddleHorizontalChar)}, 0
    )
    val nine = EncodedDigit(
        zeroSixNineChars.single { it != six.chars && it != zero.chars }, 9
    )

    val twoThreeFiveChars = input.getEncodedDigit(2)
    val three = EncodedDigit(twoThreeFiveChars.single { it.containsAll(one.chars) }, 3)
    val five = EncodedDigit(
        twoThreeFiveChars.filter { it != three.chars }.single { !it.contains(topRightVerticalChar) }, 5
    )
    val two = EncodedDigit(
        twoThreeFiveChars.filter { it != three.chars }.single { it.contains(topRightVerticalChar) }, 2
    )

    val digits = setOf(zero, one, two, three, four, five, six, seven, eight, nine)

    return input.output.map { output ->
        digits.single { digit -> output.toCharArray().toSet() == digit.chars }.value
    }.joinToString("").toInt()
}

private fun parseInput(input: String): Input {
    val (pattern, output) = input.split("|", limit = 2).map { it.trim() }
    return Input(pattern.split(" "), output.split(" "))
}
