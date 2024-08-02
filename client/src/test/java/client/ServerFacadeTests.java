package client;

import org.junit.jupiter.api.*;
import server.Server;
import service.result.*;
import ui.ServerFacade;

import java.net.HttpURLConnection;
import java.net.URI;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;
    static String url = "http://localhost:";

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        url = url + port;
        facade = new ServerFacade(url);
    }

    @BeforeEach
    public void cleanup() throws Exception {
        URI uri = new URI(url + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.connect();
        if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception("Could not clear database");
        }
    }

    @AfterAll
    static void stopServer() throws Exception {
        URI uri = new URI(url + "/db");
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod("DELETE");
        http.connect();
        if (http.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new Exception("Could not clear database");
        }
        server.stop();
    }

    @Test
    public void registerSuccess() {
        RegisterResult result = facade.register("user","pw","email");
        Assertions.assertTrue(result.getAuthToken().length() > 10);
        Assertions.assertEquals("user", result.getUsername());
    }

    @Test
    public void registerFailure() {
        facade.register("user","pw","email");
        RegisterResult result = facade.register("user","pw","email");
        Assertions.assertNull(result.getAuthToken());
    }

    @Test
    public void loginSuccess() {
        facade.register("user","pw","email");
        LoginResult result = facade.login("user","pw");
        Assertions.assertNotNull(result.getAuthToken());
    }

    @Test
    public void loginFailure() {
        LoginResult result = facade.login("user","pw");
        Assertions.assertEquals(401,result.getErrorCode());
    }

    @Test
    public void logoutSuccess() {
        facade.register("user","pw","email");
        LoginResult result = facade.login("user","pw");
        Assertions.assertEquals(200,facade.logout(result.getAuthToken()).getErrorCode());
    }

    @Test
    public void logoutFailure() {
        LogoutResult result = facade.logout("123");
        Assertions.assertEquals(401, result.getErrorCode());
    }

    @Test
    public void listGamesSuccess() {
        RegisterResult regRes = facade.register("user","pw","email");
        facade.createGame(regRes.getAuthToken(), "newGame");
        ListGamesResult result = facade.listGames(regRes.getAuthToken());
        Assertions.assertEquals(200, result.getErrorCode());
    }

    @Test
    public void listGamesFailure() {

    }

    @Test
    public void createGameSuccess() {
        RegisterResult regRes = facade.register("user","pw","email");
        Assertions.assertTrue(facade.createGame(regRes.getAuthToken(), "newGame").getGameID() > 0);
    }

    @Test
    public void createGameFailure() {
        Assertions.assertEquals(401,facade.createGame("123", "newGame").getErrorCode());
    }

    @Test
    public void joinGameSuccess() {
        RegisterResult regRes = facade.register("user","pw","email");
        CreateGameResult creRes = facade.createGame(regRes.getAuthToken(), "newGame");
        Assertions.assertEquals(200,facade.joinGame(regRes.getAuthToken(), "WHITE", creRes.getGameID()).getErrorCode());
    }

    @Test
    public void joinGameFailure() {
        RegisterResult regRes = facade.register("user","pw","email");
        CreateGameResult creRes = facade.createGame(regRes.getAuthToken(), "newGame");
        facade.joinGame(regRes.getAuthToken(), "WHITE", creRes.getGameID());
        Assertions.assertEquals(403,facade.joinGame(regRes.getAuthToken(), "WHITE", creRes.getGameID()).getErrorCode());

    }
}
