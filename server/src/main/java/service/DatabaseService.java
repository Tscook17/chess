package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import service.result.ClearResult;

public class DatabaseService {

    public static ClearResult ClearService() {
//        try {
            AuthDAO a = new AuthDAO();
            a.clear();
            GameDAO g = new GameDAO();
            g.clear();
            UserDAO u = new UserDAO();
            u.clear();
//        } catch(DataAccessException e) {
//            ClearResult result = new ClearResult();
//            result.setMessage(e.getMessage());
//        }
        return new ClearResult();
    }
}
