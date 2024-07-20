package server;

import com.google.gson.Gson;
import service.DatabaseService;
import service.result.ClearResult;
import spark.Request;
import spark.Response;

public class Handler {

    public static Object HandleRegister(Request req, Response res) {
        return res;
    }

    public static Object HandleLogin(Request req, Response res) {
        return res;
    }

    public static Object HandleLogout(Request req, Response res) {
        return res;
    }

    public static Object HandleListGames(Request req, Response res) {
        return res;
    }

    public static Object HandleCreateGame(Request req, Response res) {
        return res;
    }

    public static Object HandleJoinGame(Request req, Response res) {
        return res;
    }

    public static Object HandleClear(Request req, Response res) {
        ClearResult result = DatabaseService.ClearService();
        res.status(200);
        res.body("{}");
        return res.body();
    }
}
