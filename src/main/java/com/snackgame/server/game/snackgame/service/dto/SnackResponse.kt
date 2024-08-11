package com.snackgame.server.game.snackgame.service.dto


import com.snackgame.server.game.snackgame.snack.Snack

data class SnackResponse(
    val number: Int,
    val golden: Boolean
) {
    companion object {
        fun of(snacks: List<List<Snack>>): List<List<SnackResponse>> {
            return snacks.map { ofRow(it) }
        }

        private fun ofRow(snacks: List<Snack>): List<SnackResponse> {
            return snacks.map { SnackResponse(it.getNumber(), it.isGolden()) }
        }
    }
}
