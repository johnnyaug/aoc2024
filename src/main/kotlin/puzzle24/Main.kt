package puzzle24

import kotlin.math.pow

enum class GateType {
    INPUT, AND, OR, XOR
}

data class Gate(val i1: String, val i2: String, val o: String, val t: GateType) {
    var xParent: String = ""
    fun apply(registers: MutableMap<String, Char>) {
        val val1 = registers.getValue(i1)
        val val2 = registers.getValue(i2)
        registers[o] = when (t) {
            GateType.AND -> if (val1 == '1' && val2 == '1') '1' else '0'
            GateType.OR -> if (val1 == '1' || val2 == '1') '1' else '0'
            GateType.XOR -> if (val1 != val2) '1' else '0'
            else -> return
        }
    }

    fun classify(gates: MutableMap<String, Gate>, gatesByInputs: MutableMap<String, MutableSet<Gate>>): String {
        val input1FromType = gates[i1]?.t
        val input2FromType = gates[i2]?.t
        val isTerminal = o.startsWith("z")
        val outputTypes = gatesByInputs[o]?.map { it.t }?.sorted()?.joinToString("#")
        return "%s|%s|%s|%s|%s".format(input1FromType, input2FromType, outputTypes, t, isTerminal)
    }

    companion object {
        fun classificationAfterFlip(
            g1: Gate, g2: Gate, gates: MutableMap<String, Gate>, gatesByInputs: MutableMap<String, MutableSet<Gate>>
        ): List<String> {
            return listOf(
                Gate(g1.i1, g1.i2, g2.o, g1.t).classify(gates, gatesByInputs),
                Gate(g2.i1, g2.i2, g1.o, g2.t).classify(gates, gatesByInputs)
            )
        }
    }
}

class Solution {
    private val registers: MutableMap<String, Char>
    private val gates: MutableMap<String, Gate>
    private val gatesByInputs: MutableMap<String, MutableSet<Gate>> = mutableMapOf()

    init {
        object {}.javaClass.getResourceAsStream("input.txt")!!.bufferedReader().run {
            useLines {
                it.fold(mutableMapOf<String, Char>() to mutableMapOf()) { acc: Pair<MutableMap<String, Char>, MutableMap<String, Gate>>, current ->
                    when {
                        ":" in current -> acc.first[current.slice(0..2)] = current[5]
                        current == "" -> Unit
                        else -> {
                            val spl = current.split(" ")
                            acc.second[spl[4]] = Gate(spl[0], spl[2], spl[4], GateType.valueOf(spl[1]))
                            gatesByInputs.compute(spl[0]) { _, v ->
                                (v ?: mutableSetOf()).also { gates -> gates.add(acc.second[spl[4]]!!) }
                            }
                            gatesByInputs.compute(spl[2]) { _, v ->
                                (v ?: mutableSetOf()).also { gates -> gates.add(acc.second[spl[4]]!!) }
                            }

                        }
                    }
                    acc
                }
            }
        }.let { (registers0, gates0) ->
            registers = registers0
            gates = gates0
            gates.putAll((1..44).map { idx ->
                listOf("x%s".format(idx.toString().padStart(2, '0')), "y%s".format(idx.toString().padStart(2, '0')))
            }.flatten().associateWith { Gate("", "", it, GateType.INPUT) })
        }
    }

    private fun calc(gate: Gate) {
        if (gate.i1 !in registers) {
            calc(gates[gate.i1]!!)
        }
        if (gate.i2 !in registers) {
            calc(gates[gate.i2]!!)
        }
        gate.apply(registers)
    }

    fun part1(): Long {
        gates.keys.filter { it.startsWith("z") }.forEach { calc(gates[it]!!) }
        return gates.keys.filter { it.startsWith("z") }.sorted().foldIndexed(0) { i, acc, reg ->
            if (registers[reg] == '0') acc
            else acc + 2.0.pow(i.toDouble()).toLong()
        }
    }

    private fun addParentName(gatesToLevel: Set<Gate>) {
        if (gatesToLevel.isEmpty()) {
            return
        }
        gatesToLevel.forEach {
            gatesByInputs[it.o]?.forEach { child ->
                child.xParent = if (child.xParent == "") it.xParent else child.xParent
            }
        }
        addParentName(gatesToLevel.mapNotNull {
            gatesByInputs[it.o]
        }.flatten().toSet())
    }

    fun part2(): String {
        addParentName(
            gates.filter { it.key.startsWith("x") }.values.onEach { it.xParent = it.o }.toSet()
        )
        val frequencies = gates.map { it.value.classify(gates, gatesByInputs) }.groupingBy { it }.eachCount()
        // find gates with strange "classifications" and see where they fit:
        val outliers = frequencies.filter { it.value < 3 }.keys
        val usuals = frequencies.filter { it.value > 5 }.keys
        val candidates = gates.filter { it.value.classify(gates, gatesByInputs) in outliers }.map { it.value }
        return candidates.asSequence().map { c1 ->
            candidates.mapNotNull { c2 ->
                if (usuals.containsAll(
                        Gate.classificationAfterFlip(
                            c1, c2, gates, gatesByInputs
                        )
                    )
                ) listOf(c1, c2) else null
            }
        }.flatten().filter { it[0].xParent == it[1].xParent }.flatten().map { it.o }.toSet().sorted().joinToString(",")
    }
}

fun main() {
    println(Solution().part1())
    println(Solution().part2())
}
