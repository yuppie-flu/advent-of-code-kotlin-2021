import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun readInputAsStrings(name: String): List<String> = File("src", "$name.txt")
    .readLines()
    .map { it.trim() }

fun readInputAsInts(name: String): IntArray = File("src", "$name.txt")
    .readLines()
    .map { it.trim().toInt() }
    .toIntArray()

fun readSingleLineInputAsInts(name: String): IntArray = File("src", "$name.txt")
    .readLines()
    .map { it.trim() }
    .single()
    .split(',')
    .map { it.toInt() }
    .toIntArray()

fun String.md5(): String = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
