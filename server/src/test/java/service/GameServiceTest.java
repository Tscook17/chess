package service;

import dataaccess.mainmemory.AuthDAOBasic;
import dataaccess.DataAccessException;
import dataaccess.mainmemory.GameDAOBasic;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.request.ListGamesRequest;
import service.request.RegisterRequest;
import service.result.CreateGameResult;
import service.result.JoinGameResult;
import service.result.ListGamesResult;
import service.result.RegisterResult;

import static org.junit.jupiter.api.Assertions.*;

class GameServiceTest {
    private CreateGameRequest createReqGame1;
    private JoinGameRequest joinReq;
    private ListGamesRequest listReq;

    @BeforeEach
    void setUp() {
        createReqGame1 = new CreateGameRequest();
        createReqGame1.setGameName("game1");
        joinReq = new JoinGameRequest();
        joinReq.setPlayerColor("WHITE");
        listReq = new ListGamesRequest();
    }

    @AfterEach
    void cleanUp() {
        DatabaseService.clearService();
    }

    @Test
    void createGameServiceSuccess() {
        RegisterResult registerRes =
                UserService.registerService(new RegisterRequest("user", "pass", "email"));
        createReqGame1.setAuthToken(registerRes.getAuthToken());
        Assertions.assertTrue(GameService.createGameService(createReqGame1).getGameID() > 0);
    }

    @Test
    void createGameServiceBadRequest() {
        createReqGame1.setAuthToken(null);
        CreateGameResult result = GameService.createGameService(createReqGame1);
        Assertions.assertEquals(400,result.getErrorCode());
    }

    @Test
    void createGameServiceUnauthorized() {
        createReqGame1.setAuthToken("123");
        CreateGameResult result = GameService.createGameService(createReqGame1);
        Assertions.assertEquals(401,result.getErrorCode());
    }

    @Test
    void joinGameServiceSuccess() {
        try {
            RegisterResult registerRes =
                    UserService.registerService(new RegisterRequest("user", "pass", "email"));
            createReqGame1.setAuthToken(registerRes.getAuthToken());
            CreateGameResult gameData = GameService.createGameService(createReqGame1);
            joinReq.setAuthToken(registerRes.getAuthToken());
            joinReq.setGameID(gameData.getGameID());
            GameService.joinGameService(joinReq);
            String username = new AuthDAOBasic().getAuth(registerRes.getAuthToken()).username();
            assertEquals(new GameDAOBasic().getGame(gameData.getGameID()).whiteUsername(), username);
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void joinGameServiceBadRequest() {
        RegisterResult registerRes =
                UserService.registerService(new RegisterRequest("user", "pass", "email"));
        createReqGame1.setAuthToken(registerRes.getAuthToken());
        GameService.createGameService(createReqGame1);
        joinReq.setAuthToken(registerRes.getAuthToken());
        JoinGameResult result = GameService.joinGameService(joinReq);
        Assertions.assertEquals(400,result.getErrorCode());
    }

    @Test
    void joinGameServiceUnauthorized() {
        RegisterResult registerRes =
                UserService.registerService(new RegisterRequest("user", "pass", "email"));
        createReqGame1.setAuthToken(registerRes.getAuthToken());
        GameService.createGameService(createReqGame1);
        joinReq.setAuthToken("123");
        joinReq.setGameID(5);
        JoinGameResult result = GameService.joinGameService(joinReq);
        Assertions.assertEquals(401,result.getErrorCode());
    }

    @Test
    void joinGameServiceAlreadyTaken() {
        RegisterResult registerRes =
                UserService.registerService(new RegisterRequest("user", "pass", "email"));
        createReqGame1.setAuthToken(registerRes.getAuthToken());
        CreateGameResult gameData = GameService.createGameService(createReqGame1);
        joinReq.setAuthToken(registerRes.getAuthToken());
        joinReq.setGameID(gameData.getGameID());
        GameService.joinGameService(joinReq);
        JoinGameResult result = GameService.joinGameService(joinReq);
        Assertions.assertEquals(403,result.getErrorCode());
    }

    @Test
    void listGamesServiceSuccess() {
        RegisterResult registerRes =
                UserService.registerService(new RegisterRequest("user", "pass", "email"));
        createReqGame1.setAuthToken(registerRes.getAuthToken());
        GameService.createGameService(createReqGame1);
        listReq.setAuthToken(registerRes.getAuthToken());
        Assertions.assertTrue(GameService.listGamesService(listReq).getGames().length > 0);
    }

    @Test
    void listGamesServiceUnauthorized() {
        RegisterResult registerRes =
                UserService.registerService(new RegisterRequest("user", "pass", "email"));
        createReqGame1.setAuthToken(registerRes.getAuthToken());
        GameService.createGameService(createReqGame1);
        ListGamesResult result = GameService.listGamesService(listReq);
        Assertions.assertEquals(401,result.getErrorCode());
    }
}