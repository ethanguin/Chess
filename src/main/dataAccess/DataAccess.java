package dataAccess;

import model.GameData;
import model.SessionData;
import model.UserData;

import java.util.Collection;

/**
 * <code>DataAccess</code> Interface for accessing data in various locations. Possible implementations include: in-memory database, online database, or storage database
 */
public interface DataAccess {
    /**
     * <code>clear</code> Method for clearing all data from the DataAccess implementation
     *
     * @throws DataAccessException - throws exception if unable to access the stored data
     */
    void clear() throws DataAccessException;

    /**
     * <code>createUser</code> Method for creating a user and storing the data in a DataAccess implementation
     *
     * @param user - the user to be stored (username, password, email)
     * @throws DataAccessException - throws if user exists, if email exists, or if unable to access the stored data
     */
    void createUser(UserData user) throws DataAccessException;

    /**
     * <code>findUser</code> Method for finding an existing user
     *
     * @param user - the user to find in the database
     * @return UserData - returns null if no existing user is found, returns the data if the information is successfully found
     * @throws DataAccessException - throws if no such user is found or if unable to access stored data
     */
    UserData findUser(UserData user) throws DataAccessException;

    /**
     * <code>createSession</code> Method for creating a new login session for a user
     *
     * @param session - the session to start (includes authToken and username of the user)
     * @throws DataAccessException - throws if no user exists, if the user is unauthorized, or if unable to access stored data
     */
    void createSession(SessionData session) throws DataAccessException;

    /**
     * <code>deleteSession</code> Method for deleting a login session for a user
     *
     * @param session - the session to delete from the DataAccess implementation
     * @throws DataAccessException - throws if authToken doesn't match any existing ones in the stored data, or if unable to access stored data
     */
    void deleteSession(SessionData session) throws DataAccessException;

    /**
     * <code>findSession</code> Method for finding an existing created session
     *
     * @param session - session to find
     * @return SessionData - returns the data if the session is successfully found
     */
    SessionData findSession(SessionData session) throws DataAccessException;

    /**
     * <code>createGame</code> Method for creating a new game and storing it in the DataAccess implementation
     *
     * @param game - the game information to create and store
     * @throws DataAccessException - throws if user is unauthorized, if no game name is provided, or if unable to access stored data
     */
    void createGame(GameData game) throws DataAccessException;

    /**
     * <code>updateGame</code> Method for updating an existing game
     *
     * @param gameID    - the gameID of the game to update
     * @param chessGame - the new chessGame string
     * @throws DataAccessException - throws if user is unauthorized, if gameID doesn't exist, or if unable to access stored data
     */
    void updateGame(int gameID, String chessGame, String whiteUsername, String blackUsername) throws DataAccessException;

    void updateGame(int gameID, String chessGame) throws DataAccessException;

    /**
     * <code>findGame</code> Method for finding an existing game
     *
     * @param gameID - the gameID of the existing game
     * @return GameData - returns the data if the game is successfully found
     * @throws DataAccessException - throws if no gameID exists or if unable to access stored data
     */
    GameData findGame(int gameID) throws DataAccessException;

    /**
     * <code>deleteGame</code> Method for deleting an existing game from the DataAccess implementation
     *
     * @param game - the game to delete
     * @throws DataAccessException - throws if no game is found to delete or if unable to access stored data
     */
    void deleteGame(GameData game) throws DataAccessException;

    /**
     * <code>findAllGames</code> Method for finding all games that have been created
     *
     * @return Collection - a collection of each game that has been created
     * @throws DataAccessException - throws if unable to access stored data
     */
    Collection<GameData> findAllGames() throws DataAccessException;

    /**
     * <code>claimGameSpot</code> Method for adding a user to an existing game
     *
     * @param username    - the user to add
     * @param playerColor - the color of the team they are playing as
     * @param game        - the game the user is joining
     * @throws DataAccessException - throws if no user is found, if playerColor is already taken, if game is not found, or unable to access stored data
     */
    void claimGameSpot(String username, String playerColor, GameData game) throws DataAccessException;

    /**
     * <code>addGameSpectator</code> Method for adding a user as a spectator to a game
     *
     * @param username - user to add as a spectator
     * @param game     - the game to add the user to
     * @throws DataAccessException - throws if no user is found, if game is not found, or unable to access stored data
     */
    void addGameSpectator(String username, GameData game) throws DataAccessException;
}