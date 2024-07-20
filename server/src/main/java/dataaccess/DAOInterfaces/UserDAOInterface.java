package dataaccess.DAOInterfaces;

import dataaccess.DataAccessException;
import model.UserData;

public interface UserDAOInterface {
    void createUser(UserData userData) throws DataAccessException;
    UserData getUser(String username) throws DataAccessException;
    void clear();
}
