package server;

import com.google.gson.Gson;
import service.DatabaseService;
import service.result.ClearResult;
import spark.Request;
import spark.Response;

public class Handler {

//    public static Object HandleRegister(Request request, Response response) {
//    }
//
//    public static Object HandleLogin(Request request, Response response) {
//    }
//
//    public static Object HandleLogout(Request request, Response response) {
//    }
//
//    public static Object HandleListGames(Request request, Response response) {
//    }
//
//    public static Object HandleCreateGame(Request request, Response response) {
//    }
//
//    public static Object HandleJoinGame(Request request, Response response) {
//    }

    public static Object HandleClear(Request req, Response res) {
        ClearResult result = DatabaseService.ClearService();
        if (result.getMessage() == null) {
            res.status(200);
            res.body("{}");
            return res.body();
        } else {
            res.status(500);
            var g = new Gson();
            res.body(g.toJson(result));
            return res.body();
        }
    }
}
