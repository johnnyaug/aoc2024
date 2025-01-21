package puzzle21

import puzzle21.Directional.*
import kotlin.math.absoluteValue

val numerical = listOf(listOf('7', '8', '9'), listOf('4', '5', '6'), listOf('1', '2', '3'), listOf(null, '0', 'A'))
val directional = listOf(listOf(null, '^', 'A'), listOf('<', 'v', '>'))

enum class Directional(val key: Char) {
    U('^'), D('v'), L('<'), R('>'), A('A');
}

fun getPossibleTransitions(a1: Char, a2: Char, level: Int): List<List<Pair<Directional, Int>>> {
    val board = when {
        level >= 1 -> directional
        else -> numerical
    }
    val (y1, x1) = board.mapIndexed { i, row -> i to row.indexOf(a1) }.first { it.second != -1 }
    val (y2, x2) = board.mapIndexed { i, row -> i to row.indexOf(a2) }.first { it.second != -1 }
    val horizontalFirst = listOfNotNull((R to (x2 - x1).absoluteValue).takeIf { x2 > x1 },
        (L to (x2 - x1).absoluteValue).takeIf { x2 < x1 },
        (D to (y2 - y1).absoluteValue).takeIf { y2 > y1 },
        (U to (y2 - y1).absoluteValue).takeIf { y2 < y1 })
    val verticalFirst = horizontalFirst.reversed()
    return listOfNotNull((horizontalFirst + (A to 1)).takeIf { board.getOrNull(y1)?.getOrNull(x2) != null },
        (verticalFirst + (A to 1)).takeIf { board.getOrNull(y2)?.getOrNull(x1) != null })
}

fun transitionCost(
    c1: Char, c2: Char, memo: MutableMap<Pair<Int, Pair<Char, Char>>, Long>, maxLevel: Int, level: Int = 0
): Long {
    if (level == maxLevel) return getPossibleTransitions(c1, c2, level).first().sumOf { it.second }.toLong()
    return memo[level to (c1 to c2)] ?: getPossibleTransitions(c1, c2, level).minOf {
        var prev = 'A'
        it.sumOf { (t, count) ->
            (transitionCost(prev, t.key, memo, maxLevel, level + 1) + if (count > 1) count - 1 else 0).also {
                prev = t.key
            }
        }
    }.also { memo[level to (c1 to c2)] = it }
}

class Solution {
    private val codes = object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().lineSequence().toList()
    private fun solve(maxLevel: Int) = codes.sumOf { code ->
        val memo: MutableMap<Pair<Int, Pair<Char, Char>>, Long> = mutableMapOf()
        code.foldIndexed(0L) { i, acc: Long, c ->
            acc + code.substring(0..<code.length - 1).toLong() * transitionCost(
                if (i > 0) code[i - 1] else 'A', c, memo, maxLevel
            )
        }
    }

    fun part1() = solve(2)
    fun part2() = solve(25)
}

fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
