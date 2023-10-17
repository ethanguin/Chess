package service;

import model.GameData;
import req_Res.GameRequest;
import req_Res.GameResponse;

import java.util.Collection;
import java.util.HashSet;

/**
 * <code>GameService</code> service for requests relating to game data
 */
public class GameService {
    /**
     * <code>createGame</code> Creates a new game.
     *
     * @param game - includes an authToken to provide authentication and the gameName to provide to the new game
     * @return GameResponse - returns the gameID of the new game if successful. If unsuccessful, it includes an error message
     */
    public GameResponse createGame(GameRequest game) {
        return new GameResponse();
    }

    /**
     * <code>joinGame</code> Verifies that a specific game exists and adds the user to the game as the specified color if possible. If no color is specified in the request, they are added as a viewer
     *
     * @param game - includes the authToken, player color, and gameID
     * @return GameResponse - returns an empty response if successful. If unsuccessful, it includes an error message
     */
    public GameResponse joinGame(GameRequest game) {
        return new GameResponse();
    }

    /**
     * <code>listGames</code> Gives a list of all the games ever created
     *
     * @param authToken - the authToken of the user trying to view the games
     * @return Collection - a list of all the games
     */
    public Collection<GameData> listGames(String authToken) {
        return new HashSet<>();
    }
}
