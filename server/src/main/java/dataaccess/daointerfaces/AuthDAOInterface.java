package dataaccess.daointerfaces;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDAOInterface {
    void createAuth(AuthData authData) throws DataAccessException;
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear() throws DataAccessException;
}
