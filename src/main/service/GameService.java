package service;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import model.GameData;
import model.SessionData;
import req_Res.GameRequest;
import req_Res.GameResponse;

/**
 * <code>GameService</code> service for requests relating to game data
 */
public class GameService {
    /**
     * <code>createGame</code> Creates a new game.
     *
     * @param gameRequest - includes an authToken to provide authentication and the gameName to provide to the new game
     * @return GameResponse - returns the gameID of the new game if successful. If unsuccessful, it includes an error message
     */
    public static GameResponse createGame(GameRequest gameRequest) {
        DataAccess dao = new SQLDataAccess();
        
        if (gameRequest.getGameName() == null || gameRequest.getGameName().isEmpty()) {
            return new GameResponse("Error: bad request");
        }
        GameData newGame = new GameData(gameRequest.getGameName());
        SessionData session = new SessionData();
        session.setAuthToken(gameRequest.getAuthToken());
        try {
            if (dao.findSession(session) == null) {
                return new GameResponse("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            return new GameResponse(e.getMessage());
        }

        try {
            dao.createGame(newGame);
            GameResponse gameResponse = new GameResponse();
            gameResponse.setGameID(newGame.getGameID());
            return gameResponse;
        } catch (DataAccessException e) {
            return new GameResponse(e.getMessage());
        }
    }

    /**
     * <code>joinGame</code> Verifies that a specific game exists and adds the user to the game as the specified color if possible. If no color is specified in the request, they are added as a viewer
     *
     * @param gameRequest - includes the authToken, player color, and gameID
     * @return GameResponse - returns an empty response if successful. If unsuccessful, it includes an error message
     */
    public static GameResponse joinGame(GameRequest gameRequest) {
        String playerColor = gameRequest.getPlayerColor();
        DataAccess dao = new SQLDataAccess();

        try {
            if (dao.findGame(gameRequest.getGameID()) == null) {
                return new GameResponse("Error: bad request");
            }
        } catch (DataAccessException e) {
            return new GameResponse(e.getMessage());
        }

        SessionData session = new SessionData();
        session.setAuthToken(gameRequest.getAuthToken());
        SessionData foundSession;
        try {
            foundSession = dao.findSession(session);
            if (foundSession == null) {
                return new GameResponse("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            return new GameResponse(e.getMessage());
        }
        if (playerColor == null) {
            try {
                dao.findGame(gameRequest.getGameID()).addWatcher(foundSession.getUsername());
                return new GameResponse();
            } catch (DataAccessException e) {
                return new GameResponse(e.getMessage());
            }
        }
        try {
            dao.claimGameSpot(foundSession.getUsername(), playerColor, new GameData(gameRequest.getGameID()));
        } catch (DataAccessException e) {
            return new GameResponse(e.getMessage());
        }
        return new GameResponse();
    }

    /**
     * <code>listGames</code> Gives a list of all the games ever created
     *
     * @param authToken - the authToken of the user trying to view the games
     * @return GameResponse - a response that includes list of all the games or an error message
     */
    public static GameResponse listGames(String authToken) {
        DataAccess dao = new SQLDataAccess();

        SessionData session = new SessionData();
        session.setAuthToken(authToken);
        try {
            if (dao.findSession(session) == null) {
                return new GameResponse("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            return new GameResponse(e.getMessage());
        }
        try {
            GameResponse gameResponse = new GameResponse();
            gameResponse.setGameList(dao.findAllGames());
            return gameResponse;
        } catch (DataAccessException e) {
            return new GameResponse(e.getMessage());
        }
    }
}
