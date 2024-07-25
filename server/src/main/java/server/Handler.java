package server;

import com.google.gson.Gson;
import service.DatabaseService;
import service.GameService;
import service.UserService;
import service.request.*;
import service.result.*;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.function.Function;

public class Handler {

    public static Object handleRegister(Request req, Response res) {
        return handleServiceCall(req, res, new RegisterRequest(), UserService::registerService);
    }

    public static Object handleLogin(Request req, Response res) {
        return handleServiceCall(req, res, new LoginRequest(), UserService::loginService);
    }

    public static Object handleLogout(Request req, Response res) {
        return handleServiceCall(req, res, new LogoutRequest(), UserService::logoutService);
    }

    public static Object handleListGames(Request req, Response res) {
        return handleServiceCall(req, res, new ListGamesRequest(), GameService::listGamesService);
    }

    public static Object handleCreateGame(Request req, Response res) {
        return handleServiceCall(req, res, new CreateGameRequest(), GameService::createGameService);
    }

    public static Object handleJoinGame(Request req, Response res) {
        return handleServiceCall(req, res, new JoinGameRequest(), GameService::joinGameService);
    }

    public static Object handleClear(Request req, Response res) {
        return handleServiceCall(req, res, new RequestBase(), DatabaseService::clearService);
    }

    private static Object handleServiceCall(Request req, Response res, RequestBase request,
                                            Function<RequestBase, ResultBase> func) {
        Gson g = new Gson();
        if (!req.body().isEmpty()) { request = g.fromJson(req.body(),request.getClass()); }
        if (req.headers("authorization") != null) {
            request.setAuthToken(req.headers("authorization"));
        }
        ResultBase result = func.apply(request);
        if (result.isError()) {
            res.status(result.getErrorCode());
            res.body(g.toJson(result));
            return res.body();
        }
        res.status(200);
        res.body(g.toJson(result));
        return res.body();
    }

    public static Object errorHandler(Exception e, Request req, Response res) {
        var body =
                new Gson().toJson(Map.of("message", String.format("Error: %s", e.getMessage()), "success", false));
        res.type("application/json");
        res.status(500);
        res.body(body);
        return res.body();
    }
}
