package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessGameImpl;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Scanner;

public class SQLDataAccess implements DataAccess {
    private static final DatabaseConnector chessDatabase = new DatabaseConnector();

    public SQLDataAccess() throws DataAccessException {
    }

    @Override
    public void clear() throws DataAccessException {
        var conn = chessDatabase.getConnection();
        String[] clearStatements = {"TRUNCATE TABLE sessions", "TRUNCATE TABLE watchers", "TRUNCATE TABLE games", "TRUNCATE TABLE users;"};
        try {
            var disableKeyCheck = conn.createStatement();
            disableKeyCheck.execute("SET FOREIGN_KEY_CHECKS=0");
            for (String clear : clearStatements) {
                try (var preparedStatement = conn.prepareStatement(clear)) {
                    preparedStatement.execute();
                } catch (SQLException e) {
                    throw new DataAccessException(e.toString());
                }
            }
            disableKeyCheck.execute("SET FOREIGN_KEY_CHECKS=1");

        } catch (SQLException e) {
            throw new DataAccessException(e.toString());
        }

        chessDatabase.returnConnection(conn);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        String sqlInsert = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

        var conn = chessDatabase.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getEmail());
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            chessDatabase.returnConnection(conn);
        }
    }

    @Override
    public UserData findUser(UserData user) throws DataAccessException {
        String sqlInsert = "SELECT * FROM users WHERE username = ?";
        ResultSet resultSet;
        var conn = chessDatabase.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, user.getUsername());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new UserData(resultSet.getString("username"), resultSet.getString("password"), resultSet.getString("email"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            chessDatabase.returnConnection(conn);
        }
    }

    @Override
    public void createSession(SessionData session) throws DataAccessException {
        String sqlInsert = "INSERT INTO sessions (authToken, username) VALUES (?, ?)";

        var conn = chessDatabase.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, session.getAuthToken());
            preparedStatement.setString(2, session.getUsername());
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            chessDatabase.returnConnection(conn);
        }
    }

    @Override
    public void deleteSession(SessionData session) throws DataAccessException {
        var foundSession = findSession(session);
        if (foundSession == null) {
            throw new DataAccessException("Error: bad request");
        }
        String sqlInsert = "DELETE FROM sessions WHERE authToken = ?";
        ResultSet resultSet;
        var conn = chessDatabase.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, session.getAuthToken());
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            chessDatabase.returnConnection(conn);
        }
    }

    @Override
    public SessionData findSession(SessionData session) throws DataAccessException {
        String sqlInsert = "SELECT * FROM sessions WHERE authToken = ?";
        ResultSet resultSet;
        var conn = chessDatabase.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlInsert)) {
            preparedStatement.setString(1, session.getAuthToken());
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new SessionData(resultSet.getString("authToken"), resultSet.getString("username"));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            chessDatabase.returnConnection(conn);
        }
    }

    @Override
    public void createGame(GameData game) throws DataAccessException {
        String sqlInsert = "INSERT INTO games (gameID, gameName, game) VALUES (?, ?, ?)";

        var conn = chessDatabase.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlInsert)) {
            preparedStatement.setInt(1, game.getGameID());
            preparedStatement.setString(2, game.getGameName());
            Gson gson = new Gson();
            preparedStatement.setNString(3, gson.toJson(game.getGame()));
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            chessDatabase.returnConnection(conn);
        }
    }

    @Override
    public void updateGame(int gameID, String chessGame) throws DataAccessException {
        if (findGame(gameID) == null) {
            throw new DataAccessException("Error: bad request");
        }
        String sqlUpdate = "UPDATE games SET game = ? WHERE gameID = ?";

        var conn = chessDatabase.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlUpdate)) {
            preparedStatement.setString(1, chessGame);
            preparedStatement.setInt(2, gameID);
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            chessDatabase.returnConnection(conn);
        }
    }

    @Override
    public GameData findGame(int gameID) throws DataAccessException {
        String sqlInsert = "SELECT * FROM games WHERE gameID = ?";
        ResultSet resultSet;
        var conn = chessDatabase.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlInsert)) {
            preparedStatement.setInt(1, gameID);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new GameData(resultSet.getInt("gameID"), resultSet.getString("gameName"), resultSet.getString("whiteUsername"),
                        resultSet.getString("blackUsername"), deserializeGame(resultSet.getString("game")));
            } else {
                return null;
            }
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            chessDatabase.returnConnection(conn);
        }
    }

    @Override
    public void deleteGame(GameData game) throws DataAccessException {
        var foundGame = findGame(game.getGameID());
        if (foundGame == null) {
            throw new DataAccessException("Error: bad request");
        }
        String sqlInsert = "DELETE FROM games WHERE gameID = ?";
        ResultSet resultSet;
        var conn = chessDatabase.getConnection();
        try (var preparedStatement = conn.prepareStatement(sqlInsert)) {
            preparedStatement.setInt(1, game.getGameID());
            preparedStatement.execute();
        } catch (SQLException ex) {
            throw new DataAccessException(ex.toString());
        } finally {
            chessDatabase.returnConnection(conn);
        }
    }

    @Override
    public Collection<GameData> findAllGames() throws DataAccessException {
        Collection<GameData> gameList = new HashSet<>();
        return null;
    }

    @Override
    public void claimGameSpot(String username, String playerColor, GameData game) throws DataAccessException {

    }

    @Override
    public void addGameSpectator(String username, GameData game) throws DataAccessException {

    }

    private ChessGame deserializeGame(String gameString) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
        return gsonBuilder.create().fromJson(gameString, ChessGameImpl.class);
    }
}