package dataaccess;

import dataaccess.DAOInterfaces.UserDAOInterface;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAO implements UserDAOInterface {
    private static Map<String, UserData> UserDataDB = new HashMap<>();

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (!UserDataDB.containsKey(userData.username())) {
            UserDataDB.put(userData.username(), userData);
        } else {
            throw new DataAccessException("Error: "); // fixme
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        if (UserDataDB.containsKey(username)) {
            return UserDataDB.get(username);
        } else {
            throw new DataAccessException("Error: "); // fixme
        }
    }

    @Override
    public void clear() {
        UserDataDB.clear();
    }
}
