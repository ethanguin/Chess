package model;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessGameAdapter implements JsonDeserializer<ChessGame> {
    public ChessGame deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
        return gsonBuilder.create().fromJson(el, ChessGameImpl.class);
    }
}
