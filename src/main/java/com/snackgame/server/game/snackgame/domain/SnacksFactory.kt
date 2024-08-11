package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.snackgame.snack.PlainSnack
import com.snackgame.server.game.snackgame.snack.Snack
import java.util.concurrent.ThreadLocalRandom

fun createRandomized(width: Int, height: Int): MutableList<MutableList<Snack>> {
    if (allPositive(width, height)) {
        val snacks: MutableList<MutableList<Snack>> = createRows(width, height)
        goldenOneIn(snacks)
        return snacks
    }
    return arrayListOf()
}

private fun createRows(width: Int, height: Int): MutableList<MutableList<Snack>> {
    val snacks: MutableList<MutableList<Snack>> = arrayListOf()
    for (i in 0..height) {
        snacks.add(createRow(width))
    }

    return snacks
}

private fun createRow(width: Int): MutableList<Snack> {
    val row: MutableList<Snack> = arrayListOf()
    for (i in 0..width) {
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