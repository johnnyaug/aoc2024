package puzzle1

import kotlin.math.abs

fun part1(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val (l1, l2) = it.fold(
            Pair<MutableList<Int>, MutableList<Int>>(
                mutableListOf(),
                mutableListOf()
            )
        ) { acc, next ->
            val vals = next.split(Regex("\\s+"))
            acc.first += vals[0].toInt()
            acc.second += vals[1].toInt()
            acc
        }
        l1.sort()
        l2.sort()
        return l1.zip(l2).map {
            abs(it.first - it.second)
        }.sum()
    }
}

fun part2(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val (l1, l2) = it.fold(
            Pair<MutableList<Int>, MutableList<Int>>(
                mutableListOf(),
                mutableListOf()
            )
        ) { acc, next ->
            val vals = next.split(Regex("\\s+"))
            acc.first += vals[0].toInt()
            acc.second += vals[1].toInt()
            acc
        }
        val frequencies = l2.groupingBy { it }.eachCount()
        return l1.map { it * frequencies.getOrDefault(it, 0) }.sum()
    }
}

fun main() {
    println(part1())
    println(part2())
}
