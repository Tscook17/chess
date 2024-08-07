package service;

import dataaccess.DataAccessException;
import dataaccess.sqldao.AuthDAO;
import org.junit.jupiter.api.*;
import servicepackets.request.LoginRequest;
import servicepackets.request.LogoutRequest;
import servicepackets.request.RegisterRequest;
import servicepackets.result.LoginResult;
import servicepackets.result.LogoutResult;
import servicepackets.result.RegisterResult;

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
        Assertions.assertEquals(registerRes.getUsername(), result.getUsername());
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
        Assertions.assertEquals(loginReq.getUsername(), result.getUsername());
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
        AuthDAO authDB = new AuthDAO();
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