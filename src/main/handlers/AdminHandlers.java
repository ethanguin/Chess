package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import req_Res.ClearResponse;
import service.AdminService;
import spark.Request;
import spark.Response;

public class AdminHandlers {
    static public Object clear(Request ignoredReq, Response res) {
        ClearResponse response = AdminService.clear();
        Gson gson = new Gson();
        var message = response.getMessage();
        if (message != null) {
            res.status(500);
            return gson.toJson(message);
        }

        return new JsonObject();
    }
}