package com.snackgame.server.game.snackgame.core.domain.item


import org.springframework.core.convert.converter.Converter

class StringToItemTypeConverter : Converter<String, ItemType> {
    override fun convert(source: String): ItemType {
        return ItemType.valueOf(source.uppercase())
    }

}