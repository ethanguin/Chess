package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import model.SessionData;
import model.UserData;
import req_Res.SessionResponse;
import service.SessionService;
import spark.Request;
import spark.Response;

public class SessionHandlers {
    static public Object createSession(Request req, Response res) {
        Gson gson = new Gson();
        var user = gson.fromJson(req.body(), UserData.class);

        SessionResponse response = SessionService.createSession(user);

        var message = response.getMessage();
        if (message != null) {
            if (message.equals("Error: unauthorized")) {
                res.status(401);
            } else {
                res.status(500);
            }
        }

        return gson.toJson(response);
    }

    static public Object deleteSession(Request req, Response res) {
        Gson gson = new Gson();
        String authToken = req.headers("Authorization");
        SessionData session = new SessionData();
        session.setAuthToken(authToken);

        SessionResponse response = SessionService.deleteSession(session);
        var message = response.getMessage();

        if (message != null) {
            if (message.equals("Error: unauthorized")) {
                res.status(401);
            } else {
                res.status(500);
            }
            return gson.toJson(response);
        }

        res.status(200);
        return new JsonObject();
    }
}
