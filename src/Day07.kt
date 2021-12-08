import kotlin.math.abs

fun main() {
    val day = 7.toString().padStart(2, '0')

    fun part1(input: IntArray): Int =
        input.minOf { elemOuter -> input.sumOf { elemInner -> abs(elemOuter - elemInner) } }

    fun part2(input: IntArray) =
        (input.minOrNull()!!..input.maxOrNull()!!).minOf { elemOuter ->
            input.sumOf { elemInner ->
                val distance = abs(elemOuter - elemInner)
                distance * (distance + 1) / 2
            }
        }

    // test if implementation meets criteria from the description:
    val testInput = readSingleLineInputAsInts("Day${day}_test")
    check(part1(testInput) == 37) { "Wrong part 1 answer for the test input" }
    check(part2(testInput) == 168) { "Wrong part 2 answer for the test input" }

    val input = readSingleLineInputAsInts("Day$day")
    println("Part 1 answer: ${part1(input)}")
    println("Part 2 answer: ${part2(input)}")
}