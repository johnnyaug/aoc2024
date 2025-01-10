package puzzle15

enum class Dir(val y: Int, val x: Int) {
    U(-1, 0), D(1, 0), L(0, -1), R(0, 1)
}

data class Point(val y: Int, val x: Int) {
    operator fun plus(d: Dir) = copy(y = y + d.y, x = x + d.x)
}

enum class ElementType {
    BOX_LEFT, BOX_RIGHT, SPACE, BORDER, BOX, PLAYER
}

class Game(private val bigBoxes: Boolean, private val l: List<List<Char>>) {
    val s: MutableMap<Point, ElementType> = mutableMapOf()

    init {
        l.forEachIndexed { i, row ->
            row.forEachIndexed char@{ j, c ->
                s[Point(i, j)] = mapOf(
                    '[' to ElementType.BOX_LEFT, ']' to ElementType.BOX_RIGHT, 'O' to ElementType.BOX, '@' to ElementType.PLAYER
                )[c] ?: return@char
            }
        }
    }

    operator fun get(p: Point): ElementType {
        return s[p] ?: if (p.y >= l.size || p.y < 0 || p.x >= l[0].size || p.x < 0 || l[p.y][p.x] == '#')
            ElementType.BORDER
        else return ElementType.SPACE
    }

    fun move(p: Point, d: Dir, dryRun: Boolean): Boolean {
        if (d in listOf(Dir.L, Dir.R)) {
            val dst = if (bigBoxes && get(p) != ElementType.PLAYER) p + d + d else p + d
            if (get(dst) == ElementType.BORDER) {
                return false
            }
            if (get(dst) == ElementType.SPACE || move(dst, d, dryRun)) {
                if (!dryRun) {
                    s[p + d] = s.remove(p)!!
                    if (bigBoxes && s[p + d] != ElementType.PLAYER) {
                        s[dst] = if (s[p + d] == ElementType.BOX_LEFT) ElementType.BOX_RIGHT else ElementType.BOX_LEFT
                    }
                }
                return true
            }
            return false
        }
        val neighbor1 = p + d
        var neighbor2: Point? = null
        if (bigBoxes && get(p) != ElementType.PLAYER) {
            neighbor2 = if (get(p) == ElementType.BOX_RIGHT) neighbor1 + Dir.L else neighbor1 + Dir.R
        }
        if (get(neighbor1) == ElementType.BORDER || (neighbor2 != null && get(neighbor2) == ElementType.BORDER)) {
            return false
        }
        if ((get(neighbor1) == ElementType.SPACE || move(
                neighbor1, d, dryRun
            )) && (neighbor2 == null || get(neighbor2) == ElementType.SPACE || move(neighbor2, d, dryRun))
        ) {
            if (!dryRun) {
                s[neighbor1] = s.remove(p)!!
                if (neighbor2 != null) {
                    s[neighbor2] = s.remove(if (s[neighbor1] == ElementType.BOX_RIGHT) p + Dir.L else p + Dir.R)!!
                }
            }
            return true
        }
        return false
    }
}

class Solution(bigBoxes: Boolean) {
    private var pos: Point
    private val instructions: List<Dir>
    private val game: Game

    init {
        object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
            val l = it.toList()
            val splitIdx = l.indexOf("")
            val board = if (bigBoxes) l.slice(0..<splitIdx).map { row ->
                row.map { c -> mapOf('.' to "..", '@' to "@.", 'O' to "[]", '#' to "##")[c]!!.toCharArray().toList() }
                    .flatten()
            }.toList()
            else l.slice(0..<splitIdx).map { row -> row.toMutableList() }
            game = Game(bigBoxes, board)
            instructions = l.slice(splitIdx + 1..<l.size).map { row ->
                row.mapNotNull { c -> mapOf('^' to Dir.U, 'v' to Dir.D, '>' to Dir.R, '<' to Dir.L)[c] }
            }.flatten()
            pos = board.indices.firstNotNullOf { i ->
                if (board[i].indexOf('@') > -1) Point(i, board[i].indexOf('@')) else null
            }
        }
    }

    fun play(): Int {
        instructions.forEach { if (game.move(pos, it, true) && game.move(pos, it, false)) pos += it }
        return game.s.filterValues { it == ElementType.BOX || it == ElementType.BOX_LEFT }.keys.sumOf { p -> 100 * p.y + p.x }
    }
}

fun main() {
    println(Solution(false).play())
    println(Solution(true).play())
}
