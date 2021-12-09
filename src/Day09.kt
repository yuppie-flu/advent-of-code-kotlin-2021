import java.util.UUID

fun main() {
    val day = 9.toString().padStart(2, '0')

    fun part1(map: BasinMap): Int =
        map.getLowPoints().sumOf { it.value + 1 }

    fun part2(map: BasinMap): Int {
        map.getLowPoints().forEach { map.setBasinIdFromLowPoint(it, UUID.randomUUID()) }

        val (v1, v2, v3) = map.points.flatten()
            .filter { it.id != null }
            .groupingBy { it.id }
            .eachCount()
            .values
            .sortedDescending()
            .take(3)
        return v1 * v2 * v3
    }

    // test if implementation meets criteria from the description:
    val testInput = readInputAsStrings("Day${day}_test")
    check(part1(parseBasinMap(testInput)) == 15) { "Wrong part 1 answer for the test input" }
    check(part2(parseBasinMap(testInput)) == 1134) { "Wrong part 2 answer for the test input" }

    val input = readInputAsStrings("Day$day")
    println("Part 1 answer: ${part1(parseBasinMap(input))}")
    println("Part 2 answer: ${part2(parseBasinMap(input))}")
}

data class BasinMap(val points: List<List<BasinPoint>>) {
    private val maxX = points[0].size - 1
    private val maxY = points.size - 1

    fun getLowPoints(): List<BasinPoint> =
        points.flatMap { row ->
            row.mapNotNull { point ->
                if (getNeighbors(point).all { it.value > point.value }) {
                    point
                } else {
                    null
                }
            }
        }

    fun setBasinIdFromLowPoint(p: BasinPoint, id: UUID) {
        if (p.isHighPoint() || p.id != null) return
        p.id = id
        getNeighbors(p).forEach { setBasinIdFromLowPoint(it, id) }
    }

    private fun getNeighbors(p: BasinPoint): List<BasinPoint> {
        val result = mutableListOf<BasinPoint>()
        with(p) {
            if (p.x > 0) result += points[y][x - 1]
            if (x < maxX) result += points[y][x + 1]
            if (y > 0) result += points[y - 1][x]
            if (y < maxY) result += points[y + 1][x]
        }
        return result
    }
}

data class BasinPoint(
    val x: Int,
    val y: Int,
    val value: Int,
    var id: UUID? = null
) {
    fun isHighPoint() = value == 9
}

private fun parseBasinMap(input: List<String>) =
    BasinMap(input.mapIndexed { y, s ->
        s.toCharArray().mapIndexed { x, c ->
            BasinPoint(x, y, c.toString().toInt())
        }
    })
