package dataaccess.mainmemory;

import dataaccess.daointerfaces.UserDAOInterface;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class UserDAOBasic implements UserDAOInterface {
    private static Map<String, UserData> userDataDB = new HashMap<>();

    @Override
    public void createUser(UserData userData) {
        userDataDB.put(userData.username(), userData);
    }

    @Override
    public UserData getUser(String username) {
        return userDataDB.getOrDefault(username, null);
    }

    @Override
    public void clear() {
        userDataDB.clear();
    }
}
