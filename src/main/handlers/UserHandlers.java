package handlers;

import com.google.gson.Gson;
import model.UserData;
import req_Res.UserResponse;
import service.UserService;
import spark.*;

import java.io.IOException;

public class UserHandlers {
    static public Object createUser(Request req, Response res) {
        Gson gson = new Gson();
        var user = gson.fromJson(req.body(), UserData.class);

        UserResponse response = UserService.createUser(user);

        //check message and set status based on the message
        var message = response.getMessage();
        if (message != null) {
            if (message.equals("Error: bad request")) {
                res.status(400);
            } else if (message.equals("Error: already taken")) {
                res.status(403);
            } else {
                res.status(500);
            }
        }
        return gson.toJson(response);
    }
}
