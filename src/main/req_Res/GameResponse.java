package req_Res;

import model.GameData;

import java.util.Collection;
import java.util.Objects;

/**
 * <code>GameResponse</code> a response object that holds data relevant to the HTTP handlers for creating, joining, or listing a game
 */
public class GameResponse {
    /**
     * <code>message</code> the error message that is returned if response is a failure response
     */
    private String message;
    /**
     * <code>gameID</code> the gameID of the game created, joined, or found
     */
    private Integer gameID;

    /**
     * the list of games for listGames endpoint
     */
    private Collection<GameData> gameList;

    public GameResponse() {
    }

    public GameResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public Collection<GameData> getGameList() {
        return gameList;
    }

    public void setGameList(Collection<GameData> gameList) {
        this.gameList = gameList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameResponse that = (GameResponse) o;
        if (message != null && that.message != null) {
            if (!message.equals(that.message)) {
                return false;
            }
        } else if (!(message == null && that.message == null)) {
            return false;
        }
        return gameID.equals(that.gameID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(message, gameID);
    }
}
