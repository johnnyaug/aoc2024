package puzzle17

import kotlin.math.pow

data class Instruction(val code: Int, val op: Operand)

data class Operand(val opcode: Int) {
    fun asCombo(registers: List<Long>): Long {
        return when (opcode) {
            in 0..3 -> opcode.toLong()
            else -> registers[opcode - 4]
        }
    }

    fun asLiteral(): Int {
        return opcode
    }
}

class Solution {

    private val opcodes: List<Int>
    private val registers: MutableList<Long>
    private val program: List<Instruction>
    private var pointer: Int = 0

    init {
        object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().useLines {
            val l = it.toList()
            registers = l.slice(0..2).map { register -> register.substringAfter(": ").toLong() }.toMutableList()
            opcodes = l[4].substringAfter(": ").split(",").map(String::toInt)
            program = opcodes.foldIndexed(mutableListOf()) { idx, acc: MutableList<Instruction>, value ->
                if (idx.mod(2) == 0) acc.add(Instruction(value, Operand(-1)))
                else acc[acc.size - 1] = acc.last().copy(op = Operand(value))
                acc
            }
        }
    }


    private fun exec(inst: Instruction, outputs: MutableList<Int>) {
        when (inst.code) {
            0 -> {
                registers[0] =
                    (registers[0].toDouble() / 2.toDouble().pow(inst.op.asCombo(registers).toDouble())).toLong()
            }

            1 -> {
                registers[1] = registers[1] xor inst.op.asLiteral().toLong()
            }

            2 -> {
                registers[1] = inst.op.asCombo(registers).mod(8).toLong()
            }

            3 -> {
                if (registers[0] != 0L) {
                    pointer = inst.op.asLiteral()
                    return
                }
            }

            4 -> {
                registers[1] = registers[1] xor registers[2]
            }

            5 -> {
                outputs.add(inst.op.asCombo(registers).mod(8))
            }

            6 -> {
                registers[1] = registers[0] / 2.toDouble().pow(inst.op.asCombo(registers).toDouble()).toLong()
            }

            7 -> {
                registers[2] = registers[0] / 2.toDouble().pow(inst.op.asCombo(registers).toDouble()).toLong()
            }
        }
        pointer++
    }

    private fun run(): List<Int> {
        pointer = 0
        val outputs: MutableList<Int> = mutableListOf()
        while (pointer < program.size) exec(program[pointer], outputs)
        return outputs
    }

    fun part1(): String {
        return run().joinToString(",")
    }

    private fun base8ToLong(base8Input: List<Int>): Long {
        return base8Input.foldIndexed(0L) { idx, acc, value ->
            acc + value * 8.0.pow(idx).toLong()
        }
    }

    private fun solve(registerA: MutableList<Int>, digitIdx: Int): Boolean {
        for (guess in 0..7) {
            registerA[digitIdx] = guess
            registers[0] = base8ToLong(registerA)
            val doneSize = 16 - digitIdx
            if (run().takeLast(doneSize) == opcodes.takeLast(doneSize))
                if (digitIdx == 0 || solve(registerA, digitIdx - 1))
                    return true
        }
        return false
    }

    fun part2(): Long {
        val registerA = List(16) { 0 }.toMutableList()
        solve(registerA, 15)
        return base8ToLong(registerA)
    }
}

fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
