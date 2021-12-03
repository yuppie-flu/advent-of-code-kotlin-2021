fun main() {

    fun rideSubmarine(input: List<String>, initialPosition: Position): Int =
        input.map { parseMove(it) }
            .fold(initialPosition) { acc, elem -> acc + elem }
            .toResult()

    fun part1(input: List<String>): Int = rideSubmarine(input, SimplePosition(0, 0))


    fun part2(input: List<String>): Int = rideSubmarine(input, AdvancedPosition(0, 0, 0))

    // test if implementation meets criteria from the description:
    val testInput = readInputAsStrings("Day02_test")
    check(part1(testInput) == 150) { "Wrong part 1 answer for the test input" }
    check(part2(testInput) == 900) { "Wrong part 2 answer for the test input" }

    val input = readInputAsStrings("Day02")
    println("Part 1 answer: ${part1(input)}")
    println("Part 2 answer: ${part2(input)}")
}

private fun parseMove(input: String): Move {
    val (direction, distance) = input.split(" ", limit = 2)
    return Move(Direction.valueOf(direction.uppercase()), distance.toInt())
}

private interface Position {
    operator fun plus(move: Move): Position
    fun toResult(): Int
}

private data class Move(
    val direction: Direction,
    val distance: Int
)

private enum class Direction {
    FORWARD, UP, DOWN
}

private data class SimplePosition(
    val horizontal: Int,
    val depth: Int
) : Position {
    override operator fun plus(move: Move): SimplePosition =
        when (move.direction) {
            Direction.FORWARD -> this.copy(horizontal = horizontal + move.distance)
            Direction.UP -> this.copy(depth = depth - move.distance)
            Direction.DOWN -> this.copy(depth = depth + move.distance)
        }

    override fun toResult(): Int = horizontal * depth
}

private data class AdvancedPosition(
    val aim: Int,
    val horizontal: Int,
    val depth: Int
) : Position {
    override operator fun plus(move: Move): AdvancedPosition =
        when (move.direction) {
            Direction.FORWARD -> AdvancedPosition(
                aim, horizontal + move.distance, depth + aim * move.distance
            )
            Direction.UP -> this.copy(aim = aim - move.distance)
            Direction.DOWN -> this.copy(aim = aim + move.distance)
        }

    override fun toResult(): Int = horizontal * depth
}