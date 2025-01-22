package puzzle22

import java.util.*

const val cycleSize = 0xFFFFFF

class Solution {
    private val buyers = object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().run {
        useLines {
            it.map { s -> s.toInt() }.toList()
        }
    }.toSet()

    fun next(secret: Int): Int {
        var res = (secret xor (secret shl 6)) and 0xFFFFFF
        res = (res xor (res shr 5)) and 0xFFFFFF
        res = (res xor (res shl 11)) and 0xFFFFFF
        return res
    }

    fun part1(): Long {
        val cycle: MutableList<Int> = mutableListOf()
        var current: Int? = null
        val cycleMap: MutableMap<Int, Int> = mutableMapOf()
        for (i in 0..cycleSize) {
            cycle.add(next(current ?: 1).also { current = it })
            cycleMap[current!!] = i
        }
        return buyers.sumOf { b ->
            cycle[(cycleMap[b]!! + 2000).mod(0xFFFFFF)].toLong()
        }
    }

    fun part2(): Int {
        val lastFiveNumbers = LinkedList<Int>()
        val activeBuyers = LinkedList<Int>()
        val seqBuyers: MutableMap<List<Int>, MutableSet<Int>> = mutableMapOf()
        val seqToValue: MutableMap<List<Int>, Int> = mutableMapOf()
        repeat(4) {
            lastFiveNumbers.offer(next(lastFiveNumbers.peek() ?: 1))
        }
        var n = lastFiveNumbers.peek()
        for (i in 0..cycleSize) {
            n = next(n)
            lastFiveNumbers.addLast(n)
            if (lastFiveNumbers.peekFirst() in buyers) {
                activeBuyers.addLast(i)
            }
            while (activeBuyers.size != 0 && activeBuyers.peekFirst() + 1997 <= i) {
                activeBuyers.removeFirst()
            }
            if (activeBuyers.size != 0) {
                val diffs = lastFiveNumbers.zipWithNext { a, b -> b.mod(10) - a.mod(10) }
                activeBuyers.filter {
                    diffs !in seqBuyers || it !in seqBuyers.getValue(diffs)
                }.let { relevantBuyers ->
                    seqToValue[diffs] = seqToValue.getOrDefault(diffs, 0) + relevantBuyers.size * n.mod(10)
                    seqBuyers[diffs] = seqBuyers.getOrDefault(diffs, mutableSetOf()).also { it.addAll(relevantBuyers) }
                }
            }
            lastFiveNumbers.poll()
        }
        return seqToValue.values.max()
    }
}

fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
