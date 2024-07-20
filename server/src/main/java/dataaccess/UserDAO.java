package dataaccess;

import dataaccess.DAOInterfaces.UserDAOInterface;
import model.UserData;

public class UserDAO implements UserDAOInterface {
    @Override
    public void createUser(String userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {

    }
}
