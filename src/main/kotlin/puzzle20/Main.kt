package puzzle20

import kotlin.math.abs

const val allowedShortcut = 20

data class Point(val y: Int, val x: Int) {
    operator fun plus(d: Dir): Point {
        return copy(y = y + d.y, x = x + d.x)
    }
}

enum class Dir(val y: Int, val x: Int) {
    U(-1, 0), D(1, 0), L(0, -1), R(0, 1)
}


class Solution {
    private val start: Point
    private val maze: List<String>

    init {
        object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
            maze = it.toList()
            start = maze.mapIndexed { i, row ->
                row.mapIndexed { j, _ -> Point(i, j) }.filter { p -> get(p) == 'S' }
            }.flatten().first()
        }
    }

    fun get(p: Point): Char {
        return maze[p.y][p.x]
    }

    private fun findOptimal(): List<Point> {
        var p = start
        val way: MutableList<Point> = mutableListOf(p)
        while (get(p) != 'E') {
            p = listOf(
                p.copy(y = p.y + 1), p.copy(y = p.y - 1), p.copy(x = p.x + 1), p.copy(x = p.x - 1)
            ).first { p0 -> p0 !in way && get(p0) != '#' }
            way.add(p)
        }
        return way
    }


    fun part1(): Int {
        val way = findOptimal()
        return way.mapIndexed { i, p ->
            Dir.entries.mapNotNull { d ->
                if (get(p + d) == '#') p + d + d else null
            }.map { way.indexOf(it) - i - 2 }.count { it >= 100 }
        }.sum()
    }

    fun part2(): Int {
        var res = 0
        val way = findOptimal()
        for (i in way.indices) {
            for (j in (i + 1)..<way.size) {
                val shortcutSize = abs(way[i].y - way[j].y) + abs(way[i].x - way[j].x)
                if (shortcutSize <= allowedShortcut && (j - i) >= shortcutSize + 100) {
                    res += 1
                }
            }
        }
        return res
    }
}

fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
