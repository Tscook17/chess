package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.result.LoginResult;
import service.result.LogoutResult;
import service.result.RegisterResult;

import java.util.UUID;

public class UserService {

    public static RegisterResult registerService(RegisterRequest req) throws DataAccessException {
        // check if good request
        if (req.isBadRequest()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        // check if already taken
        UserDAO userDB = new UserDAO();
        if (userDB.getUser(req.getUsername()) != null) {
            throw new DataAccessException("Error: already taken", 403);
        }
        // create user
        userDB.createUser(new UserData(req.getUsername(), req.getPassword(), req.getEmail()));
        // create authToken
        String authToken = UUID.randomUUID().toString();
        AuthDAO authDB = new AuthDAO();
        authDB.createAuth(new AuthData(authToken, req.getUsername()));

        return new RegisterResult(req.getUsername(), authToken);
    }

    public static LoginResult loginService(LoginRequest req) throws DataAccessException {
        // check if good request
        if (req.isBadRequest()) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        // get userData
        UserDAO userDB = new UserDAO();
        UserData userData = userDB.getUser(req.getUsername());
        // check if matches request
        if ((userData == null) || !userData.password().equals(req.getPassword())) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        // create new auth token
        AuthDAO authDB = new AuthDAO();
        String authToken = UUID.randomUUID().toString();
        authDB.createAuth(new AuthData(authToken, req.getUsername()));

        return new LoginResult(req.getUsername(), authToken);
    }

    public static LogoutResult logoutService(LogoutRequest req) throws DataAccessException {
        // check if good request
        if (req.isBadRequest()) {
            throw new DataAccessException("Error: unauthorized", 401);
        }
        // delete authToken
        AuthDAO authDB = new AuthDAO();
        authDB.deleteAuth(req.getAuthToken());

        return new LogoutResult();
    }
}
