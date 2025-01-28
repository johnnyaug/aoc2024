package puzzle25

class Solution {
    private val keys: MutableSet<List<Int>> = mutableSetOf()
    private val locks: MutableSet<List<Int>> = mutableSetOf()

    init {
        object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
            it.chunked(8).forEach { pattern ->
                val values = pattern.fold(mutableListOf(0, 0, 0, 0, 0)) { acc: MutableList<Int>, s: String ->
                    s.forEachIndexed { j, c ->
                        if (c == '#') acc[j]++
                    }
                    acc
                }
                (if (pattern[0][0] == '#') locks else keys).add(values)
            }
        }
    }

    fun part1() = keys.map { k ->
        locks.map { l ->
            if (k.zip(l).all { it.first + it.second <= 7 }) 1 else 0
        }
    }.flatten().sum()

    fun part2(): Int {
        return 2024
    }
}


fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
