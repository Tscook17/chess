package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.DatabaseService;
import service.UserService;
import service.request.LoginRequest;
import service.request.RegisterRequest;
import service.result.ClearResult;
import service.result.LoginResult;
import service.result.RegisterResult;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class Handler {

    public static Object HandleRegister(Request req, Response res) {
        Gson g = new Gson();
        RegisterRequest request = g.fromJson(req.body(),RegisterRequest.class);
        UserService u = new UserService();
        try {
            RegisterResult result = u.RegisterService(request);
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
        UserService u = new UserService();
        try {
            LoginResult result = u.LoginService(request);
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
