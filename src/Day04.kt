fun main() {

    val day = 4.toString().padStart(2, '0')

    fun part1(input: List<String>): Int {
        val boards: Collection<Board> = readBoards(input)

        // the first winning board
        readNumbersLine(input[0]).forEach { n ->
            val winningScore = boards.firstNotNullOfOrNull { b -> b.markNumberAndReturnScoreIfWinner(n) }
            if (winningScore != null) {
                return winningScore * n
            }
        }
        throw IllegalStateException("No winning board")
    }

    fun part2(input: List<String>): Int {
        val boards = mutableSetOf<Board>()
        boards.addAll(readBoards(input))

        // the last winning board
        readNumbersLine(input[0]).forEach { n ->
            val winnerBoardsToScores: Map<Board, Int> = boards.mapNotNull {
                    b -> b.markNumberAndReturnScoreIfWinner(n)?.let { score -> Pair(b, score) }
            }.toMap()
            boards.removeAll(winnerBoardsToScores.keys)
            if (boards.isEmpty()) {
                return winnerBoardsToScores.values.first() * n
            }
        }
        throw IllegalStateException("No winning board")

    }

    // test if implementation meets criteria from the description:
    val testInput = readInputAsStrings("Day${day}_test")
    check(part1(testInput) == 4512) { "Wrong part 1 answer for the test input" }
    check(part2(testInput) == 1924) { "Wrong part 2 answer for the test input" }

    val input = readInputAsStrings("Day$day")
    println("Part 1 answer: ${part1(input)}")
    println("Part 2 answer: ${part2(input)}")
}

private fun readNumbersLine(line: String): List<Int> =
    line.split(',').map { it.toInt() }

private fun readBoards(input: List<String>): Collection<Board> =
    input.drop(1).filter { it.isNotEmpty() }
    .withIndex()
    .groupBy { it.index / Board.SIZE }
    .mapValues { Board("Board #${it.key + 1}", it.value.map { idxValue -> idxValue.value }) }
    .values

private data class Board(
    private val name: String,
    private val originalRows: List<String>
) {
    companion object {
        const val SIZE = 5
        val ONE_OR_TWO_SPACES = """[ ]{1,2}""".toRegex()
    }

    private val parsedRows: List<List<Int>> =
        originalRows.map {
            it.split(ONE_OR_TWO_SPACES).map { number -> number.toInt() }
        }
    private val unmarkedRows: Set<MutableSet<Int>> = parsedRows.map { it.toMutableSet() }.toSet()
    private val unmarkedColumns: Set<MutableSet<Int>> = (0 until SIZE).map { col ->
        (0 until SIZE).map { row -> parsedRows[row][col] }.toMutableSet()
    }.toSet()

    fun markNumberAndReturnScoreIfWinner(n: Int): Int? {
        (unmarkedRows + unmarkedColumns).forEach { it.remove(n) }
        return if ((unmarkedRows + unmarkedColumns).any { it.isEmpty() }) {
            unmarkedRows.sumOf { it.sum() }
        } else {
            null
        }
    }
}

