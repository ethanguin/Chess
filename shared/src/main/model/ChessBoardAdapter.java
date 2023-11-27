package model;

import chess.ChessBoard;
import chess.ChessBoardImpl;
import chess.ChessPiece;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ChessBoardAdapter implements JsonDeserializer<ChessBoard> {
    public ChessBoard deserialize(JsonElement el, Type type, JsonDeserializationContext ctx) throws JsonParseException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
        return gsonBuilder.create().fromJson(el, ChessBoardImpl.class);
    }
}
