package puzzle14

private const val h = 103
private const val w = 101

data class Point(var y: Int, var x: Int) {
    constructor(l: List<Int>) : this(l[1], l[0])

    fun add(o: Point) {
        y = (y + o.y).mod(h)
        x = (x + o.x).mod(w)
    }
}

data class Robot(val p: Point, var v: Point) {
    constructor(l: List<Point>) : this(l[0], l[1])

    fun click() = p.add(v)
}

class Solution {
    private val robots: List<Robot> =
        object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().lines().map {
            Robot(it.split(" ").map { sub -> sub.substringAfter("=") }
                .map { p -> Point(p.split(",").map(String::toInt)) })
        }.toList()

    fun part1(): Long {
        repeat(100) { robots.forEach(Robot::click) }
        return robots.filter { it.p.y != h / 2 && it.p.x != w / 2 }.groupingBy {
            (if (it.p.y < h / 2) "U" else "D") + (if (it.p.x < w / 2) "L" else "R")
        }.eachCount().values.fold(1L) { a, b -> a * b.toLong() }
    }

    fun part2(): Int {
        (1..h * w).forEach { time ->
            val board = robots.map { r ->
                r.click()
                r.p
            }.toSet()
            for (y in 0..h) {
                if ((w / 2 - 7..w / 2 + 7).all { x -> board.contains(Point(y, x)) }) {
                    return time
                }
            }
        }
        return -1
    }
}

fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
