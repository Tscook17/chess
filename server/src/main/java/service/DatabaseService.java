package service;

import dataaccess.DataAccessException;
import dataaccess.mainmemory.GameDAOBasic;
import dataaccess.sqldao.AuthDAO;
import dataaccess.sqldao.UserDAO;
import service.request.RequestBase;
import service.result.ClearResult;

public class DatabaseService {
    public static ClearResult clearService(RequestBase requestBase) {
        return clearService();
    }

    public static ClearResult clearService() {
        try {
            AuthDAO a = new AuthDAO();
            a.clear();
            GameDAOBasic g = new GameDAOBasic();
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
