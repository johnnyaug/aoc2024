package puzzle23

class Solution {

    private val connections = object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().run {
        useLines {
            it.toList().map { row -> row.split("-") }
                .map { nodes -> if (nodes[1] > nodes[0]) nodes[0] to nodes[1] else nodes[1] to nodes[0] }
        }
    }

    private val connectionMap: Map<String, Set<String>> = buildMap {
        connections.forEach { (c1, c2) ->
            compute(c1) { _, v -> (v ?: emptySet()) + c2 }
            compute(c2) { _, v -> (v ?: emptySet()) + c1 }
        }
    }

    fun part1(): Int {
        return connections.sumOf { (c1, c2) ->
            connectionMap[c1]!!.intersect(connectionMap[c2]!!)
                .count { c2 < it && (c1.startsWith("t") || c2.startsWith("t") || it.startsWith("t")) }
        }
    }

    /**
     * Bron-Kerbosch algorithm:
     * https://en.wikipedia.org/wiki/Bron%E2%80%93Kerbosch_algorithm
     **/
    private fun findMaximalCliques(must: Set<String>, p: Set<String>, x: Set<String>, results: MutableSet<Set<String>>) {
        if (p.isEmpty() && x.isEmpty()) {
            results.add(must)
        }
        var possible = p
        var disallowed = x
        for (v in possible) {
            findMaximalCliques(must + v, possible.intersect(connectionMap[v]!!), disallowed.intersect(connectionMap[v]!!), results)
            possible = possible - v
            disallowed = disallowed + v
        }
    }

    fun part2(): String {
        val maximalCliques: MutableSet<Set<String>> = mutableSetOf()
        findMaximalCliques(emptySet(), connectionMap.keys, emptySet(), maximalCliques)
        return maximalCliques.first { clique -> clique.size == maximalCliques.maxOf { it.size } }.sorted().joinToString(",")
    }

}

fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
