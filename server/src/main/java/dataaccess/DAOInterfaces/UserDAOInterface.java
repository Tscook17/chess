package dataaccess.DAOInterfaces;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDAOInterface {
    void createUser(String userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clear();
}
