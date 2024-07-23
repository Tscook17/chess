package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.DatabaseService;
import service.request.*;
import service.result.*;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

import static service.GameService.*;
import static service.UserService.*;

public class Handler {

    public static Object handleRegister(Request req, Response res) {
        Gson g = new Gson();
        RegisterRequest request = g.fromJson(req.body(),RegisterRequest.class);
        try {
            RegisterResult result = registerService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            res.body(g.toJson(handleError(e)));
            return res.body();
        }
    }

    public static Object handleLogin(Request req, Response res) {
        Gson g = new Gson();
        LoginRequest request = g.fromJson(req.body(),LoginRequest.class);
        try {
            LoginResult result = loginService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            res.body(g.toJson(handleError(e)));
            return res.body();
        }
    }

    public static Object handleLogout(Request req, Response res) {
        Gson g = new Gson();
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        try {
            LogoutResult result = logoutService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            res.body(g.toJson(handleError(e)));
            return res.body();
        }
    }

    public static Object handleListGames(Request req, Response res) {
        Gson g = new Gson();
        ListGamesRequest request = new ListGamesRequest(req.headers("authorization"));
        try {
            ListGamesResult result = listGamesService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            res.body(g.toJson(handleError(e)));
            return res.body();
        }
    }

    public static Object handleCreateGame(Request req, Response res) {
        Gson g = new Gson();
        CreateGameRequest request = g.fromJson(req.body(),CreateGameRequest.class);
        request.setAuthToken(req.headers("authorization"));
        try {
            CreateGameResult result = createGameService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            res.body(g.toJson(handleError(e)));
            return res.body();
        }
    }

    public static Object handleJoinGame(Request req, Response res) {
        Gson g = new Gson();
        JoinGameRequest request = g.fromJson(req.body(),JoinGameRequest.class);
        request.setAuthToken(req.headers("authorization"));
        try {
            JoinGameResult result = joinGameService(request);
            res.status(200);
            res.body("{}");
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            res.body(g.toJson(handleError(e)));
            return res.body();
        }
    }

    public static Object handleClear(Request req, Response res) {
        DatabaseService.clearService();
        res.status(200);
        res.body("{}");
        return res.body();
    }

    private static Map<String, String> handleError(DataAccessException e) {
        Map<String, String> pair = new HashMap<String, String>();
        pair.put("message", e.getMessage());
        return pair;
    }
}
