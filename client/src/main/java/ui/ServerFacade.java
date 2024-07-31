package ui;

import service.result.*;

public class ServerFacade {
    public RegisterResult register(String username, String password, String email) {
        return new RegisterResult();
    }

    public LoginResult login(String username, String password) {
        return new LoginResult();
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
}
