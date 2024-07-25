package service;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.request.RequestBase;
import service.result.ClearResult;
import service.result.ResultBase;

public class DatabaseService {
    public static ResultBase clearService(RequestBase requestBase) {
        return clearService();
    }

    public static ClearResult clearService() {
        AuthDAO a = new AuthDAO();
        a.clear();
        GameDAO g = new GameDAO();
        g.clear();
        UserDAO u = new UserDAO();
        u.clear();
        return new ClearResult();
    }
}
