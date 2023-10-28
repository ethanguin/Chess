package server;

import com.google.gson.Gson;
import spark.*;
import handlers.*;

import java.util.*;

public class HTTPServer {

    public static void main(String[] args) {
        new HTTPServer().run();
    }

    private void run() {
        //Specify the port you want the server to listen on
        Spark.port(8080);

        //Register a directory for hosting static files
        Spark.externalStaticFileLocation("web");

        //Error


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
}