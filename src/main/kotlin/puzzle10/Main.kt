package puzzle10

import java.util.*

data class Point(val startY: Int, val startX: Int, val y: Int, val x: Int) {
    fun getAdjacent() : List<Point> {
        return listOf(copy(y=y+1), copy(x=x+1), copy(y=y-1), copy(x=x-1))
    }
}

val getHeads = { data: List<List<Int>> ->
    data.mapIndexed { i, it -> it.mapIndexed { j, it -> if (it == 0) j else null }.filterNotNull() }
        .flatMapIndexed { idx, l -> l.map { idx to it } }.map { Point(it.first, it.second, it.first, it.second) }
}

val getNeighbors = { data: List<List<Int>>, p: Point ->
    p.getAdjacent().mapNotNull { pa ->
        if (0 <= pa.y && pa.y < data.size && 0 <= pa.x && pa.x < data[0].size && data[pa.y][pa.x] == data[p.y][p.x] + 1) pa else null
    }
}

fun findNines(lines: Sequence<String>, nineCallback: (Point) -> Unit) {
    val data = lines.map { it.map { it.digitToInt() } }.toList()
    val q: Queue<Point> = LinkedList(getHeads(data))
    while (q.isNotEmpty()) {
        val nxt = q.poll()
        if (data[nxt.y][nxt.x] == 9) {
            nineCallback(nxt)
        } else {
            val elements = getNeighbors(data, nxt)
            q.addAll(elements)
        }
    }
}

fun part1(): Long {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val uniqueDests: MutableSet<Point> = mutableSetOf()
        findNines(it, { p ->
            uniqueDests.add(p)
        })
        return uniqueDests.size.toLong()
    }
}

fun part2(): Long {
    var res = 0L
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        findNines(it, { p ->
            res+=1
        })
    }
    return res
}

fun main() {
    println(part1())
    println(part2())
}
