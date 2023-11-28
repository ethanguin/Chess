package serverFacadeTests;

import chess.ChessGame;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import model.GameData;
import model.SessionData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import req_Res.*;
import service.GameService;
import service.SessionService;
import service.UserService;
import ui.ServerFacade;

public class ServerFacadeTests {
    final UserData testUser = new UserData("ethan", "password123", "me@me.org");
    final SessionData testSession = new SessionData(testUser.getUsername());
    final UserData failUser = new UserData("john", "", "me@me.org");
    final String authToken = "12345";
    final ServerFacade server = new ServerFacade("http://localhost:8080");

    @Test
    public void successfulClear() throws Exception {
        //add in data into the dao and use the server facade to clear it
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);
        Assertions.assertEquals(dao.findUser(testUser), testUser);

        server.clear();
        Assertions.assertNull(dao.findUser(testUser));
    }

    @Test
    public void successfulCreateGame() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        dao.createSession(testSession);
        GameRequest gameRequest = new GameRequest();
        gameRequest.setGameName("newGame");
        gameRequest.setAuthToken(testSession.getAuthToken());

        GameResponse response = server.createGame(gameRequest.getAuthToken(), gameRequest.getGameName());

        Assertions.assertNotNull(dao.findGame(response.getGameID()));
        Assertions.assertNull(response.getMessage());
    }

    @Test
    public void createGameNoAuth() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);

        GameRequest gameRequest = new GameRequest();
        gameRequest.setGameName("newGame");

        Assertions.assertThrows(ResponseException.class, () -> server.createGame(gameRequest.getAuthToken(), gameRequest.getGameName()));
    }

    @Test
    public void successfulJoinGame() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        dao.createSession(testSession);
        GameData game = new GameData("newGame");
        dao.createGame(game);

        GameRequest request = new GameRequest();
        request.setGameID(game.getGameID());
        request.setAuthToken(testSession.getAuthToken());
        request.setPlayerColor("WHITE");

        GameData response = server.joinGame(request.getAuthToken(), request.getGameID(), ChessGame.TeamColor.valueOf(request.getPlayerColor()));

        Assertions.assertEquals(testUser.getUsername(), dao.findGame(game.getGameID()).getWhiteUsername());
    }

    @Test
    public void joinGameNoAuth() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        GameData game = new GameData("newGame");
        dao.createGame(game);

        GameRequest request = new GameRequest();
        request.setGameID(game.getGameID());
        request.setPlayerColor("white");

        GameResponse response = GameService.joinGame(request);

        Assertions.assertThrows(ResponseException.class, () -> server.joinGame(request.getAuthToken(), request.getGameID(), ChessGame.TeamColor.WHITE));
    }

    @Test
    public void successfulListGames() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        dao.createSession(testSession);
        GameData game = new GameData("newGame");
        dao.createGame(game);

        GameData[] response = server.listGames(testSession.getAuthToken());

        Assertions.assertNotNull(response);
        Assertions.assertNotNull(dao.findGame(game.getGameID()));
        Assertions.assertEquals(dao.findGame(game.getGameID()), response[0]);
    }

    @Test
    public void listGamesNoAuth() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        GameData game = new GameData("newGame");
        dao.createGame(game);

        GameResponse response = GameService.listGames(testSession.getAuthToken());

        Assertions.assertThrows(ResponseException.class, () -> server.listGames(testSession.getAuthToken()));
    }

    @Test
    public void successfulLogin() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);

        SessionResponse response = server.login(testUser.getUsername(), testUser.getPassword());
        SessionData session = new SessionData(testUser.getUsername());
        session.setAuthToken(response.getAuthToken());

        Assertions.assertNotNull(dao.findSession(session));
    }

    @Test
    public void wrongPasswordLogin() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);

        Assertions.assertThrows(ResponseException.class, () -> server.login(testUser.getUsername(), failUser.getPassword()));
    }

    @Test
    public void successfulLogout() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);
        SessionData session = new SessionData(authToken, testUser.getUsername());
        dao.createSession(session);
        Assertions.assertNotNull(dao.findSession(session));

        server.logout(authToken);

        Assertions.assertNull(dao.findSession(session));
    }

    @Test
    public void wrongAuthLogout() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);
        SessionData session = new SessionData(authToken, testUser.getUsername());
        dao.createSession(session);
        SessionData wrongSession = new SessionData("fish123", testUser.getUsername());

        Assertions.assertThrows(ResponseException.class, () -> server.logout(wrongSession.getAuthToken()));
    }

    @Test
    public void successfulCreateUser() throws Exception {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        server.register(testUser.getUsername(), testUser.getPassword(), testUser.getEmail());

        Assertions.assertEquals(testUser, dao.findUser(testUser));
    }

    @Test
    public void createUserNoData() {
        Assertions.assertThrows(ResponseException.class, () -> server.register("", "", ""));
    }
}
