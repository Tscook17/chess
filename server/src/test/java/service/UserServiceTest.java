package service;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.result.LoginResult;
import service.result.LogoutResult;
import service.result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {
    private RegisterRequest registerReq;
    private RegisterResult registerRes;
    private LoginRequest loginReq;
    private LoginResult loginRes;
    private LogoutRequest logoutReq;
    private LogoutResult logoutRes;

    @BeforeEach
    void setUp() {
        // register
        registerReq = new RegisterRequest("user","pass","email");
        registerRes = new RegisterResult("user", "123");
        // login
        loginReq = new LoginRequest("user", "pass");
        loginRes = new LoginResult("user","pass");
        // logout
        logoutReq = new LogoutRequest("123");
        logoutRes = new LogoutResult();
    }

    @Test
    void registerServiceSuccess() {
        try {
            RegisterResult result = UserService.RegisterService(registerReq);
            Assertions.assertEquals(result.getUsername(), registerRes.getUsername());
            Assertions.assertNotNull(result.getAuthToken());
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void registerServiceBadRequest() {
        registerReq.setEmail(null);
        Assertions.assertThrows(DataAccessException.class, () -> UserService.RegisterService(registerReq));
    }

    @Test
    void registerServiceAlreadyTaken() {
        try {
            UserService.RegisterService(registerReq);
        } catch(DataAccessException e) {
            Assertions.fail();
        }
        Assertions.assertThrows(DataAccessException.class, () -> UserService.RegisterService(registerReq));
    }

    @Test
    void loginServiceSuccess() {
        try {
            UserService.RegisterService(registerReq);
            LoginResult result = UserService.LoginService(loginReq);
            Assertions.assertEquals(result.getUsername(), loginReq.getUsername());
            Assertions.assertNotNull(result.getAuthToken());
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void loginServiceUnauthorized() {
        try {
            UserService.RegisterService(registerReq);
            loginReq.setPassword("000");
            Assertions.assertThrows(DataAccessException.class, () -> UserService.LoginService(loginReq));
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void logoutService() {
    }
}