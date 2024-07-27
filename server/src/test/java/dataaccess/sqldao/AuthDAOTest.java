package dataaccess.sqldao;

import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthDAOTest {

    @BeforeEach
    void setup() throws DataAccessException {
        new AuthDAO().clear();
    }

    @AfterAll
    static void cleanup() throws DataAccessException {
        new AuthDAO().clear();
    }

    @Test
    void createAuthSuccess() throws DataAccessException {
        AuthDAO authDao = new AuthDAO();
        authDao.createAuth(new AuthData("123","user"));
        AuthData authData = authDao.getAuth("123");
        Assertions.assertEquals("123", authData.authToken());
        Assertions.assertEquals("user", authData.username());
    }

    @Test
    void createAuthFailure() throws DataAccessException {
        AuthDAO authDao = new AuthDAO();
        authDao.createAuth(new AuthData("123","user"));
        Assertions.assertThrows(DataAccessException.class, ()->authDao.createAuth(new AuthData("123","user")));
    }

    @Test
    void getAuthSuccess() throws DataAccessException {
        AuthDAO authDao = new AuthDAO();
        authDao.createAuth(new AuthData("123","user"));
        Assertions.assertNotNull(authDao.getAuth("123"));
    }

    @Test
    void getAuthFailure() throws DataAccessException {
        AuthDAO authDao = new AuthDAO();
        Assertions.assertNull(authDao.getAuth("123"));
    }

    @Test
    void deleteAuthSuccess() {
    }

    @Test
    void deleteAuthFailure() {
    }

    @Test
    void clear() throws DataAccessException {
        AuthDAO authDao = new AuthDAO();
        authDao.createAuth(new AuthData("123","user"));
        authDao.clear();
        Assertions.assertNull(authDao.getAuth("123"));
    }
}