package puzzle3

import kotlin.math.abs

fun part1(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        return it.map {
            Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)").findAll(it).map {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }.sum()
        }.sum()

    }
}

fun part2(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        return Regex("mul\\(([0-9]{1,3}),([0-9]{1,3})\\)").findAll(
            it.joinToString().replace(
                Regex("don't\\(\\)((?!do\\(\\)).)*(\$|do\\(\\))"),
                ""
            )
        ).map {
            it.groupValues[1].toInt() * it.groupValues[2].toInt()
        }.sum()
    }
}

fun main() {
    println(part1())
    println(part2())
}
