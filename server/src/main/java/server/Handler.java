package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.DatabaseService;
import service.GameService;
import service.UserService;
import service.request.*;
import service.result.*;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

import static service.GameService.*;
import static service.UserService.*;

public class Handler {

    public static Object HandleRegister(Request req, Response res) {
        Gson g = new Gson();
        RegisterRequest request = g.fromJson(req.body(),RegisterRequest.class);
        try {
            RegisterResult result = RegisterService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            Map<String, String> pair = new HashMap<String, String>();
            pair.put("message", e.getMessage());
            res.body(g.toJson(pair));
            return res.body();
        }
    }

    public static Object HandleLogin(Request req, Response res) {
        Gson g = new Gson();
        LoginRequest request = g.fromJson(req.body(),LoginRequest.class);
        try {
            LoginResult result = LoginService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            Map<String, String> pair = new HashMap<String, String>();
            pair.put("message", e.getMessage());
            res.body(g.toJson(pair));
            return res.body();
        }
    }

    public static Object HandleLogout(Request req, Response res) {
        Gson g = new Gson();
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        try {
            LogoutResult result = LogoutService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            Map<String, String> pair = new HashMap<String, String>();
            pair.put("message", e.getMessage());
            res.body(g.toJson(pair));
            return res.body();
        }
    }

    public static Object HandleListGames(Request req, Response res) {
        Gson g = new Gson();
        ListGamesRequest request = new ListGamesRequest(req.headers("authorization"));
        try {
            ListGamesResult result = ListGamesService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            Map<String, String> pair = new HashMap<String, String>();
            pair.put("message", e.getMessage());
            res.body(g.toJson(pair));
            return res.body();
        }
    }

    public static Object HandleCreateGame(Request req, Response res) {
        Gson g = new Gson();
        CreateGameRequest request = g.fromJson(req.body(),CreateGameRequest.class);
        request.setAuthToken(req.headers("authorization"));
        try {
            CreateGameResult result = CreateGameService(request);
            res.status(200);
            res.body(g.toJson(result));
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            Map<String, String> pair = new HashMap<String, String>();
            pair.put("message", e.getMessage());
            res.body(g.toJson(pair));
            return res.body();
        }
    }

    public static Object HandleJoinGame(Request req, Response res) {
        Gson g = new Gson();
        JoinGameRequest request = g.fromJson(req.body(),JoinGameRequest.class);
        request.setAuthToken(req.headers("authorization"));
        try {
            JoinGameResult result = JoinGameService(request);
            res.status(200);
            res.body("{}");
            return res.body();
        } catch(DataAccessException e) {
            res.status(e.getErrorCode());
            Map<String, String> pair = new HashMap<String, String>();
            pair.put("message", e.getMessage());
            res.body(g.toJson(pair));
            return res.body();
        }
    }

    public static Object HandleClear(Request req, Response res) {
        DatabaseService.ClearService();
        res.status(200);
        res.body("{}");
        return res.body();
    }
}
