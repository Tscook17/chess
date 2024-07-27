package service;

import dataaccess.mainmemory.AuthDAOBasic;
import dataaccess.mainmemory.GameDAOBasic;
import dataaccess.mainmemory.UserDAOBasic;
import service.request.RequestBase;
import service.result.ClearResult;

public class DatabaseService {
    public static ClearResult clearService(RequestBase requestBase) {
        return clearService();
    }

    public static ClearResult clearService() {
        AuthDAOBasic a = new AuthDAOBasic();
        a.clear();
        GameDAOBasic g = new GameDAOBasic();
        g.clear();
        UserDAOBasic u = new UserDAOBasic();
        u.clear();
        return new ClearResult();
    }
}
