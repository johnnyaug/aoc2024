package puzzle12

import java.util.*

data class Point(val y: Int, val x: Int) {
    fun getNeighbors(): Set<Point> {
        return setOf(
            copy(y = y - 1),
            copy(y = y + 1),
            copy(x = x - 1),
            copy(x = x + 1)
        )
    }

    fun getDiagonals(): Set<Point> {
        return setOf(
            copy(y = y - 1, x = x - 1),
            copy(y = y + 1, x = x + 1),
            copy(y = y - 1, x = x + 1),
            copy(y = y + 1, x = x - 1)
        )
    }
}

fun findNextStart(data: List<String>, pointToRegion: Set<Point>): Point? {
    for (i in data.indices) {
        for (j in data[0].indices) {
            if (!pointToRegion.contains(Point(i, j))) {
                return Point(i, j)
            }
        }
    }
    return null
}


fun getRegion(data: List<String>, start: Point): Set<Point> {
    val points: MutableSet<Point> = mutableSetOf()
    val q: Queue<Point> = LinkedList(listOf(start))
    while (q.isNotEmpty()) {
        val p = q.poll()
        if (points.contains(p)) {
            continue
        }
        points.add(p)
        p.getNeighbors().filter {
            it.y >= 0 && it.y < data.size && it.x >= 0 && it.x < data[0].length && data[it.y][it.x] == data[start.y][start.x]
        }.forEach(q::offer)
    }
    return points
}

fun getPerimeter(points: Set<Point>): Int {
    return points.sumOf { p ->
        4 - p.getNeighbors().intersect(points).size
    }
}

fun countCorners(points: Set<Point>): Int {
    return points.sumOf({ p ->
        p.getDiagonals().filter { d ->
            if (points.contains(d)) {
                d.getNeighbors().intersect(p.getNeighbors()).all { !points.contains(it) }
            } else {
                d.getNeighbors().intersect(p.getNeighbors()).map { points.contains(it) }.toSet().size == 1
            }
        }.size
    })
}

fun part1(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val data = it.toList()
        val donePoints: MutableSet<Point> = mutableSetOf()
        var res = 0
        while (true) {
            val region = getRegion(data, findNextStart(data, donePoints) ?: break)
            res += getPerimeter(region) * region.size
            region.forEach(donePoints::add)
        }
        return res
    }
}

fun part2(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val data = it.toList()
        val donePoints: MutableSet<Point> = mutableSetOf()
        var res = 0
        while (true) {
            val region = getRegion(data, findNextStart(data, donePoints) ?: break)
            res += countCorners(region) * region.size
            region.forEach(donePoints::add)
        }
        return res
    }
}


fun main() {
    println(part1())
    println(part2())
}
