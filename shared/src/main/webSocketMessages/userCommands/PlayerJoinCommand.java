package webSocketMessages.userCommands;

import chess.ChessGame;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.JOIN_PLAYER;

public class PlayerJoinCommand extends GameCommand {
    public final ChessGame.TeamColor playerColor;

    public PlayerJoinCommand(String authToken, Integer gameID, ChessGame.TeamColor playerColor) {
        super(JOIN_PLAYER, authToken, gameID);
        this.playerColor = playerColor;
    }
}
