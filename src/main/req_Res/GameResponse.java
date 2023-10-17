package req_Res;

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
    private String gameID;

    public GameResponse() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
}
