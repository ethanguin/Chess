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
        try {
            findUser(user);
        } catch (DataAccessException e) {
            users.add(user);
            return;
        }
        throw new DataAccessException("Error: already taken");
    }

    @Override
    public UserData findUser(UserData user) throws DataAccessException {
        for (UserData userData : users) {
            if (userData.getUsername().equals(user.getUsername())) {
                return userData;
            }
        }
        throw new DataAccessException("user does not exist");
    }

    @Override
    public void createSession(SessionData session) throws DataAccessException {
        try {
            findUser(new UserData(session.getUsername()));
            sessions.add(session);
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void deleteSession(SessionData session) throws DataAccessException {
        try {
            sessions.remove(findSession(session));
        } catch (DataAccessException e) {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public SessionData findSession(SessionData session) throws DataAccessException {
        for (SessionData sessionData : sessions) {
            if (sessionData.getAuthToken().equals(session.getAuthToken())) {
                return sessionData;
            }
        }
        throw new DataAccessException("no session found");
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        games.add(game);
    }

    @Override
    public void updateGame(int gameID, String chessGame) throws DataAccessException {
        findGame(gameID).setGameName(chessGame);
    }

    @Override
    public GameData findGame(int gameID) throws DataAccessException {
        for (GameData gameData : games) {
            if (gameData.getGameID() == gameID) {
                return gameData;
            }
        }
        throw new DataAccessException("Error: bad request");
    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {
        games.remove(findGame(game.getGameID()));
    }

    @Override
    public Collection<GameData> findAllGames() throws DataAccessException {
        return new HashSet<>(games);
    }

    @Override
    public void claimGameSpot(String username, String playerColor, GameData gameData) throws DataAccessException {
        var game = findGame(gameData.getGameID());
        DataAccessException alreadyTaken = new DataAccessException("Error: already taken");
        if (playerColor.equalsIgnoreCase("white")) {
            if (game.getWhiteUsername() != null) {
                throw alreadyTaken;
            }
            game.setWhiteUsername(username);
        } else if (playerColor.equalsIgnoreCase("black")) {
            if (game.getBlackUsername() != null) {
                throw alreadyTaken;
            }
            game.setBlackUsername(username);
        } else if (playerColor.equalsIgnoreCase("")) {
            addGameSpectator(username, game);
        } else {
            throw new DataAccessException("Error: bad request");
        }

    }

    @Override
    public void addGameSpectator(String username, GameData game) throws DataAccessException {
        findUser(new UserData(username));
        findGame(game.getGameID()).addWatcher(username);
    }
}
