import java.lang.IllegalArgumentException

fun main() {

    val day = 3.toString().padStart(2, '0')

    fun part1(input: List<String>): Int {
        val initial = List(input.first().length) { PositionResult(0, 0) }
        val positionResults = input.fold(initial) {
            resultList, inputLine -> resultList.zip(inputLine.toList()) { r, c -> r + c }
        }
        val gammaRate = positionResults.map(PositionResult::gamma).binaryRepresentationToInt()
        val epsilonRate = positionResults.map(PositionResult::epsilon).binaryRepresentationToInt()

        return gammaRate * epsilonRate
    }

    fun decodeRatingByCondition(input: List<String>, condition: (Int, Int) -> Char): Int {
        var remainingLines = input.map { it.toList() }
        var position = 0
        while (remainingLines.size > 1) {
            val countOfOnesAndZeros: Map<Char, Int> = remainingLines.map { it[position] }.groupingBy { it }.eachCount()
            val winner = condition(countOfOnesAndZeros.getValue('0'), countOfOnesAndZeros.getValue('1'))
            remainingLines = remainingLines.filter { it[position] == winner }
            position++
        }

        return remainingLines[0].binaryRepresentationToInt()
    }

    fun part2(input: List<String>): Int {
        val oxygenGeneratorRating = decodeRatingByCondition(input) {
            zeros, ones -> if (zeros > ones) '0' else '1'
        }
        val co2ScrubberRating = decodeRatingByCondition(input) {
                zeros, ones -> if (ones < zeros) '1' else '0'
        }
        return oxygenGeneratorRating * co2ScrubberRating
    }

    // test if implementation meets criteria from the description:
    val testInput = readInputAsStrings("Day${day}_test")
    check(part1(testInput) == 198) { "Wrong part 1 answer for the test input" }
    check(part2(testInput) == 230) { "Wrong part 2 answer for the test input" }

    val input = readInputAsStrings("Day$day")
    println("Part 1 answer: ${part1(input)}")
    println("Part 2 answer: ${part2(input)}")
}

private fun <T> List<T>.binaryRepresentationToInt() =
    this.joinToString("").toInt(2)

private data class PositionResult(
    val ones: Int,
    val zeros: Int
) {
    operator fun plus(c: Char): PositionResult =
        when(c) {
            '0' -> this.copy(zeros = zeros+ 1)
            '1' -> this.copy(ones = ones + 1)
            else -> throw IllegalArgumentException("Unexpected character $c")
        }
    val gamma: Byte
        get() = if (ones > zeros) 1 else 0

    val epsilon: Byte
        get() = if (ones < zeros) 1 else 0
}
