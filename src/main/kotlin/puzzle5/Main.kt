package puzzle5

import java.io.Reader

typealias Rules = Map<Int, Set<Pair<Int, Int>>>

data class Data(
    val rules: Rules,
    val updates: List<List<Int>>
)

fun isValidUpdate(u: List<Int>, rules: Rules): Boolean {
    for (i in 0..<u.size) {
        for (j in (i + 1)..<u.size) {
            val r = rules.getOrDefault(u[i], emptySet()).intersect(rules.getOrDefault(u[j], emptySet()))
                .firstOrNull()
            if (r != null && r.first == u[j]) {
                return false
            }
        }
    }
    return true
}

fun parseInput(r: Reader): Data {
    val rules: MutableMap<Int, MutableSet<Pair<Int, Int>>> = mutableMapOf()
    val updates: MutableList<List<Int>> = mutableListOf()

    r.forEachLine {
        if (it.contains("|")) {
            val (p1, p2) = it.split("|")
            val p = Pair(p1.toInt(), p2.toInt())
            val l1 = rules.getOrDefault(p.first, mutableSetOf())
            l1.add(p)
            val l2 = rules.getOrDefault(p.second, mutableSetOf())
            l2.add(p)
            rules[p.first] = l1
            rules[p.second] = l2
        } else if (it != "") {
            updates.add(it.split(",").map { it.toInt() })
        }
    }
    return Data(rules, updates)
}

fun part1(): Int {
    val (rules, updates) = parseInput(object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader())
    return updates.sumOf {
        if (isValidUpdate(it, rules)) it[it.size / 2] else 0
    }
}

fun part2(): Int {
    val (rules, updates) = parseInput(object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader())
    return updates.sumOf {
        if (!isValidUpdate(it, rules)) {
            it.sortedWith { a, b ->
                val r1 = rules.getOrDefault(a, emptySet()).intersect(rules.getOrDefault(b, emptySet()))
                    .firstOrNull()
                if (r1 == null) 0 else if (r1.first == a) -1 else 1
            }[it.size / 2]
        } else 0
    }
}


fun main() {
    println(part1())
    println(part2())
}
