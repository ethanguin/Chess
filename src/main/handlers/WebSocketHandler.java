package handlers;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dataAccess.*;
import model.*;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.*;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;


import static chess.ChessGame.TeamColor.*;

@WebSocket
public class WebSocketHandler {

    private final DataAccess dataAccess = new SQLDataAccess();

    public static class Connection {
        public UserData user;
        public GameData game;
        public Session session;

        public Connection(UserData user, Session session) {
            this.user = user;
            this.session = session;
        }


        private void send(String msg) throws Exception {
            System.out.printf("Send to %s: %s%n", user.getUsername(), msg);
            session.getRemote().sendString(msg);
        }

        private void sendError(String msg) throws Exception {
            sendError(session.getRemote(), msg);
        }

        private static void sendError(RemoteEndpoint endpoint, String msg) throws Exception {
            var errMsg = (new ErrorMessage(String.format("ERROR: %s", msg))).toString();
            System.out.println(errMsg);
            endpoint.sendString(errMsg);
        }

    }

    public static class ConnectionManager {
        public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

        public void add(String username, Connection connection) {
            connections.put(username, connection);
        }

        public Connection get(String username) {
            return connections.get(username);
        }

        public void remove(Session session) {
            Connection removeConnection = null;
            for (var c : connections.values()) {
                if (c.session.equals(session)) {
                    removeConnection = c;
                    break;
                }
            }

            if (removeConnection != null) {
                connections.remove(removeConnection.user.getUsername());
            }
        }

        public void announceMessage(int gameID, String excludeUsername, String msg) throws Exception {
            var removeList = new ArrayList<Connection>();
            for (var c : connections.values()) {
                if (c.session.isOpen()) {
                    if (c.game.getGameID() == gameID && !c.user.getUsername().equals(excludeUsername)) {
                        c.send(msg);
                    }
                } else {
                    removeList.add(c);
                }
            }

            for (var c : removeList) {
                connections.remove(c.user.getUsername());
            }
        }

        @Override
        public String toString() {
            var sb = new StringBuilder("[\n");
            for (var c : connections.values()) {
                sb.append(String.format("  {'game':%d, 'user': %s}%n", c.game.getGameID(), c.user));
            }
            sb.append("]");
            return sb.toString();
        }
    }

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        System.out.println("connection opened\n" + connections);
    }


    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        connections.remove(session);
        System.out.println("connection closed\n" + connections);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            var command = readJson(message, GameCommand.class);
            var connection = getConnection(command.getAuthString(), session);
            if (connection != null) {
                switch (command.getCommandType()) {
                    case JOIN_PLAYER -> join(connection, readJson(message, JoinPlayerCommand.class));
                    case JOIN_OBSERVER -> observe(connection, command);
                    case MAKE_MOVE -> move(connection, readJson(message, MoveCommand.class));
                    case LEAVE -> leave(connection, command);
                    case RESIGN -> resign(connection, command);
                }
            } else {
                Connection.sendError(session.getRemote(), "unknown user");
            }
        } catch (Exception e) {
            Connection.sendError(session.getRemote(), e.getMessage());
        }
    }

    private void join(Connection connection, JoinPlayerCommand command) throws Exception {
        var gameData = dataAccess.findGame(command.gameID);
        if (gameData != null) {
            String expectedUsername = (command.playerColor == ChessGame.TeamColor.BLACK) ? gameData.getBlackUsername() : gameData.getWhiteUsername();
            if (expectedUsername.equals(connection.user.getUsername())) {
                connection.game = gameData;
                String loadMsg = (new LoadMessage(gameData)).toString();
                connection.send(loadMsg);

                String notificationMsg = (new NotificationMessage(String.format("%s joined %s as %s", connection.user.getUsername(), gameData.getGameName(), command.playerColor))).toString();
                connections.announceMessage(gameData.getGameID(), connection.user.getUsername(), notificationMsg);
            } else {
                connection.sendError("player has not joined game");
            }
        } else {
            connection.sendError("game does not exist");
        }
    }

    private void observe(Connection connection, GameCommand command) throws Exception {
        GameData gameData = dataAccess.findGame(command.gameID);
        if (gameData != null) {
            connection.game = gameData;
            String loadMsg = (new LoadMessage(gameData)).toString();
            connection.send(loadMsg);

            String notificationMsg = (new NotificationMessage(String.format("%s observing %s", connection.user.getUsername(), gameData.getGameName()))).toString();
            connections.announceMessage(gameData.getGameID(), connection.user.getUsername(), notificationMsg);
        } else {
            connection.sendError("game does not exist");
        }
    }

    private void move(Connection connection, MoveCommand command) throws Exception {
        GameData gameData = dataAccess.findGame(command.gameID);
        if (gameData != null) {
            if (gameData.getState() == GameState.UNDECIDED) {
                gameData.getGame().makeMove(command.move);
                var notificationMsg = (new NotificationMessage(String.format("%s moved %s", connection.user.getUsername(), command.move))).toString();
                connections.announceMessage(gameData.getGameID(), connection.user.getUsername(), notificationMsg);

                gameData = handleGameStateChange(gameData);
                Gson gson = new Gson();
                String gameSerialized = gson.toJson(gameData.getGame());
                dataAccess.updateGame(gameData.getGameID(), gameSerialized);
                connection.game = gameData;

                String loadMsg = (new LoadMessage(gameData)).toString();
                connections.announceMessage(gameData.getGameID(), "", loadMsg);
            } else {
                connection.sendError("game is over: " + gameData.getState());
            }
        } else {
            connection.sendError("game does not exist");
        }
    }

    private void leave(Connection connection, GameCommand command) throws Exception {
        GameData gameData = dataAccess.findGame(command.gameID);
        if (gameData != null) {
            if (gameData.getBlackUsername().equals(connection.user.getUsername())) {
                gameData.setBlackUsername(null);
            } else if (gameData.getWhiteUsername().equals(connection.user.getUsername())) {
                gameData.setWhiteUsername(null);
            }
            Gson gson = new Gson();
            String gameSerialized = gson.toJson(gameData.getGame());
            dataAccess.updateGame(gameData.getGameID(), gameSerialized);
            connections.remove(connection.session);
            var notificationMsg = (new NotificationMessage(String.format("%s left", connection.user.getUsername()))).toString();
            connections.announceMessage(gameData.getGameID(), "", notificationMsg);
        } else {
            connection.sendError("game does not exist");
        }
    }

    private void resign(Connection connection, GameCommand command) throws Exception {
        GameData gameData = dataAccess.findGame(command.gameID);
        if (gameData != null) {
            var color = getPlayerColor(gameData, connection.user.getUsername());
            if (color != null) {
                var state = color == ChessGame.TeamColor.WHITE ? GameState.BLACK : GameState.WHITE;
                gameData.setState(state);
                Gson gson = new Gson();
                String gameSerialized = gson.toJson(gameData.getGame());
                dataAccess.updateGame(gameData.getGameID(), gameSerialized);
                connection.game = gameData;

                String notificationMsg = (new NotificationMessage(String.format("%s resigned", connection.user.getUsername()))).toString();
                connections.announceMessage(gameData.getGameID(), "", notificationMsg);
            } else {
                connection.sendError("only players can resign");
            }
        } else {
            connection.sendError("game does not exist");
        }
    }

    private ChessGame.TeamColor getPlayerColor(GameData gameData, String username) {
        if (gameData.getBlackUsername().equals(username)) {
            return ChessGame.TeamColor.BLACK;
        } else if (gameData.getWhiteUsername().equals(username)) {
            return WHITE;
        }
        return null;
    }

    private GameData handleGameStateChange(GameData gameData) throws Exception {
        NotificationMessage notificationMsg = null;
        ChessGame game = gameData.getGame();
        if (game.isInStalemate(WHITE) || game.isInStalemate(BLACK)) {
            gameData.setState(GameState.DRAW);
            notificationMsg = new NotificationMessage("game is a draw");
        } else if (game.isInCheckmate(WHITE)) {
            gameData.setState(GameState.BLACK);
            notificationMsg = new NotificationMessage(String.format("Black player, %s, wins!", gameData.getBlackUsername()));
        } else if (game.isInCheckmate(BLACK)) {
            gameData.setState(GameState.WHITE);
            notificationMsg = new NotificationMessage(String.format("White player, %s, wins!", gameData.getWhiteUsername()));
        } else if (game.isInCheck(WHITE)) {
            notificationMsg = new NotificationMessage(String.format("White player, %s, is in check!", gameData.getWhiteUsername()));
        } else if (game.isInCheck(BLACK)) {
            notificationMsg = new NotificationMessage(String.format("Black player, %s, is in check!", gameData.getBlackUsername()));
        }

        if (notificationMsg != null) {
            connections.announceMessage(gameData.getGameID(), "", notificationMsg.toString());
        }
        return gameData;
    }


    private static <T> T readJson(String json, Class<T> clazz) throws IOException {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
        gsonBuilder.registerTypeAdapter(ChessGame.class, new ChessPieceAdapter());
        gsonBuilder.registerTypeAdapter(ChessGame.class, new ChessBoardAdapter());


        var obj = gsonBuilder.create().fromJson(json, clazz);
        if (obj == null) {
            throw new IOException("Invalid JSON");
        }
        return obj;
    }


    private Connection getConnection(String id, Session session) throws Exception {
        Connection connection = null;
        var authData = isAuthorized(id);
        if (authData != null) {
            connection = connections.get(authData.getUsername());
            if (connection == null) {
                var user = dataAccess.findUser(new UserData(authData.getUsername()));
                connection = new Connection(user, session);
                connections.add(authData.getUsername(), connection);
            }
        }
        return connection;
    }


    public SessionData isAuthorized(String token) throws DataAccessException {
        if (token != null) {
            return dataAccess.findSession(new SessionData(token));
        }
        return null;
    }

}
