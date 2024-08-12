package com.snackgame.server.game.snackgame.domain

import com.snackgame.server.game.snackgame.exception.InvalidBoardSizeException
import com.snackgame.server.game.snackgame.exception.InvalidCoordinateException
import com.snackgame.server.game.snackgame.exception.SnackNotRemovableException
import com.snackgame.server.game.snackgame.snack.EmptySnack
import com.snackgame.server.game.snackgame.snack.Snack
import java.util.stream.Collectors

class Board() {
    private var snacks: MutableList<MutableList<Snack>> = arrayListOf()

    constructor(snacks: MutableList<MutableList<Snack>>) : this() {
        validateIsRectangle(snacks)
        this.snacks = snacks.map { ArrayList(it) }.toMutableList()
    }

    constructor(height: Int, width: Int) : this(createRandomized(height, width))

    private fun validateIsRectangle(snacks: List<List<Snack>>) {
        if (snacks.isEmpty() || snacks[0].isEmpty()) {
            throw InvalidBoardSizeException()
        }
    }

    fun reset(): MutableList<MutableList<Snack>> {
        return createRandomized(getHeight(), getWidth())
    }

    fun removeSnacksIn(streak: Streak): List<Snack> {
        validateIsIncluded(streak.toCoordinates())
        validateSumOf(streak.toCoordinates())

        return streak.toCoordinates().stream().map(this::removeSnacksAt).filter(Snack::exists)
            .collect(Collectors.toList())
    }

    private fun validateIsIncluded(coordinates: List<Coordinate>) {
        coordinates.forEach { coordinate ->
            if (coordinate.y >= snacks.size || coordinate.x >= snacks[0].size) {
                throw InvalidCoordinateException()
            }
        }
    }

    private fun validateSumOf(coordinates: List<Coordinate>) {
        if (sumSnacksIn(coordinates) != REMOVABLE_SUM) {
            throw SnackNotRemovableException("스낵들의 합이 " + REMOVABLE_SUM + "이 아닙니다")
        }
    }

    private fun sumSnacksIn(coordinates: List<Coordinate>): Int {
        return getSnacksIn(coordinates).stream().map(Snack::getNumber).reduce(0, Integer::sum)

    }

    private fun getSnacksIn(coordinates: List<Coordinate>): List<Snack> {
        return coordinates.stream().map { coordinate -> snacks[coordinate.y][coordinate.x] }
            .collect(Collectors.toList())
    }

    private fun removeSnacksAt(coordinate: Coordinate): Snack {
        val row = snacks[coordinate.y]
        return row.set(coordinate.x, EmptySnack.get())
    }

    fun getSnacks(): List<List<Snack>> {
        return snacks.map { ArrayList(it) }
    }


    private fun getHeight(): Int {
        return this.snacks.size
    }

    private fun getWidth(): Int {
        return this.snacks[0].size
    }

    companion object {
        private const val REMOVABLE_SUM = 10;
    }
}