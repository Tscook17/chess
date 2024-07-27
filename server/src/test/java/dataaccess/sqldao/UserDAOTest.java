package dataaccess.sqldao;

import dataaccess.DataAccessException;
import model.UserData;
import org.eclipse.jetty.server.Authentication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {

    @BeforeEach
    void setUp() throws DataAccessException {
        new UserDAO().clear();
    }

    @Test
    void createUserSuccess() throws DataAccessException {
        UserDAO userDao = new UserDAO();
        UserData userData = new UserData("user","pw","email");
        userDao.createUser(userData);
        UserData dataUser = userDao.getUser("user");
        Assertions.assertNotNull(dataUser);
        Assertions.assertTrue(userDao.verifyPassword(dataUser.password(), "pw"));
    }

    @Test
    void createUserFailure() throws DataAccessException {
        UserDAO userDao = new UserDAO();
        UserData userData = new UserData("user","pw","email");
        userDao.createUser(userData);
        Assertions.assertThrows(DataAccessException.class, ()->userDao.createUser(userData));
    }

    @Test
    void getUserSuccess() throws DataAccessException {
        UserDAO userDao = new UserDAO();
        UserData userData = new UserData("user","pw","email");
        userDao.createUser(userData);
        UserData dataUser = userDao.getUser("user");
        Assertions.assertNotNull(dataUser);
    }

    @Test
    void getUserFailure() throws DataAccessException {
        UserDAO userDao = new UserDAO();
        UserData dataUser = userDao.getUser("user");
        Assertions.assertNull(dataUser);
    }

    @Test
    void clearSuccess() throws DataAccessException {
        UserDAO userDao = new UserDAO();
        userDao.createUser(new UserData("user","pw","email"));
        userDao.clear();
        Assertions.assertNull(userDao.getUser("user"));
    }
}