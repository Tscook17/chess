package dataaccess;

import dataaccess.DAOInterfaces.AuthDAOInterface;
import model.AuthData;

public class AuthDAO implements AuthDAOInterface {
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

    }
}
