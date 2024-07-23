package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.result.LoginResult;
import service.result.RegisterResult;

class UserServiceTest {
    private RegisterRequest registerReq;
    private RegisterResult registerRes;
    private LoginRequest loginReq;
    private LogoutRequest logoutReq;

    @BeforeEach
    void setUp() {
        registerReq = new RegisterRequest("user","pass","email");
        registerRes = new RegisterResult("user", "123");
        loginReq = new LoginRequest("user", "pass");
        logoutReq = new LogoutRequest("123");
    }

    @AfterEach
    void cleanUp() {
        DatabaseService.clearService();
    }

    @Test
    void registerServiceSuccess() {
        try {
            RegisterResult result = UserService.registerService(registerReq);
            Assertions.assertEquals(result.getUsername(), registerRes.getUsername());
            Assertions.assertNotNull(result.getAuthToken());
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void registerServiceBadRequest() {
        registerReq.setEmail(null);
        Assertions.assertThrows(DataAccessException.class, () -> UserService.registerService(registerReq));
    }

    @Test
    void registerServiceAlreadyTaken() {
        try {
            UserService.registerService(registerReq);
        } catch(DataAccessException e) {
            Assertions.fail();
        }
        Assertions.assertThrows(DataAccessException.class, () -> UserService.registerService(registerReq));
    }

    @Test
    void loginServiceSuccess() {
        try {
            UserService.registerService(registerReq);
            LoginResult result = UserService.loginService(loginReq);
            Assertions.assertEquals(result.getUsername(), loginReq.getUsername());
            Assertions.assertNotNull(result.getAuthToken());
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void loginServiceUnauthorized() {
        try {
            UserService.registerService(registerReq);
            loginReq.setPassword("000");
            Assertions.assertThrows(DataAccessException.class, () -> UserService.loginService(loginReq));
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void logoutServiceSuccess() {
        try {
            RegisterResult authData = UserService.registerService(registerReq);
            UserService.logoutService(new LogoutRequest(authData.getAuthToken()));
            AuthDAO authDB = new AuthDAO();
            Assertions.assertThrows(DataAccessException.class, () -> authDB.getAuth(authData.getAuthToken()));
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void logoutServiceUnauthorized() {
        try {
            UserService.registerService(registerReq);
            LogoutRequest request = new LogoutRequest(logoutReq.getAuthToken());
            Assertions.assertThrows(DataAccessException.class, () -> UserService.logoutService(request));
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }
}