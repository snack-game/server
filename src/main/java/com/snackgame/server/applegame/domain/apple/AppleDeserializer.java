package com.snackgame.server.applegame.domain.apple;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.IntNode;

public class AppleDeserializer extends StdDeserializer<Apple> {

    public AppleDeserializer() {
        super(Apple.class);
    }

    @Override
    public Apple deserialize(JsonParser p, DeserializationContext context) throws IOException {
        TreeNode node = p.readValueAsTree();
        return resolve(node);
    }

    private Apple resolve(TreeNode node) {
        int number = ((IntNode)node.get("number")).intValue();
        boolean isGolden = ((BooleanNode)node.get("isGolden")).booleanValue();

        if (number == 0) {
            return EmptyApple.get();
        }
        if (isGolden) {
            return GoldenApple.of(number);
        }
        return PlainApple.of(number);
    }
}
