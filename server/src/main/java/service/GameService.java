package service;

import dataaccess.DataAccessException;
import dataaccess.sqldao.AuthDAO;
import dataaccess.sqldao.GameDAO;
import model.AuthData;
import servicepackets.result.*;
import servicepackets.request.*;

public class GameService {
    public static CreateGameResult createGameService(CreateGameRequest req) {
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
            // create game
            GameDAO gameDB = new GameDAO();
            return new CreateGameResult(gameDB.createGame(req.getGameName()));
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }
    }

    public static JoinGameResult joinGameService(JoinGameRequest req) {
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

    public static ListGamesResult listGamesService(ListGamesRequest req) {
        ListGamesResult response = new ListGamesResult();
        // get authToken
        AuthDAO authDB = new AuthDAO();
        try {
            authDB.getAuth(req.getAuthToken());
            // list games
            GameDAO gameDB = new GameDAO();
            return new ListGamesResult(gameDB.listGames());
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }
    }
}
