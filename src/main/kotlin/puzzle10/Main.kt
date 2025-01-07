package puzzle10

import java.util.*

data class Point(val startY: Int, val startX: Int, val y: Int, val x: Int) {
    fun getNeighbors(data: List<List<Int>>): List<Point> {
        return listOf(
            copy(y = y + 1),
            copy(x = x + 1),
            copy(y = y - 1),
            copy(x = x - 1)
        ).filter { o -> 0 <= o.y && o.y < data.size && 0 <= o.x && o.x < data[0].size && data[o.y][o.x] == data[y][x] + 1 }
    }
}

val getHeads = { data: List<List<Int>> ->
    data.mapIndexed { _, row -> row.mapIndexed { j, value -> if (value == 0) j else null }.filterNotNull() }
        .flatMapIndexed { idx, l -> l.map { idx to it } }.map { Point(it.first, it.second, it.first, it.second) }
}

fun applyOnNines(lines: Sequence<String>, nineCallback: (Point) -> Unit) {
    val data = lines.map { l -> l.map { it.digitToInt() } }.toList()
    val q: Queue<Point> = LinkedList(getHeads(data))
    while (q.isNotEmpty()) {
        val nxt = q.poll()
        if (data[nxt.y][nxt.x] == 9) {
            nineCallback(nxt)
        } else {
            val elements = nxt.getNeighbors(data)
            q.addAll(elements)
        }
    }
}

fun part1(): Long {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val uniqueDests: MutableSet<Point> = mutableSetOf()
        applyOnNines(it, uniqueDests::add)
        return uniqueDests.size.toLong()
    }
}

fun part2(): Long {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        var res = 0L
        applyOnNines(it) { res += 1 }
        return res
    }
}

fun main() {
    println(part1())
    println(part2())
}
