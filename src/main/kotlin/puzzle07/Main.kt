package puzzle07

fun getChildren(ways: List<Long>, i: Int, current: Long, part: Int) : List<Long> {
    if (part == 1) {
        return listOf(current * ways[i], current + ways[i])
    }
    return  listOf(current * ways[i], current + ways[i], (current.toString() + ways[i].toString()).toLong())
}

fun dfs(ways: List<Long>, i: Int, dest: Long, current: Long, part: Int): Boolean {
    if (i == ways.size) {
        return dest == current
    }
    if (current > dest) {
        return false
    }
    return getChildren(ways, i, current, part).any {
        dfs(ways, i+1, dest, it, part)
    }
}

fun part1(): Long {
    var res = 0L
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().forEachLine {
        val s = it.split(":")
        val dest = s[0].toLong()
        val ways = s[1].trim().split(" ").map(String::toLong).toList()
        res += if (dfs(ways, 1, dest, ways[0], 1)) dest else 0
    }
    return res
}


fun part2(): Long {
    var res = 0L
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().forEachLine {
        val s = it.split(":")
        val dest = s[0].toLong()
        val ways = s[1].trim().split(" ").map(String::toLong).toList()
        res += if (dfs(ways, 1, dest, ways[0], 2)) dest else 0
    }
    return res
}

fun main() {
    println(part1())
    println(part2())
}
