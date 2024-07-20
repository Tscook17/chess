package dataaccess;

import dataaccess.DAOInterfaces.UserDAOInterface;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO implements UserDAOInterface {
    private static Map<String, UserData> UserDataDB = new HashMap<>();

    @Override
    public void createUser(String userData) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    @Override
    public void clear() {
        UserDataDB.clear();
    }
}
