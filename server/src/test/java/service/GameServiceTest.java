package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
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
    private CreateGameResult createResGame1;
    private CreateGameRequest createReqGame2;
    private CreateGameResult createResGame2;
    private JoinGameRequest joinReq;
    private JoinGameResult joinRes;
    private ListGamesRequest listReq;
    private ListGamesResult listRes;

    @BeforeEach
    void setUp() {
        createReqGame1 = new CreateGameRequest();
        createReqGame1.setGameName("game1");
        createResGame1 = new CreateGameResult();
        createReqGame2 = new CreateGameRequest();
        createReqGame2.setGameName("game2");
        createResGame2 = new CreateGameResult();

        joinReq = new JoinGameRequest();
        joinReq.setPlayerColor("WHITE");
        joinRes = new JoinGameResult();
        listReq = new ListGamesRequest();
        listRes = new ListGamesResult();
    }

    @AfterEach
    void cleanUp() {
        DatabaseService.ClearService();
    }

    @Test
    void createGameServiceSuccess() {
        try {
            RegisterResult registerRes =
                    UserService.RegisterService(new RegisterRequest("user", "pass", "email"));
            createReqGame1.setAuthToken(registerRes.getAuthToken());
            Assertions.assertTrue(GameService.CreateGameService(createReqGame1).getGameID() > 0);
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void createGameServiceBadRequest() {
        createReqGame1.setAuthToken(null);
        Assertions.assertThrows(DataAccessException.class, () -> GameService.CreateGameService(createReqGame1));
    }

    @Test
    void createGameServiceUnauthorized() {
            Assertions.assertThrows(DataAccessException.class, () -> GameService.CreateGameService(createReqGame1));
    }

    @Test
    void joinGameServiceSuccess() {
        try {
            RegisterResult registerRes =
                    UserService.RegisterService(new RegisterRequest("user", "pass", "email"));
            createReqGame1.setAuthToken(registerRes.getAuthToken());
            CreateGameResult gameData = GameService.CreateGameService(createReqGame1);
            joinReq.setAuthToken(registerRes.getAuthToken());
            joinReq.setGameID(gameData.getGameID());
            GameService.JoinGameService(joinReq);
            String username = new AuthDAO().getAuth(registerRes.getAuthToken()).username();
            assertEquals(new GameDAO().getGame(gameData.getGameID()).whiteUsername(), username);
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void joinGameServiceBadRequest() {
        try {
            RegisterResult registerRes =
                    UserService.RegisterService(new RegisterRequest("user", "pass", "email"));
            createReqGame1.setAuthToken(registerRes.getAuthToken());
            GameService.CreateGameService(createReqGame1);
            joinReq.setAuthToken(registerRes.getAuthToken());
            Assertions.assertThrows(DataAccessException.class, () -> GameService.JoinGameService(joinReq));
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void joinGameServiceUnauthorized() {
        try {
            RegisterResult registerRes =
                    UserService.RegisterService(new RegisterRequest("user", "pass", "email"));
            createReqGame1.setAuthToken(registerRes.getAuthToken());
            GameService.CreateGameService(createReqGame1);
            Assertions.assertThrows(DataAccessException.class, () -> GameService.JoinGameService(joinReq));
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void joinGameServiceAlreadyTaken() {
        try {
            RegisterResult registerRes =
                    UserService.RegisterService(new RegisterRequest("user", "pass", "email"));
            createReqGame1.setAuthToken(registerRes.getAuthToken());
            CreateGameResult gameData = GameService.CreateGameService(createReqGame1);
            joinReq.setAuthToken(registerRes.getAuthToken());
            joinReq.setGameID(gameData.getGameID());
            GameService.JoinGameService(joinReq);
            Assertions.assertThrows(DataAccessException.class,() -> GameService.JoinGameService(joinReq));
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void listGamesServiceSuccess() {
        try {
            RegisterResult registerRes =
                    UserService.RegisterService(new RegisterRequest("user", "pass", "email"));
            createReqGame1.setAuthToken(registerRes.getAuthToken());
            GameService.CreateGameService(createReqGame1);
            listReq.setAuthToken(registerRes.getAuthToken());
            Assertions.assertTrue(GameService.ListGamesService(listReq).getGames().length > 0);
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }

    @Test
    void listGamesServiceUnauthorized() {
        try {
            RegisterResult registerRes =
                    UserService.RegisterService(new RegisterRequest("user", "pass", "email"));
            createReqGame1.setAuthToken(registerRes.getAuthToken());
            GameService.CreateGameService(createReqGame1);
            Assertions.assertThrows(DataAccessException.class, () -> GameService.ListGamesService(listReq));
        } catch(DataAccessException e) {
            Assertions.fail();
        }
    }
}