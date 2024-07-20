package dataaccess;

import dataaccess.DAOInterfaces.UserDAOInterface;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO implements UserDAOInterface {
    private static Map<String, UserData> UserDataDB = new HashMap<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        UserDataDB.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return UserDataDB.getOrDefault(username, null);
    }

    @Override
    public void clear() {
        UserDataDB.clear();
    }
}
