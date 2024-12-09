package puzzle6

fun get(data: List<String>, y: Int, x: Int): Char? {
    return data.getOrNull(y)?.getOrNull(x)
}

fun next(data: List<String>, p: Point): Point? {
    val nextY = p.y + dirs[p.d].first
    val nextX = p.x + dirs[p.d].second
    if (get(data, nextY, nextX) == '#') {
        return Point((p.d + 1).mod(4), p.y, p.x)
    } else if (get(data, nextY, nextX) == null) {
        return null
    } else {
        return Point(p.d, nextY, nextX)
    }
}

fun getStart(data: List<String>): Point {
    data.forEachIndexed ret@{ i, r ->
        r.forEachIndexed { j, c ->
            if (c == '^') {
                return Point(0, i, j)
            }
        }
    }
    return Point(0, 0, 0)
}

val dirs = listOf(Pair(-1, 0), Pair(0, 1), Pair(1, 0), Pair(0, -1))
data class Point(val d: Int, val y: Int, val x: Int)

fun part1(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val data = it.toList()
        val start = getStart(data)
        val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()
        var p: Point? = start
        while (p != null) {
            visited.add(Pair(p.y, p.x))
            p = next(data, p)
        }
        return visited.size
    }
}


fun part2(): Int {

    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val data = it.toList()
        fun isCycle(data: List<String>, start: Point, extraY: Int, extraX: Int): Boolean {
            val visited: MutableSet<Point> = mutableSetOf()
            var p: Point? = start
            while (p != null) {
                if (visited.contains(p)) {
                    return true
                }
                visited.add(p!!)
                val p1 = next(data, p!!)
                if (p1 != null && p1.d == p!!.d && p1.y == extraY && p1.x == extraX) {
                    p = Point((p1.d + 1).mod(4), p!!.y, p!!.x)
                } else {
                    p = p1
                }
            }
            return false
        }

        val start = getStart(data)
        var p: Point? = start
        val found: MutableSet<Pair<Int, Int>> = mutableSetOf()
        val visited: MutableSet<Point> = mutableSetOf()
        while (true) {
            p = next(data, p!!)
            if (p == null) {
                break
            }
            if (!found.contains(Pair(p!!.y, p!!.x)) && isCycle(data, start, p!!.y, p!!.x)) {
                found += Pair(p!!.y, p!!.x)
            }
            visited.add(p!!)
        }
        return found.size
    }
}

fun main() {
    println(part1())
    println(part2())
}
