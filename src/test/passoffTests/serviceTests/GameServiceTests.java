package passoffTests.serviceTests;

import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.MemoryDataAccess;
import model.SessionData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import req_Res.GameRequest;
import req_Res.GameResponse;
import service.GameService;
import spark.Session;

public class GameServiceTests {
    UserData testUser = new UserData("ethan", "password123", "me@me.org");
    SessionData testSession = new SessionData(testUser.getUsername());

    @Test
    public void successfulCreateGame() throws DataAccessException {
        DataAccess dao = new MemoryDataAccess();
        dao.clear();

        dao.createUser(testUser);
        dao.createSession(testSession);
        GameRequest gameRequest = new GameRequest();
        gameRequest.setGameName("newGame");
        gameRequest.setAuthToken(testSession.getAuthToken());

        GameResponse response = GameService.createGame(gameRequest);

        Assertions.assertNotNull(response.getGameID());
    }
}
