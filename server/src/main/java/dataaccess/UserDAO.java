package dataaccess;

import dataaccess.daointerfaces.UserDAOInterface;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO implements UserDAOInterface {
    private static Map<String, UserData> userDataDB = new HashMap<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        userDataDB.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return userDataDB.getOrDefault(username, null);
    }

    @Override
    public void clear() {
        userDataDB.clear();
    }
}
