package puzzle13

import kotlin.math.min

data class Point(val y: Long, val x: Long) {
    constructor(l: List<Long>) : this(l[0], l[1])
}

data class Machine(val a: Point, val b: Point, val p: Point)

fun getMinPrice(d1: Point, d2: Point, target: Point, memo: MutableMap<Point, Int>): Int {
    if (target.y == 0L && target.x == 0L) {
        return 0
    }
    if (target.x < 0 || target.y < 0) {
        return -1
    }
    if (memo.contains(target)) {
        return memo[target]!!
    }
    val resA = getMinPrice(d1, d2, Point(target.y - d1.y, target.x - d1.x), memo)
    val resB = getMinPrice(d1, d2, Point(target.y - d2.y, target.x - d2.x), memo)
    if (resA == -1 && resB == -1) {
        memo[target] = -1
    } else if (resA == -1) {
        memo[target] = 1 + resB
    } else if (resB == -1) {
        memo[target] = 3 + resA
    } else {
        memo[target] = min(3 + resA, 1 + resB)
    }
    return memo[target]!!
}

fun parseMachine(lines: List<String>, idx: Int, prizeBump: Long = 0): Machine {
    val buttonA = Point(lines[idx * 4].substringAfter(": ").split(", ").map { it.substringAfter("+").toLong() })
    val buttonB = Point(lines[idx * 4 + 1].substringAfter(": ").split(", ").map { it.substringAfter("+").toLong() })
    val prize =
        Point(lines[idx * 4 + 2].substringAfter(": ").split(", ").map { prizeBump + it.substringAfter("=").toLong() })
    return Machine(buttonA, buttonB, prize)
}

fun part1(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val l = it.toList()
        return (0..l.size / 4).map { idx -> parseMachine(l, idx) }
            .map { m -> getMinPrice(m.a, m.b, m.p, mutableMapOf()) }.filter { m -> m > 0 }.sum()
    }
}

fun part2(): Long {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val l = it.toList()
        return (0..l.size / 4).map { idx -> parseMachine(l, idx, 10000000000000) }.sumOf { m ->
            if (m.a.x.toDouble() / m.a.y != m.b.x.toDouble() / m.b.y && (m.b.x * m.p.y - m.b.y * m.p.x).mod(m.b.x * m.a.y - m.a.x * m.b.y) == 0L && (m.a.x * m.p.y - m.a.y * m.p.x).mod(
                    -m.b.x * m.a.y + m.a.x * m.b.y
                ) == 0L
            ) {
                3 * (m.b.x * m.p.y - m.b.y * m.p.x) / (m.b.x * m.a.y - m.a.x * m.b.y) + (m.a.x * m.p.y - m.a.y * m.p.x) / (-m.b.x * m.a.y + m.a.x * m.b.y)
            } else if (m.p.x.toDouble() / m.p.y == m.a.x.toDouble() / m.a.y && m.a.x / m.b.x < 3 && m.p.x.mod(m.b.x)
                    .mod(m.a.x) == 0L
            ) {
                m.p.x / m.b.x + 3 * (m.p.x.mod(m.b.x) / m.a.x)
            } else {
                0L
            }
        }
    }

}


fun main() {
    println(part1())
    println(part2())
}
