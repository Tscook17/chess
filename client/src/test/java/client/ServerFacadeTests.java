package client;

import org.junit.jupiter.api.*;
import server.Server;
import service.result.LoginResult;
import service.result.RegisterResult;
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
}
