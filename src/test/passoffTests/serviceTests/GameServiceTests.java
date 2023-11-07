package passoffTests.serviceTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import dataAccess.SQLDataAccess;
import model.GameData;
import model.SessionData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import req_Res.GameRequest;
import req_Res.GameResponse;
import service.GameService;

public class GameServiceTests {
    UserData testUser = new UserData("ethan", "password123", "me@me.org");
    SessionData testSession = new SessionData(testUser.getUsername());

    @Test
    public void successfulCreateGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        dao.createSession(testSession);
        GameRequest gameRequest = new GameRequest();
        gameRequest.setGameName("newGame");
        gameRequest.setAuthToken(testSession.getAuthToken());

        GameResponse response = GameService.createGame(gameRequest);

        Assertions.assertNotNull(dao.findGame(response.getGameID()));
        Assertions.assertNull(response.getMessage());
    }

    @Test
    public void createGameNoAuth() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);

        GameRequest gameRequest = new GameRequest();
        gameRequest.setGameName("newGame");

        GameResponse response = GameService.createGame(gameRequest);

        Assertions.assertEquals("Error: unauthorized", response.getMessage());
    }

    @Test
    public void successfulJoinGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        dao.createSession(testSession);
        GameData game = new GameData("newGame");
        dao.createGame(game);

        GameRequest request = new GameRequest();
        request.setGameID(game.getGameID());
        request.setAuthToken(testSession.getAuthToken());
        request.setPlayerColor("white");

        GameResponse response = GameService.joinGame(request);

        Assertions.assertNull(response.getMessage());
        Assertions.assertEquals(testUser.getUsername(), dao.findGame(game.getGameID()).getWhiteUsername());
    }

    @Test
    public void joinGameNoAuth() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        GameData game = new GameData("newGame");
        dao.createGame(game);

        GameRequest request = new GameRequest();
        request.setGameID(game.getGameID());
        request.setPlayerColor("white");

        GameResponse response = GameService.joinGame(request);

        Assertions.assertEquals("Error: unauthorized", response.getMessage());
    }

    @Test
    public void successfulListGames() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        dao.createSession(testSession);
        GameData game = new GameData("newGame");
        dao.createGame(game);

        GameResponse response = GameService.listGames(testSession.getAuthToken());

        Assertions.assertNull(response.getMessage());
    }

    @Test
    public void listGamesNoAuth() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        GameData game = new GameData("newGame");
        dao.createGame(game);

        GameResponse response = GameService.listGames(testSession.getAuthToken());

        Assertions.assertEquals("Error: unauthorized", response.getMessage());
    }

}
