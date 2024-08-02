package ui;

import com.google.gson.Gson;
import service.request.*;
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
            RegisterResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = readResponseBody(http, RegisterResult.class);
            } else {
                result = readErrorBody(http, RegisterResult.class);
            }
            result.setErrorCode(http.getResponseCode());
            return result;
        } catch(Exception e) {
            return new RegisterResult(e.getMessage(), 500);
        }
    }

    public LoginResult login(String username, String password) {
        Gson g = new Gson();
        try {
            String body = g.toJson(new LoginRequest(username, password));
            HttpURLConnection http = sendRequest("/session", "POST", body);
            LoginResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = readResponseBody(http, LoginResult.class);
            } else {
                result = readErrorBody(http, LoginResult.class);
            }
            result.setErrorCode(http.getResponseCode());
            return result;
        } catch(Exception e) {
            return new LoginResult(e.getMessage(), 500);
        }
    }

    public LogoutResult logout(String authToken) {
        Gson g = new Gson();
        try {
            String body = g.toJson(new LogoutRequest(authToken));
            HttpURLConnection http = sendRequest("/session", "DELETE", body);
            LogoutResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = readResponseBody(http, LogoutResult.class);
            } else {
                result = readErrorBody(http, LogoutResult.class);
            }
            result.setErrorCode(http.getResponseCode());
            return result;
        } catch(Exception e) {
            return new LogoutResult(e.getMessage(), 500);
        }
    }

    public ListGamesResult listGames(String authToken) {
        Gson g = new Gson();
        try {
            String body = g.toJson(new ListGamesRequest(authToken));
            HttpURLConnection http = sendRequest("/game", "GET", body);
            ListGamesResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = readResponseBody(http, ListGamesResult.class);
            } else {
                result = readErrorBody(http, ListGamesResult.class);
            }
            result.setErrorCode(http.getResponseCode());
            return result;
        } catch(Exception e) {
            return new ListGamesResult(e.getMessage(), 500);
        }
    }

    public CreateGameResult createGame(String authToken, String gameName) {
        Gson g = new Gson();
        try {
            String body = g.toJson(new CreateGameRequest(authToken, gameName));
            HttpURLConnection http = sendRequest("/game", "POST", body);
            CreateGameResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = readResponseBody(http, CreateGameResult.class);
            } else {
                result = readErrorBody(http, CreateGameResult.class);
            }
            result.setErrorCode(http.getResponseCode());
            return result;
        } catch(Exception e) {
            return new CreateGameResult(e.getMessage(), 500);
        }
    }

    public JoinGameResult joinGame(String authToken, String playerColor, int gameID) {
        Gson g = new Gson();
        try {
            String body = g.toJson(new JoinGameRequest(authToken, playerColor, gameID));
            HttpURLConnection http = sendRequest("/game", "PUT", body);
            JoinGameResult result;
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {
                result = readResponseBody(http, JoinGameResult.class);
            } else {
                result = readErrorBody(http, JoinGameResult.class);
            }
            result.setErrorCode(http.getResponseCode());
            return result;
        } catch(Exception e) {
            return new JoinGameResult(e.getMessage(), 500);
        }
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
