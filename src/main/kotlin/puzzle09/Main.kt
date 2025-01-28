package puzzle09

fun sumSequence(first: Long, l: Long): Long {
    return (2 * first + l - 1) * l / 2
}

fun part1(): Long {
    var res = 0L
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().forEachLine {
        val diskMap = it.map { it.digitToInt() }
        var (usedLeft, usedRight) = 0L to 0L
        var nextBlock = 0L
        var (i, j) = 0 to diskMap.size - 1
        while (i <= j) {
            val (leftItem, rightItem) = diskMap[i] to diskMap[j]
            var l: Long = leftItem - usedLeft
            var fileID: Int
            if (i.mod(2) == 0) {
                fileID = i / 2
                usedLeft = 0
                i++
            } else if (leftItem - usedLeft < rightItem - usedRight) {
                fileID = j / 2
                usedRight += l
                usedLeft = 0
                i++

            } else {
                l = rightItem - usedRight
                fileID = j / 2
                usedLeft += l
                usedRight = 0
                j -= 2
            }
            res += fileID * sumSequence(nextBlock, l)
            nextBlock += l
        }
    }
    return res
}

data class Node(var length: Int, var fileID: Long?, var next: Node?, var prev: Node?, val idx: Int)

fun part2(): Long {
    var res = 0L
    object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().forEachLine {
        val data = it.map { it.digitToInt() }
        val head = Node(data[0], 0L, null, null, 0)
        var current = head
        var tail = head
        data.forEachIndexed { i, n ->
            if (i == 0)
                return@forEachIndexed
            tail = Node(n, if (i.mod(2) == 0) i.toLong() / 2 else null, null, current, i)
            current.next = tail
            current = tail
        }

        var right: Node? = tail
        while (right != null) {
            var left: Node? = head
            while (left != null && left.idx < right.idx) {
                if (left.fileID != null || right.length > left.length) {
                    left = left.next
                    continue
                }
                val diff = left.length - right.length
                left.length = right.length
                left.fileID = right.fileID
                if (diff > 0) {
                    left.next = Node(diff, null, left.next, left, left.idx)
                }
                right.fileID = null
                break
            }
            right = right.prev
            while (right != null && right.fileID == null)
                right = right.prev

        }
        var left: Node? = head
        var nextBlock = 0L
        while (left != null) {
            if (left.fileID != null) {
                res += left.fileID!! * (sumSequence(nextBlock, left.length.toLong()))
            }
            nextBlock += left.length
            left = left.next
        }
    }

    return res
}

fun main() {
    println(part1())
    println(part2())
}
