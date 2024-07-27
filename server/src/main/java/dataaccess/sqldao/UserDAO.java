package dataaccess.sqldao;

import dataaccess.DataAccessException;
import dataaccess.daointerfaces.UserDAOInterface;
import model.UserData;

public class UserDAO implements UserDAOInterface {
    @Override
    public void createUser(UserData userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {

    }
}
