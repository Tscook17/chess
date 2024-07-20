package dataaccess;

import dataaccess.DAOInterfaces.AuthDAOInterface;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class AuthDAO implements AuthDAOInterface {
    private static Map<String, String> AuthDataDB = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) {
        AuthDataDB.put(authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        if (AuthDataDB.containsKey(authToken)) {
            return new AuthData(authToken, AuthDataDB.get(authToken));
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (AuthDataDB.containsKey(authToken)) {
            AuthDataDB.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized");
        }
    }

    @Override
    public void clear() {
        AuthDataDB.clear();
    }
}
