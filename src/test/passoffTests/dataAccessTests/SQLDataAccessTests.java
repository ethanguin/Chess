package passoffTests.dataAccessTests;

import chess.*;
import com.google.gson.Gson;
import dataAccess.DataAccess;
import dataAccess.DataAccessException;
import dataAccess.SQLDataAccess;
import model.GameData;
import model.SessionData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

public class SQLDataAccessTests {
    final UserData testUser = new UserData("ethan", "password", "me@me.org");
    final String authToken = "abc1234";
    final SessionData testSession = new SessionData(authToken, testUser.getUsername());

    final GameData testGame = new GameData(1234, "gameTest", null, null, new ChessGameImpl());

    @Test
    public void successfulClear() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);
        Assertions.assertNotNull(dao.findUser(testUser));

        dao.clear();
        Assertions.assertNull(dao.findUser(testUser));
    }

    @Test
    public void successfulCreateUser() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);
        Assertions.assertNotNull(dao.findUser(testUser));
    }

    @Test
    public void createDuplicateUser() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        Assertions.assertNotNull(dao.findUser(testUser));

        Assertions.assertThrows(DataAccessException.class, () -> dao.createUser(testUser));
    }

    @Test
    public void successfulFindUser() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);
        Assertions.assertNotNull(dao.findUser(testUser));
    }

    @Test
    public void findNonexistentUser() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        Assertions.assertNull(dao.findUser(testUser));
    }

    @Test
    public void successfulCreateSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);

        dao.createSession(testSession);
        Assertions.assertNotNull(dao.findSession(testSession));
    }

    @Test
    public void createDuplicateSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);

        dao.createSession(testSession);
        Assertions.assertNotNull(dao.findSession(testSession));

        Assertions.assertThrows(DataAccessException.class, () -> dao.createSession(testSession));
    }

    @Test
    public void successfulFindSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);

        dao.createSession(testSession);
        Assertions.assertEquals(testSession, dao.findSession(testSession));
    }

    @Test
    public void findNonexistentSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        Assertions.assertNull(dao.findSession(testSession));
    }

    @Test
    public void successfulDeleteSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createUser(testUser);
        dao.createSession(testSession);
        Assertions.assertEquals(testSession, dao.findSession(testSession));

        dao.deleteSession(testSession);
        Assertions.assertNull(dao.findSession(testSession));
    }

    @Test
    public void deleteNonexistentSession() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        Assertions.assertThrows(DataAccessException.class, () -> dao.deleteSession(testSession));
    }

    @Test
    public void successfulCreateGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createGame(testGame);
        Assertions.assertEquals(testGame, dao.findGame(testGame.getGameID()));
    }

    @Test
    public void createDuplicateGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createGame(testGame);
        Assertions.assertNotNull(dao.findGame(testGame.getGameID()));

        Assertions.assertThrows(DataAccessException.class, () -> dao.createGame(testGame));
    }

    @Test
    public void successfulUpdateGame() throws DataAccessException, InvalidMoveException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createGame(testGame);
        ChessGame updatedGame = new ChessGameImpl();
        updatedGame.makeMove(new ChessMoveImpl(new ChessPositionImpl(1, 2), new ChessPositionImpl(1, 3)));

        Gson gson = new Gson();
        dao.updateGame(testGame.getGameID(), gson.toJson(updatedGame));
        GameData foundGame = dao.findGame(testGame.getGameID());
        GameData expectedGame = new GameData(testGame.getGameID(), testGame.getGameName(), testGame.getWhiteUsername(), testGame.getBlackUsername(), updatedGame);
        Assertions.assertNotNull(foundGame);

        Assertions.assertEquals(expectedGame, foundGame);
    }

    @Test
    public void updateNonexistentGame() throws DataAccessException, InvalidMoveException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        ChessGame updatedGame = new ChessGameImpl();
        updatedGame.makeMove(new ChessMoveImpl(new ChessPositionImpl(1, 2), new ChessPositionImpl(1, 3)));
        Gson gson = new Gson();

        Assertions.assertThrows(DataAccessException.class, () -> dao.updateGame(testGame.getGameID(), gson.toJson(updatedGame)));
    }

    @Test
    public void successfulFindGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();
        dao.createGame(testGame);
        GameData foundGame = dao.findGame(testGame.getGameID());

        Assertions.assertEquals(testGame, foundGame);
    }

    @Test
    public void findNonexistentGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        Assertions.assertNull(dao.findGame(testGame.getGameID()));
    }

    @Test
    public void successfulDeleteGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createGame(testGame);
        Assertions.assertNotNull(dao.findGame(testGame.getGameID()));

        dao.deleteGame(testGame);
        Assertions.assertNull(dao.findGame(testGame.getGameID()));
    }

    @Test
    public void deleteNonexistentGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        Assertions.assertThrows(DataAccessException.class, () -> dao.deleteGame(testGame));
    }

    @Test
    public void successfulFindAllGames() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        Collection<GameData> gameList = new ArrayList<>();
        gameList.add(new GameData("game1"));
        gameList.add(new GameData("game2"));
        gameList.add(new GameData("game3"));

        for (GameData game : gameList) {
            dao.createGame(game);
        }
        boolean equals = true;
        var foundGames = dao.findAllGames();
        if (foundGames.size() != gameList.size()) {
            equals = false;
        }
        for (GameData game : foundGames) {
            if (!gameList.contains(game)) {
                equals = false;
                break;
            }
        }
        Assertions.assertTrue(equals);
    }

    @Test
    public void successfulClaimGameSpot() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        dao.createGame(testGame);
        dao.claimGameSpot(testUser.getUsername(), "white", testGame);
        testGame.setWhiteUsername(testUser.getUsername());

        var foundGame = dao.findGame(testGame.getGameID());
        Assertions.assertEquals(testGame, foundGame);
    }

    @Test
    public void claimGameSpotNonexistentGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);

        Assertions.assertThrows(DataAccessException.class, () -> dao.claimGameSpot(testUser.getUsername(), "white", testGame));
    }

    @Test
    public void successfulAddGameSpectator() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        dao.createGame(testGame);
        dao.addGameSpectator(testUser.getUsername(), testGame);
        testGame.addWatcher(testUser.getUsername());

        var foundGame = dao.findGame(testGame.getGameID());
        Assertions.assertEquals(testGame, foundGame);
    }

    @Test
    public void addGameSpectatorNonexistentGame() throws DataAccessException {
        DataAccess dao = new SQLDataAccess();
        dao.clear();

        dao.createUser(testUser);
        Assertions.assertThrows(DataAccessException.class, () -> dao.addGameSpectator(testUser.getUsername(), testGame));
    }
}