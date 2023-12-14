package model;

import chess.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessMoveAdapter implements JsonDeserializer<ChessMove> {
    public ChessMove deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessPosition.class, new ChessPositionAdapter());
        gsonBuilder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        return gsonBuilder.create().fromJson(el, ChessMoveImpl.class);
    }
}
