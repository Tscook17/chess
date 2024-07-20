package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.result.ClearResult;

public class DatabaseService {

    public static ClearResult ClearService() {
        AuthDAO a = new AuthDAO();
        a.clear();
        GameDAO g = new GameDAO();
        g.clear();
        UserDAO u = new UserDAO();
        u.clear();
        return new ClearResult();
    }
}
