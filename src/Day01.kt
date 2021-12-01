fun main() {
    fun calculateNumberOfIncreases(input: IntArray, groupSize: Int): Int =
        input.mapIndexed { idx, value ->
            when {
                idx < groupSize -> 0
                value > input[idx - groupSize] -> 1
                else -> 0
            }
        }.sum()

    fun part1(input: IntArray): Int = calculateNumberOfIncreases(input, 1)

    fun part2(input: IntArray): Int = calculateNumberOfIncreases(input, 3)

    // test if implementation meets criteria from the description:
    val testInput = readInputAsInts("Day01_test")
    check(part1(testInput) == 7) { "Wrong part 1 answer for the test input" }
    check(part2(testInput) == 5) { "Wrong part 2 answer for the test input" }

    val input = readInputAsInts("Day01")
    println("Part 1 answer: ${part1(input)}")
    println("Part 2 answer: ${part2(input)}")
}
