package dataAccess;

import model.GameData;
import model.SessionData;
import model.UserData;

import java.util.Collection;

public class MemoryDataAccess implements DataAccess {

    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData findUser(UserData user) throws DataAccessException {
        return null;
    }

    @Override
    public void createSession(SessionData session) throws DataAccessException {

    }

    @Override
    public void deleteSession(SessionData session) throws DataAccessException {

    }

    @Override
    public SessionData findSession(SessionData session) throws DataAccessException {
        return null;
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {

    }

    @Override
    public void updateGame(String gameID, String chessGame) throws DataAccessException {

    }

    @Override
    public GameData findGame(String gameID) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {

    }

    @Override
    public Collection<GameData> findAllGames() throws DataAccessException {
        return null;
    }

    @Override
    public void claimGameSpot(String username, String playerColor, GameData game) throws DataAccessException {

    }

    @Override
    public void addGameSpectator(String username, GameData game) {

    }
}
