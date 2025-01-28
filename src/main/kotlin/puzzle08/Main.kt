package puzzle08

data class Point(val y: Int, val x: Int, val c: Char)

class Line(val p1: Point, val p2: Point) {
    private val diffY = p1.y - p2.y
    private val diffX = p1.x - p2.x
    fun getAllPoints(maxX: Int, maxY: Int): Set<Point> {
        val res: MutableSet<Point> = mutableSetOf()
        for (sign in listOf(1, -1)) {
            var (x, y) = p1.x to p1.y
            while (x in 0..maxX && y in 0..maxY) {
                res.add(Point(y, x, '#'))
                x += sign * (p1.x - p2.x)
                y += sign * (p1.y - p2.y)
            }
        }
        return res
    }

    private fun isInline(p: Point): Boolean {
        val otherDiffY = p.y - p1.y
        val otherDiffX = p.x - p1.x
        return otherDiffY.mod(diffY) == 0 && otherDiffX.mod(diffX) == 0 && otherDiffY / diffY == otherDiffX / diffX
    }

    override fun equals(other: Any?) = isInline((other as Line).p1) && isInline(other.p2)

    override fun hashCode() = 0
}

fun getAntidote(p1: Point, p2: Point): Point {
    return Point(2 * p2.x - p1.x, 2 * p2.y - p1.y, '#')
}

fun isValid(p: Point, data: List<String>): Boolean {
    return p.y >= 0 && p.y < data.size && p.x >= 0 && p.x < data[0].length
}

fun part1(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val data = it.toList()
        val points = data.mapIndexed { i, it ->
            it.mapIndexed { j, it -> Point(i, j, it) }.filter { it.c != '.' }
        }.flatten()
        return points.flatMap { p1 ->
            points.filter { it != p1 && it.c == p1.c }.map { p2 -> getAntidote(p1, p2) }.filter { isValid(it, data) }
        }.toSet().size
    }
}


fun part2(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val data = it.toList()
        val points = data.mapIndexed { i, it ->
            it.mapIndexed { j, it -> Point(i, j, it) }.filter { it.c != '.' }
        }.flatten()
        val lines =
            points.flatMap { p1 -> points.filter { it != p1 && it.c == p1.c }.map { p2 -> Line(p1, p2) } }.toSet()
        return lines.map { it.getAllPoints(data[0].length - 1, data.size - 1) }.flatten().toSet().size
    }
}

fun main() {
    println(part1())
    println(part2())
}
