package dataaccess;

import dataaccess.daointerfaces.AuthDAOInterface;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO implements AuthDAOInterface {
    private static Map<String, String> authDataDB = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) {
        authDataDB.put(authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (authDataDB.containsKey(authToken)) {
            return new AuthData(authToken, authDataDB.get(authToken));
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authDataDB.containsKey(authToken)) {
            authDataDB.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public void clear() {
        authDataDB.clear();
    }
}
