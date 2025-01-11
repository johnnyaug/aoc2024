package puzzle16

import java.util.*


data class Point(val y: Int, val x: Int) {
    operator fun plus(d: Dir): Point {
        return copy(y = y + d.y, x = x + d.x)
    }
}

data class State(val p: Point, val d: Dir)

enum class Dir(val y: Int, val x: Int) {
    U(-1, 0), R(0, 1), D(1, 0), L(0, -1);

    fun turns(): Set<Dir> {
        return setOf(entries[(this.ordinal + 1).mod(4)], entries[(this.ordinal - 1).mod(4)])
    }
}

class Solution {
    private val maze = object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().run {
        useLines { it.map { row -> row.toCharArray() }.toList() }
    }

    fun get(p: Point): Char {
        return maze[p.y][p.x]
    }

    inner class Node(val s: State, val dist: Int, val parent: Node?) : Comparable<Node> {
        override operator fun compareTo(other: Node): Int = dist - other.dist

        fun getNeighbors(): Set<Node> {
            val state = s
            val dst = state.p + state.d
            val res: MutableSet<Node> = mutableSetOf()
            if (get(dst) in listOf('.', 'S', 'E')) {
                res.add(Node(State(dst, state.d), dist + 1, this))
            }
            res.addAll(state.d.turns().map { newDir -> Node(State(state.p, newDir), dist + 1000, this) })
            return res
        }
    }

    private fun dijkstra(cb: (Node) -> Boolean): Int {
        val q = PriorityQueue<Node>()
        q.offer(Node(State(Point(maze.size - 2, 1), Dir.R), 0, null))
        val visitedStates: MutableMap<State, Int> = mutableMapOf()
        var minDistance: Int = Int.MAX_VALUE
        while (q.isNotEmpty()) {
            val current = q.poll()
            if (visitedStates.getOrDefault(current.s, Int.MAX_VALUE) < current.dist) {
                continue
            }
            if (get(current.s.p) == 'E') {
                if (current.dist > minDistance || !cb(current)) {
                    return current.dist
                }
                minDistance = current.dist
            }
            visitedStates[current.s] = current.dist
            current.getNeighbors().forEach(q::offer)
        }
        return 0
    }

    fun part1(): Int = dijkstra { false }


    fun part2(): Int {
        val q2: Queue<Node> = LinkedList()
        dijkstra {
            q2.offer(it)
            true
        }
        val points: MutableSet<Point> = mutableSetOf()
        while (q2.isNotEmpty()) {
            val current = q2.poll()
            points.add(current.s.p)
            current.parent?.let(q2::offer)
        }
        return points.size
    }
}

fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
