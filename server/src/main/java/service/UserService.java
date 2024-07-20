package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import service.request.RegisterRequest;
import service.result.RegisterResult;

import java.util.UUID;

public class UserService {

    public RegisterResult RegisterService(RegisterRequest req) throws DataAccessException {
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
}
