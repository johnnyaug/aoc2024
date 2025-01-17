package puzzle18

import java.util.*

data class Point(val y: Int, val x: Int) {
    constructor(l: List<Int>) : this(l[1], l[0])

    override fun toString(): String {
        return "%s,%s".format(x, y)
    }

    fun getNeighbors(obstacles: Set<Point>, h: Int, w: Int): List<Point> {
        return listOf(
            copy(y = y + 1), copy(x = x + 1), copy(y = y - 1), copy(x = x - 1)
        ).filter { o -> o.y in 0..<h && o.x in 0..<w && o !in obstacles }
    }
}

class Solution(dataFilename: String, private val h: Int, private val w: Int) {
    private val obstacles: Set<Point>
    private val obstacleList: List<Point>

    init {
        object {}.javaClass.getResourceAsStream(dataFilename)!!.bufferedReader().useLines {
            it.map { row -> Point(row.split(",").map(String::toInt)) }.run {
                obstacleList = toList()
                obstacles = obstacleList.toSet()
            }
        }
    }

    private fun aStar(byteCount: Int): Int {
        val start = Point(0, 0)
        val cameFrom: MutableMap<Point, Point> = mutableMapOf()
        val distances: MutableMap<Point, Int> = mutableMapOf()
        val guesses: MutableMap<Point, Int> = mutableMapOf()
        val q: Queue<Point> = PriorityQueue { p1, p2 ->
            guesses.getOrDefault(p1, Int.MAX_VALUE) - guesses.getOrDefault(p2, Int.MAX_VALUE)
        }
        distances[start] = 0
        guesses[start] = h + w
        q.offer(start)
        while (q.isNotEmpty()) {
            val current = q.poll()
            if (current.y == h - 1 && current.x == w - 1) {
                return distances[current]!!
            }
            for (n in current.getNeighbors(obstacles.take(byteCount).toSet(), h, w)) {
                val distance = distances[current]!! + 1
                if (distance < distances.getOrDefault(n, Int.MAX_VALUE)) {
                    cameFrom[n] = current
                    distances[n] = distance
                    guesses[n] = distance + h - n.y + w - n.x
                    if (n !in q) q.offer(n)
                }
            }
        }
        return -1
    }

    fun part1(byteCount: Int): Int = aStar(byteCount)

    fun part2(): String {
        var s = 1
        var e = obstacles.size
        while (e - s > 1) {
            val mid = (e + s) / 2
            if (aStar(mid) > -1) s = mid else e = mid
        }
        return obstacleList[(s..e).first { aStar(it + 1) == -1 }].toString()
    }
}


fun main() {
//    println(Solution("input0.txt", 7, 7).part1(12))
    println(Solution("input.txt", 71, 71).part1(1024))
//    println(Solution("input0.txt", 7, 7).part2())
    println(Solution("input.txt", 71, 71).part2())
}
