package service;

import dataaccess.DataAccessException;
import dataaccess.sqldao.AuthDAO;
import dataaccess.sqldao.GameDAO;
import dataaccess.sqldao.UserDAO;
import servicepackets.request.RequestBase;
import servicepackets.result.ClearResult;

public class DatabaseService {
    public static ClearResult clearService(RequestBase requestBase) {
        return clearService();
    }

    public static ClearResult clearService() {
        try {
            AuthDAO a = new AuthDAO();
            a.clear();
            GameDAO g = new GameDAO();
            g.clear();
            UserDAO u = new UserDAO();
            u.clear();
            return new ClearResult();
        } catch(DataAccessException e) {
            ClearResult response = new ClearResult();
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }
    }
}
