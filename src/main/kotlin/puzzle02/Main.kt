package puzzle02

import kotlin.math.abs

fun part1(): Int {
    object {}.javaClass.getResourceAsStream("input0.txt")!!.bufferedReader().useLines {
        return it.map {
            val row = it.split(" ").map { it.toInt() }
            if (row.mapIndexed { index, current ->
                    index == 0 || current > row[index - 1] && abs(current - row[index - 1]) >= 1 && abs(current - row[index - 1]) <= 3
                }.all { it } || row.mapIndexed { index, current ->
                    index == 0 || current < row[index - 1] && abs(current - row[index - 1]) >= 1 && abs(current - row[index - 1]) <= 3
                }.all { it }) {
                1
            } else {
                0
            }
        }.sum()
    }
}


fun part2(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        return it.map {
            val row = it.split(" ").map { it.toInt() }
            for (i in 0..<row.size) {
                val r = row.filterIndexed { index, _ -> index != i }
                if (r.mapIndexed { index, current ->
                        index == 0 || current > r[index - 1] && abs(current - r[index - 1]) >= 1 && abs(current - r[index - 1]) <= 3
                    }.all { it } || r.mapIndexed { index, current ->
                        index == 0 || current < r[index - 1] && abs(current - r[index - 1]) >= 1 && abs(current - r[index - 1]) <= 3
                    }.all { it }) {
                    return@map 1
                }
            }
            0
        }.sum()
    }
}

fun main() {
    println(part1())
    println(part2())
}
