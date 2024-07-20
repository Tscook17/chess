package dataaccess.DAOInterfaces;

import dataaccess.DataAccessException;
import model.AuthData;

public interface AuthDAOInterface {
    void createAuth(AuthData authData);
    AuthData getAuth(String authToken) throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;
    void clear();
}
