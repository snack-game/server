package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.snackgame.snack.PlainSnack
import com.snackgame.server.game.snackgame.snack.Snack
import java.util.concurrent.ThreadLocalRandom

fun createRandomized(height: Int, width: Int): MutableList<MutableList<Snack>> {
    if (allPositive(height, width)) {
        val snacks: MutableList<MutableList<Snack>> = createRows(height, width)
        goldenOneIn(snacks)
        return snacks
    }
    return arrayListOf()
}

private fun createRows(height: Int, width: Int): MutableList<MutableList<Snack>> {
    val snacks: MutableList<MutableList<Snack>> = arrayListOf()
    for (i in 0 until height) {
        snacks.add(createRow(width))
    }

    return snacks
}

private fun createRow(width: Int): MutableList<Snack> {
    val row: MutableList<Snack> = arrayListOf()
    for (i in 0 until width) {
        row.add(PlainSnack.ofRandomizedNumber())

    }
    return row
}

private fun goldenOneIn(snacks: MutableList<MutableList<Snack>>) {
    val row = pickIndexIn(snacks.size)
    val column = pickIndexIn(snacks[row].size)
    val picked = snacks[row][column]
    snacks[row][column] = picked.golden()
}

private fun pickIndexIn(size: Int): Int {
    return ThreadLocalRandom.current().nextInt(size)
}

private fun allPositive(vararg numbers: Int): Boolean {
    return numbers.all { it > 0 }
}