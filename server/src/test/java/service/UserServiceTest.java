package service;

import dataaccess.mainmemory.AuthDAOBasic;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import service.request.LoginRequest;
import service.request.LogoutRequest;
import service.request.RegisterRequest;
import service.result.LoginResult;
import service.result.LogoutResult;
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
        RegisterResult result = UserService.registerService(registerReq);
        Assertions.assertEquals(result.getUsername(), registerRes.getUsername());
        Assertions.assertNotNull(result.getAuthToken());
    }

    @Test
    void registerServiceBadRequest() {
        registerReq.setEmail(null);
        RegisterResult result = UserService.registerService(registerReq);
        Assertions.assertEquals(400,result.getErrorCode());
    }

    @Test
    void registerServiceAlreadyTaken() {
        UserService.registerService(registerReq);
        RegisterResult result = UserService.registerService(registerReq);
        Assertions.assertEquals(403,result.getErrorCode());
    }

    @Test
    void loginServiceSuccess() {
        UserService.registerService(registerReq);
        LoginResult result = UserService.loginService(loginReq);
        Assertions.assertEquals(result.getUsername(), loginReq.getUsername());
        Assertions.assertNotNull(result.getAuthToken());
    }

    @Test
    void loginServiceUnauthorized() {
        UserService.registerService(registerReq);
        loginReq.setPassword("000");
        LoginResult result = UserService.loginService(loginReq);
        Assertions.assertEquals(401,result.getErrorCode());
    }

    @Test
    void logoutServiceSuccess() {
        RegisterResult authData = UserService.registerService(registerReq);
        UserService.logoutService(new LogoutRequest(authData.getAuthToken()));
        AuthDAOBasic authDB = new AuthDAOBasic();
        Assertions.assertThrows(DataAccessException.class, () -> authDB.getAuth(authData.getAuthToken()));
    }

    @Test
    void logoutServiceUnauthorized() {
        UserService.registerService(registerReq);
        LogoutRequest request = new LogoutRequest(logoutReq.getAuthToken());
        LogoutResult result = UserService.logoutService(request);
        Assertions.assertEquals(401,result.getErrorCode());
    }
}