package server;

import service.DatabaseService;
import service.result.ClearResult;
import spark.Request;
import spark.Response;

public class Handler {

    public static Object HandleRegister(Request request, Response response) {
    }

    public static Object HandleLogin(Request request, Response response) {
    }

    public static Object HandleLogout(Request request, Response response) {
    }

    public static Object HandleListGames(Request request, Response response) {
    }

    public static Object HandleCreateGame(Request request, Response response) {
    }

    public static Object HandleJoinGame(Request request, Response response) {
    }

    public static Object HandleClear(Request request, Response response) {
        ClearResult result = DatabaseService.ClearService();
    }
}
