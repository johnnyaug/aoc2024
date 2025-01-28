package puzzle04

fun part1(): Int {
    var s = 0
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val dirs =
            listOf(Pair(0, 1), Pair(1, 0), Pair(0, -1), Pair(-1, 0), Pair(1, 1), Pair(-1, -1), Pair(1, -1), Pair(-1, 1))
        val l = it.toList()
        for (y in 0..<l.size) {
            for (x in 0..l[0].length) {
                for (d in dirs) {
                    if (l.getOrNull(y)?.getOrNull(x) == 'X' && l.getOrNull(y + d.first)
                            ?.getOrNull(x + d.second) == 'M' && l.getOrNull(y + 2 * d.first)
                            ?.getOrNull(x + 2 * d.second) == 'A' && l.getOrNull(y + 3 * d.first)
                            ?.getOrNull(x + 3 * d.second) == 'S'
                    ) {
                        s += 1
                    }
                }
            }
        }
    }
    return s
}

fun part2(): Int {
    var s = 0
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        val l = it.toList()
        val get = { y: Int, x: Int -> l.getOrNull(y)?.getOrNull(x) }
        for (y in 0..<l.size) {
            for (x in 0..l[0].length) {
                if (l.getOrNull(y)?.getOrNull(x) == 'A') {
                    if ((get(y - 1, x - 1) == 'M' && get(y + 1, x + 1) == 'S' || get(y + 1, x + 1) == 'M' && get(
                            y - 1,
                            x - 1
                        ) == 'S') &&
                        (get(y - 1, x + 1) == 'M' && get(y + 1, x - 1) == 'S' || get(y + 1, x - 1) == 'M' && get(
                            y - 1,
                            x + 1
                        ) == 'S')
                    ) {
                        s += 1
                    }
                }
            }
        }
    }
    return s
}


fun main() {
    println(part1())
    println(part2())
}
