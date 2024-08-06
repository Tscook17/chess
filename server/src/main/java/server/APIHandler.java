package server;

import com.google.gson.Gson;
import service.DatabaseService;
import service.GameService;
import service.UserService;
import servicepackets.request.*;
import servicepackets.result.*;
import spark.Request;
import spark.Response;

import java.util.Map;
import java.util.function.Function;

public class APIHandler {

    public static Object handleRegister(Request req, Response res) {
        return handleServiceCall(req, res, new RegisterRequest(), RegisterRequest.class, UserService::registerService);
    }

    public static Object handleLogin(Request req, Response res) {
        return handleServiceCall(req, res, new LoginRequest(), LoginRequest.class, UserService::loginService);
    }

    public static Object handleLogout(Request req, Response res) {
        return handleServiceCall(req, res, new LogoutRequest(), LogoutRequest.class, UserService::logoutService);
    }

    public static Object handleListGames(Request req, Response res) {
        return handleServiceCall(req, res, new ListGamesRequest(), ListGamesRequest.class, GameService::listGamesService);
    }

    public static Object handleCreateGame(Request req, Response res) {
        return handleServiceCall(req, res, new CreateGameRequest(), CreateGameRequest.class, GameService::createGameService);
    }

    public static Object handleJoinGame(Request req, Response res) {
        return handleServiceCall(req, res, new JoinGameRequest(), JoinGameRequest.class, GameService::joinGameService);
    }

    public static Object handleClear(Request req, Response res) {
        return handleServiceCall(req, res, new RequestBase(), RequestBase.class, DatabaseService::clearService);
    }

    private static <T extends RequestBase, G extends ResultBase> Object handleServiceCall(Request req, Response res,
                                                                                          T request, Class<T> requestType, Function<T, G> func) {
        Gson g = new Gson();
        if (!req.body().isEmpty()) { request = g.fromJson(req.body(),requestType);}
        if (req.headers("authorization") != null) {
            request.setAuthToken(req.headers("authorization"));
        }
        G result = func.apply(request);
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
