package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.request.RequestBase;
import service.result.LoginResult;
import service.result.LogoutResult;
import service.result.RegisterResult;

import java.util.UUID;

public class UserService {

    public static RegisterResult registerService(RequestBase requestBase) {
        return registerServiceRunner((RegisterRequest) requestBase);
    }

    public static LoginResult loginService(RequestBase requestBase) {
        return loginServiceRunner((LoginRequest) requestBase);
    }

    public static LogoutResult logoutService(RequestBase requestBase) {
        return logoutServiceRunner((LogoutRequest) requestBase);
    }

    private static RegisterResult registerServiceRunner(RegisterRequest req) {
        RegisterResult response = new RegisterResult();
        // check if good request
        if (req.isBadRequest()) {
            response.setError("Error: bad request", 400);
            return response;
        }
        // check if already taken
        UserDAO userDB = new UserDAO();
        if (userDB.getUser(req.getUsername()) != null) {
            response.setError("Error: already taken", 403);
            return response;
        }
        // create user
        userDB.createUser(new UserData(req.getUsername(), req.getPassword(), req.getEmail()));
        // create authToken
        String authToken = UUID.randomUUID().toString();
        AuthDAO authDB = new AuthDAO();
        authDB.createAuth(new AuthData(authToken, req.getUsername()));

        return new RegisterResult(req.getUsername(), authToken);
    }

    private static LoginResult loginServiceRunner(LoginRequest req) {
        LoginResult response = new LoginResult();
        // check if good request
        if (req.isBadRequest()) {
            response.setError("Error: unauthorized", 401);
            return response;
        }
        // get userData
        UserDAO userDB = new UserDAO();
        UserData userData = userDB.getUser(req.getUsername());
        // check if matches request
        if ((userData == null) || !userData.password().equals(req.getPassword())) {
            response.setError("Error: unauthorized", 401);
            return response;
        }
        // create new auth token
        AuthDAO authDB = new AuthDAO();
        String authToken = UUID.randomUUID().toString();
        authDB.createAuth(new AuthData(authToken, req.getUsername()));

        return new LoginResult(req.getUsername(), authToken);
    }

    private static LogoutResult logoutServiceRunner(LogoutRequest req) {
        LogoutResult response = new LogoutResult();
        // check if good request
        if (req.isBadRequest()) {
            response.setError("Error: unauthorized", 401);
            return response;
        }
        // delete authToken
        AuthDAO authDB = new AuthDAO();
        try {
            authDB.deleteAuth(req.getAuthToken());
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }

        return new LogoutResult();
    }
}
