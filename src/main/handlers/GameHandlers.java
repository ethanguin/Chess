package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import req_Res.GameRequest;
import req_Res.GameResponse;
import service.GameService;
import spark.Request;
import spark.Response;

public class GameHandlers {
    static public Object createGame(Request req, Response res) {
        Gson gson = new Gson();
        var game = gson.fromJson(req.body(), GameRequest.class);
        game.setAuthToken(req.headers("Authorization"));

        GameResponse response = GameService.createGame(game);

        var message = response.getMessage();
        if (message != null) {
            if (message.equals("Error: unauthorized")) {
                res.status(401);
            } else if (message.equals("Error: bad request")) {
                res.status(400);
            } else {
                res.status(500);
            }
        }

        return gson.toJson(response);
    }

    static public Object joinGame(Request req, Response res) {
        Gson gson = new Gson();
        var game = gson.fromJson(req.body(), GameRequest.class);
        game.setAuthToken(req.headers("Authorization"));

        GameResponse response = GameService.joinGame(game);

        var message = response.getMessage();
        if (message != null) {
            switch (message) {
                case "Error: unauthorized" -> res.status(401);
                case "Error: bad request" -> res.status(400);
                case "Error: already taken" -> res.status(403);
                default -> res.status(500);
            }
            return gson.toJson(response);
        }

        return new JsonObject();
    }

    static public Object listGames(Request req, Response res) {
        Gson gson = new Gson();
        GameResponse response = GameService.listGames(req.headers("Authorization"));

        var message = response.getMessage();
        if (message != null) {
            if (message.equals("Error: unauthorized")) {
                res.status(401);
            } else {
                res.status(500);
            }
            return gson.toJson(response);
        }
        return gson.toJson(response);
    }
}
