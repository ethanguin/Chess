package dataAccess;

import model.GameData;
import model.SessionData;
import model.UserData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryDataAccess implements DataAccess {
    private static Collection<UserData> users = new HashSet<>();
    private static Collection<GameData> games = new HashSet<>();
    private static Collection<SessionData> sessions = new HashSet<>();

    @Override
    public void clear() throws DataAccessException {
        users.clear();
        games.clear();
        sessions.clear();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        if (findUser(user) != null) {
            throw new DataAccessException("Error: already taken");
        }
        users.add(user);
    }

    @Override
    public UserData findUser(UserData user) {
        for (UserData userData : users) {
            if (userData.getUsername().equals(user.getUsername())) {
                return userData;
            }
        }
        return null;
    }

    @Override
    public void createSession(SessionData session) throws DataAccessException {
        if (findUser(new UserData(session.getUsername())) != null) {
            sessions.add(session.copy());
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void deleteSession(SessionData session) throws DataAccessException {
        SessionData foundSession = findSession(session);
        if (foundSession == null) {
            throw new DataAccessException("Error: unauthorized");
        } else {
            sessions.remove(foundSession);
        }
    }

    @Override
    public SessionData findSession(SessionData session) {
        for (SessionData sessionData : sessions) {
            if (sessionData.getAuthToken().equals(session.getAuthToken())) {
                return sessionData;
            }
        }
        return null;
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        games.add(game);
    }

    @Override
    public void updateGame(String gameID, String chessGame) throws DataAccessException {
        GameData game = findGame(gameID);
        if (game == null) {
            throw new DataAccessException("Error: game does not exist");
        }
        game.setGameName(chessGame);

    }

    @Override
    public GameData findGame(String gameID) {
        for (GameData gameData : games) {
            if (gameData.getGameID().equals(gameID)) {
                return gameData;
            }
        }
        return null;
    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {
        games.remove(findGame(game.getGameID()));
    }

    @Override
    public Collection<GameData> findAllGames() throws DataAccessException {
        return games;
    }

    @Override
    public void claimGameSpot(String username, String playerColor, GameData game) throws DataAccessException {
        if (playerColor.equalsIgnoreCase("white")) {
            findGame(game.getGameID()).setWhiteUsername(username);
        } else if (playerColor.equalsIgnoreCase("black")) {
            findGame(game.getGameID()).setBlackUsername(username);
        } else if (playerColor.equalsIgnoreCase("")) {
            addGameSpectator(username, game);
        } else {
            throw new DataAccessException("Error: bad request");
        }

    }

    @Override
    public void addGameSpectator(String username, GameData game) throws DataAccessException {
        if (findUser(new UserData(username)) == null) {
            throw new DataAccessException("Error: unauthorized");
        }
        findGame(game.getGameID()).addWatcher(username);
    }
}
