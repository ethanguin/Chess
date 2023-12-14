package server;

import com.google.gson.Gson;
import spark.*;
import handlers.*;

import java.util.*;

import static spark.Spark.webSocket;

public class HTTPServer {
    WebSocketHandler webSocketHandler;

    public HTTPServer() {
        webSocketHandler = new WebSocketHandler();
    }

    public static void main(String[] args) {
        var server = new HTTPServer();
        server.run();
    }

    private void run() {
        //Specify the port you want the server to listen on
        Spark.port(8080);

        //Register a directory for hosting static files
        Spark.externalStaticFileLocation("web");

        //WebSocket handler
        webSocket("/connect", webSocketHandler);

        //Error
        Spark.get("/error", this::throwError);
        Spark.exception(Exception.class, this::errorHandler);
        Spark.notFound((req, res) -> {
            var msg = String.format("[%s] %s not found", req.requestMethod(), req.pathInfo());
            return errorHandler(new Exception(msg), req, res);
        });

        //Register admin handlers
        Spark.delete("/db", AdminHandlers::clear);

        //Register game handlers
        Spark.get("/game", GameHandlers::listGames);
        Spark.post("/game", GameHandlers::createGame);
        Spark.put("/game", GameHandlers::joinGame);

        //Register user handlers
        Spark.post("/user", UserHandlers::createUser);

        //Register session handlers
        Spark.post("/session", SessionHandlers::createSession);
        Spark.delete("/session", SessionHandlers::deleteSession);
    }

    private Object throwError(Request req, Response res) {
        throw new RuntimeException("Server on fire");
    }

    public Object errorHandler(Exception e, Request req, Response res) {
        var body = new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return body;
    }
}