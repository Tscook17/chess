package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import service.request.*;
import service.result.*;

public class GameService {
    public static ResultBase createGameService(RequestBase requestBase) {
        return createGameServiceRunner((CreateGameRequest) requestBase);
    }

    public static ResultBase joinGameService(RequestBase requestBase) {
        return joinGameServiceRunner((JoinGameRequest) requestBase);
    }

    public static ResultBase listGamesService(RequestBase requestBase) {
        return listGamesServiceRunner((ListGamesRequest) requestBase);
    }

    private static CreateGameResult createGameServiceRunner(CreateGameRequest req) {
        CreateGameResult response = new CreateGameResult();
        // check if good request
        if (req.isBadRequest()) {
            response.setError("Error: bad request", 400);
            return response;
        }
        // get authData
        AuthDAO authDB = new AuthDAO();
        try {
            authDB.getAuth(req.getAuthToken());
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }
        // create game
        GameDAO gameDB = new GameDAO();
        return new CreateGameResult(gameDB.createGame(req.getGameName()));
    }

    private static JoinGameResult joinGameServiceRunner(JoinGameRequest req) {
        JoinGameResult response = new JoinGameResult();
        // check if good request
        if (req.isBadRequest()) {
            response.setError("Error: bad request", 400);
            return response;
        }
        try {
            // get authToken
            AuthDAO authDB = new AuthDAO();
            AuthData authData = authDB.getAuth(req.getAuthToken());
            // update game
            GameDAO gameDB = new GameDAO();
            gameDB.updateGame(req.getGameID(), req.getPlayerColor(), authData.username());
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }

        return new JoinGameResult();
    }

    private static ListGamesResult listGamesServiceRunner(ListGamesRequest req) {
        ListGamesResult response = new ListGamesResult();
        // get authToken
        AuthDAO authDB = new AuthDAO();
        try {
            authDB.getAuth(req.getAuthToken());
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }
        // list games
        GameDAO gameDB = new GameDAO();
        return new ListGamesResult(gameDB.listGames());
    }
}
