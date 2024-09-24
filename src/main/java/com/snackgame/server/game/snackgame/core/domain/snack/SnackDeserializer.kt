package com.snackgame.server.game.snackgame.core.domain.snack

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.databind.node.BooleanNode
import com.fasterxml.jackson.databind.node.IntNode

class SnackDeserializer : StdDeserializer<Snack>(Snack.javaClass) {

    override fun deserialize(p: JsonParser, context: DeserializationContext): Snack {
        val node = p.readValueAsTree<JsonNode>()
        return resolve(node)
    }

    private fun resolve(node: JsonNode): Snack {
        val number = (node.get("number") as IntNode).intValue()
        val isGolden = (node.get("isGolden") as BooleanNode).booleanValue()

        return when {
            number == 0 -> EmptySnack.get()
            isGolden -> GoldenSnack.of(number)
            else -> PlainSnack.of(number)
        }
    }
}
