package model;

import chess.ChessGame;
import chess.ChessGameImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * <code>GameData</code> class is the object that stores any data related to a specific game
 */

public class GameData {
    /**
     * a unique numerical ID that identifies a game
     */
    private int gameID;

    /**
     * a name given to a game, doesn't have to be unique
     */
    private String gameName;

    /**
     * the username of the player with the white pieces
     */
    private String whiteUsername;

    /**
     * the username of the player with the black pieces
     */
    private String blackUsername;

    /**
     * the list of usernames of the users watching the game
     */
    private Collection<String> watchers = new ArrayList<>();

    /**
     * the Chess Game assigned to the game, including pieces, chess board, and positions
     */
    private ChessGame game = new ChessGameImpl();

    public GameData(int gameID, String gameName, String whiteUsername, String blackUsername, ChessGame game) {
        this.gameID = gameID;
        this.gameName = gameName;
        this.whiteUsername = whiteUsername;
        this.blackUsername = blackUsername;
        this.game = game;
    }

    public GameData() {
    }

    public GameData(int gameID) {
        this.gameID = gameID;
    }

    public GameData(String gameName) {
        this.gameName = gameName;
        this.gameID = RandomNum.newNum();
    }

    public int getGameID() {
        return gameID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getWhiteUsername() {
        return whiteUsername;
    }

    public void setWhiteUsername(String whiteUsername) {
        this.whiteUsername = whiteUsername;
    }

    public String getBlackUsername() {
        return blackUsername;
    }

    public void setBlackUsername(String blackUsername) {
        this.blackUsername = blackUsername;
    }

    public void addWatcher(String username) {
        watchers.add(username);
    }

    public ChessGame getGame() {
        return game;
    }

    public void setGame(ChessGame game) {
        this.game = game;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameData gameData = (GameData) o;
        if (whiteUsername != null && gameData.whiteUsername != null) {
            if (!whiteUsername.equals(gameData.whiteUsername)) {
                return false;
            }
        } else if (!(whiteUsername == null && gameData.whiteUsername == null)) {
            return false;
        }
        if (blackUsername != null && gameData.blackUsername != null) {
            if (!blackUsername.equals(gameData.blackUsername)) {
                return false;
            }
        } else if (!(blackUsername == null && gameData.blackUsername == null)) {
            return false;
        }
        return gameID == gameData.gameID && gameName.equals(gameData.gameName) && watchers.equals(gameData.watchers) && game.equals(gameData.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameID, gameName, whiteUsername, blackUsername, watchers, game);
    }
}
