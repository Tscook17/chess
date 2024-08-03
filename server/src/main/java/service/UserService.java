package service;

import dataaccess.DataAccessException;
import dataaccess.sqldao.AuthDAO;
import dataaccess.sqldao.UserDAO;
import model.AuthData;
import model.UserData;
import servicepackets.request.LoginRequest;
import servicepackets.request.LogoutRequest;
import servicepackets.request.RegisterRequest;
import servicepackets.result.LoginResult;
import servicepackets.result.LogoutResult;
import servicepackets.result.RegisterResult;

import java.util.UUID;

public class UserService {
    public static RegisterResult registerService(RegisterRequest req) {
        RegisterResult response = new RegisterResult();
        try {
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
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }
    }

    public static LoginResult loginService(LoginRequest req) {
        LoginResult response = new LoginResult();
        try {
            // check if good request
            if (req.isBadRequest()) {
                response.setError("Error: unauthorized", 401);
                return response;
            }
            // get userData
            UserDAO userDB = new UserDAO();
            UserData userData = userDB.getUser(req.getUsername());
            // check if matches request
            if ((userData == null) || !userDB.verifyPassword(userData.password(), req.getPassword())) {
                response.setError("Error: unauthorized", 401);
                return response;
            }
            // create new auth token
            AuthDAO authDB = new AuthDAO();
            String authToken = UUID.randomUUID().toString();
            authDB.createAuth(new AuthData(authToken, req.getUsername()));

            return new LoginResult(req.getUsername(), authToken);
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }
    }

    public static LogoutResult logoutService(LogoutRequest req) {
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
