package req_Res;

/**
 * <code>GameRequest</code> a request object that holds data relevant to the HTTP handlers for creating, joining, or listing a game
 */

public class GameRequest {
    /**
     * <code>authToken</code> the authToken submitted when making the request (linked to a specific user's login session)
     */
    private String authToken;
    /**
     * <code>gameName</code> the game name of the game being created, joined, or listed
     */
    private String gameName;
    /**
     * <code>playerColor</code> the player color that the user is requesting to take when joining
     */
    private String playerColor;
    /**
     * <code>gameID</code> the gameID of the game being created, joined, or listed
     */
    private String gameID;

    public GameRequest() {
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public String getGameID() {
        return gameID;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }
}
