import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

fun main() {
    val day = 5.toString().padStart(2, '0')

    fun part1(input: List<String>) =
        countOverlapPoints(input) { parseOrthogonalLine(it) }

    fun part2(input: List<String>) =
        countOverlapPoints(input) { parseOrthogonalOrDiagonalLine(it) }

    // test if implementation meets criteria from the description:
    val testInput = readInputAsStrings("Day${day}_test")
    check(part1(testInput) == 5) { "Wrong part 1 answer for the test input" }
    check(part2(testInput) == 12) { "Wrong part 2 answer for the test input" }

    val input = readInputAsStrings("Day$day")
    println("Part 1 answer: ${part1(input)}")
    println("Part 2 answer: ${part2(input)}")
}

private fun countOverlapPoints(input: List<String>, lineParser: (String) -> Line?): Int =
    input.mapNotNull { lineParser(it) }
        .flatMap { it.getAllPoints() }
        .groupingBy { it }
        .eachCount()
        .count { it.value > 1 }

private fun parseLine(input: String, lineBuilder: (Point, Point) -> Line?): Line? =
    input.split(" -> ", limit = 2).let {
        lineBuilder(parsePoint(it[0]), parsePoint(it[1]))
    }

private fun parseOrthogonalLine(input: String): Line? =
    parseLine(input) { a, b -> if (a.x == b.x || a.y == b.y) OrthogonalLine(a, b) else null }

private fun parseOrthogonalOrDiagonalLine(input: String): Line? =
    parseLine(input) { a, b ->
        when {
            a.x == b.x || a.y == b.y -> OrthogonalLine(a, b)
            abs(a.x - b.x) == abs(a.y - b.y) -> DiagonalLine(a, b)
            else -> null
        }
    }

private fun parsePoint(input: String): Point =
    input.split(',', limit = 2)
        .map { it.toInt() }
        .let { Point(it[0], it[1]) }

private data class Point(val x: Int, val y: Int)

private interface Line {
    fun getAllPoints(): List<Point>
}

private data class OrthogonalLine(val start: Point, val end: Point): Line {

    init {
        check(start.x == end.x || start.y == end.y) { "Not an orthogonal line!" }
    }

    override fun getAllPoints(): List<Point> =
        when {
            start.x == end.x -> (min(start.y, end.y)..max(start.y, end.y)).map { Point(start.x, it) }
            start.y == end.y -> (min(start.x, end.x)..max(start.x, end.x)).map { Point(it, start.y) }
            else -> throw IllegalStateException("Not an orthogonal line! [$this]")
        }
}

private data class DiagonalLine(val start: Point, val end: Point): Line {

    init {
        check(abs(start.x - end.x) == abs(start.y - end.y)) { "Not a diagonal line! [$this]" }
    }

    override fun getAllPoints(): List<Point> =
        when {
            start.x < end.x && start.y < end.y -> (0..end.x - start.x).map { Point(start.x + it, start.y + it) }
            start.x < end.x && start.y > end.y -> (0..end.x - start.x).map { Point(start.x + it, start.y - it) }
            start.x > end.x && start.y < end.y -> (0..start.x - end.x).map { Point(start.x - it, start.y + it) }
            start.x > end.x && start.y > end.y -> (0..start.x - end.x).map { Point(start.x - it, start.y - it) }
            else -> throw IllegalStateException("Not a line! [$this]")
        }
}
