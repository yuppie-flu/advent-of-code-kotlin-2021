fun main() {
    val day = 6.toString().padStart(2, '0')

    fun part1(input: IntArray) = countFishPopulation(80, input)

    fun part2(input: IntArray) = countFishPopulation(256, input)

    // test if implementation meets criteria from the description:
    val testInput = readSingleLineInputAsInts("Day${day}_test")
    check(part1(testInput) == 5934L) { "Wrong part 1 answer for the test input" }
    check(part2(testInput) == 26984457539) { "Wrong part 2 answer for the test input" }

    val input = readSingleLineInputAsInts("Day$day")
    println("Part 1 answer: ${part1(input)}")
    println("Part 2 answer: ${part2(input)}")
}

const val NEW_FISH_BIRTH_COUNTER = 8
const val OLD_FISH_BIRTH_COUNTER = 6

private fun countFishPopulation(tillDay: Int, initialCounters: IntArray): Long {
    var ageToCount = initialCounters.toList()
        .groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }

    repeat(tillDay) {
        ageToCount = (0..NEW_FISH_BIRTH_COUNTER).mapNotNull { age ->
            when (age) {
                OLD_FISH_BIRTH_COUNTER -> {
                    val count = (ageToCount[0] ?: 0) + (ageToCount[OLD_FISH_BIRTH_COUNTER + 1] ?: 0)
                    if (count > 0) age to count else null
                }
                NEW_FISH_BIRTH_COUNTER -> ageToCount[0]?.let { age to it }
                else -> ageToCount[age + 1]?.let { age to it }
            }
        }.toMap()
    }

    return ageToCount.entries.sumOf { it.value }
}