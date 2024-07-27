package service;

import dataaccess.DataAccessException;
import dataaccess.mainmemory.GameDAOBasic;
import dataaccess.sqldao.AuthDAO;
import model.AuthData;
import service.request.*;
import service.result.*;

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
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }
        // create game
        GameDAOBasic gameDB = new GameDAOBasic();
        return new CreateGameResult(gameDB.createGame(req.getGameName()));
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
            GameDAOBasic gameDB = new GameDAOBasic();
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
        } catch(DataAccessException e) {
            response.setError(e.getMessage(), e.getErrorCode());
            return response;
        }
        // list games
        GameDAOBasic gameDB = new GameDAOBasic();
        return new ListGamesResult(gameDB.listGames());
    }
}
