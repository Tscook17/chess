package dataaccess;

import dataaccess.DAOInterfaces.AuthDAOInterface;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO implements AuthDAOInterface {
    private static Map<String, String> AuthDataDB = new HashMap<>();

    @Override
    public void createAuth(String authData) {

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }

    @Override
    public void clear() {
        AuthDataDB.clear();
    }
}
