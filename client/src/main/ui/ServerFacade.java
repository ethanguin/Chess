package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;
import req_Res.*;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public void clear() throws ResponseException {
        var r = this.makeRequest("DELETE", "/db", null, null, ClearResponse.class);
    }

    public UserResponse register(String username, String password, String email) throws ResponseException {
        var request = Map.of("username", username, "password", password, "email", email);
        return this.makeRequest("POST", "/user", request, null, UserResponse.class);
    }

    public SessionResponse login(String username, String password) throws ResponseException {
        var request = Map.of("username", username, "password", password);
        return this.makeRequest("POST", "/session", request, null, SessionResponse.class);
    }

    public void logout(String authToken) throws ResponseException {
        this.makeRequest("DELETE", "/session", null, authToken, null);
    }

    public GameResponse createGame(String authToken, String gameName) throws ResponseException {
        var request = Map.of("gameName", gameName);
        return this.makeRequest("POST", "/game", request, authToken, GameResponse.class);
    }

    public GameData[] listGames(String authToken) throws ResponseException {
        record Response(GameData[] games) {
        }
        var response = this.makeRequest("GET", "/game", null, authToken, Response.class);
        return (response != null ? response.games : new GameData[0]);
    }

    public GameData joinGame(String authToken, int gameID, ChessGame.TeamColor color) throws ResponseException {
        var request = new GameRequest();
        request.setGameID(gameID);
        request.setPlayerColor(color.toString());
        this.makeRequest("PUT", "/game", request, authToken, GameResponse.class);
        return getGame(authToken, gameID);
    }

    public GameData observeGame(String authToken, int gameID) throws ResponseException {
        var request = new GameRequest();
        request.setGameID(gameID);
        this.makeRequest("PUT", "/game", request, authToken, GameResponse.class);
        return getGame(authToken, gameID);
    }

    private GameData getGame(String authToken, int gameID) throws ResponseException {
        var games = listGames(authToken);
        for (var game : games) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        throw new ResponseException(404, "Game does not exist");
    }

    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null) {
                http.addRequestProperty("Authorization", authToken);
            }

            if (request != null) {
                http.addRequestProperty("Accept", "application/json");
                String reqData = new Gson().toJson(request);
                try (OutputStream reqBody = http.getOutputStream()) {
                    reqBody.write(reqData.getBytes());
                }
            }
            http.connect();

            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (http.getResponseCode() == 200) {
                    if (responseClass != null) {
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.registerTypeAdapter(ChessGame.class, new ChessGameAdapter());
                        gsonBuilder.registerTypeAdapter(ChessBoard.class, new ChessBoardAdapter());
                        gsonBuilder.registerTypeAdapter(ChessPiece.class, new ChessPieceAdapter());
                        return gsonBuilder.create().fromJson(reader, responseClass);
                    }
                    return null;
                }

                throw new ResponseException(http.getResponseCode(), reader);
            }
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}