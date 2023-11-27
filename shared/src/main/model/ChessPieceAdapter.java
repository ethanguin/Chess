package model;

import chess.ChessPiece;
import chess.ChessPieceImpl;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessPieceAdapter implements JsonDeserializer<ChessPiece> {
    public ChessPiece deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        return new Gson().fromJson(el, ChessPieceImpl.class);
    }
}
