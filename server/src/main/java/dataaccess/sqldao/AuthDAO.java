package dataaccess.sqldao;

import dataaccess.DataAccessException;
import dataaccess.daointerfaces.AuthDAOInterface;
import model.AuthData;

public class AuthDAO implements AuthDAOInterface {
    @Override
    public void createAuth(AuthData authData) {

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
