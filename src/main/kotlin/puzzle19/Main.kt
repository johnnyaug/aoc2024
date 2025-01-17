package puzzle19

import kotlin.math.min

class Solution {
    private val patterns: Set<String>
    private val desired: Set<String>
    private val maxPattern: Int

    init {
        object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
            it.toList().run {
                patterns = this[0].split(", ").toSet()
                desired = this.slice(2..<size).toSet()
                maxPattern = patterns.maxOf { p -> p.length }
            }
        }
    }

    private fun countPossible(design: String, from: Int, memo: MutableMap<Int, Long>): Long {
        if (from == design.length) return 1
        memo[from]?.also { return it }
        return (1..min(maxPattern, design.length - from)).filter { design.substring(from..<from + it) in patterns }
            .sumOf { countPossible(design, from + it, memo) }.also { memo[from] = it }
    }

    fun part1(): Int {
        return desired.count { countPossible(it, 0, mutableMapOf()) > 0 }
    }

    fun part2(): Long {
        return desired.sumOf { countPossible(it, 0, mutableMapOf()) }
    }
}


fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
