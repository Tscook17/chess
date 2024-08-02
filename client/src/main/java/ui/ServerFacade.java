package ui;

import com.google.gson.Gson;
import service.request.LoginRequest;
import service.request.RegisterRequest;
import service.result.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class ServerFacade {
    private String url;

    public ServerFacade(String url) {
        this.url = url;
    }

    public RegisterResult register(String username, String password, String email) {
        Gson g = new Gson();
        try {
            String body = g.toJson(new RegisterRequest(username, password, email));
            HttpURLConnection http = sendRequest("/user", "POST", body);
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readResponseBody(http, RegisterResult.class);
            } else {
                RegisterResult result = readErrorBody(http, RegisterResult.class);
                result.setErrorCode(http.getResponseCode());
                return result;
            }
        } catch(Exception e) {
            return new RegisterResult(e.getMessage(), 500);
        }
    }

    public LoginResult login(String username, String password) {
        Gson g = new Gson();
        try {
            String body = g.toJson(new LoginRequest(username, password));
            HttpURLConnection http = sendRequest("/session", "POST", body);
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                return readResponseBody(http, LoginResult.class);
            } else {
                LoginResult result = readErrorBody(http, LoginResult.class);
                result.setErrorCode(http.getResponseCode());
                return result;
            }
        } catch(Exception e) {
            return new LoginResult(e.getMessage(), 500);
        }
    }

    public LogoutResult logout(String authToken) {
        return new LogoutResult();
    }

    public ListGamesResult listGames(String authToken) {
        return new ListGamesResult();
    }

    public CreateGameResult createGame(String authToken, String gameName) {
        return new CreateGameResult();
    }

    public JoinGameResult joinGame(String authToken, String playerColor, String gameID) {
        return new JoinGameResult();
    }

    private HttpURLConnection sendRequest(String path, String method, String body) throws Exception {
        URI uri = new URI(url + path);
        HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
        http.setRequestMethod(method);
        writeRequestBody(body, http);
        http.connect();
        return http;
    }

    private void writeRequestBody(String body, HttpURLConnection http) throws Exception {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private <T> T readResponseBody(HttpURLConnection http, Class<T> responseClass) throws Exception {
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return new Gson().fromJson(inputStreamReader, responseClass);
        }
    }

    private <T> T readErrorBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        try (InputStream respBody = http.getErrorStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            return new Gson().fromJson(inputStreamReader, responseClass);
        }
    }
}
