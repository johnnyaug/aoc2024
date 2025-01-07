package puzzle11

fun blink(stone: String): List<String> {
    if (stone == "0" || stone == "") {
        return listOf("1")
    }
    if (stone.length.mod(2) == 0) {
        return listOf(
            stone.substring(0..<stone.length / 2).trimStart('0'),
            stone.substring(stone.length / 2).trimStart('0')
        )
    }
    return listOf((2024 * stone.toLong()).toString())
}

fun blink(counts: Map<String, Long>): Map<String, Long> {
    val res: MutableMap<String, Long> = mutableMapOf()
    for ((stone, count) in counts) {
        for (newStone in blink(stone)) {
            res.compute(newStone) { _, newCount ->
                (newCount ?: 0) + count
            }
        }
    }
    return res
}

fun part1(): Int {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        var s = it.first().split(" ")
        for (i in 1..25) {
            s = s.map { blink(it) }.flatten()
        }
        return s.size
    }
}

fun part2(): Long {
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
        var counts = it.first().split(" ").groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        for (j in 1..75) {
            counts = blink(counts)
        }
        return counts.values.sum()
    }
}

fun main() {
    println(part1())
    println(part2())
}
