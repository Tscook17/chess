package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import service.request.CreateGameRequest;
import service.request.JoinGameRequest;
import service.request.ListGamesRequest;
import service.result.CreateGameResult;
import service.result.JoinGameResult;
import service.result.ListGamesResult;

public class GameService {
    public CreateGameResult CreateGameService(CreateGameRequest req) throws DataAccessException {
        // check if good request
        if (req.isBadRequest()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        // get authData
        AuthDAO authDB = new AuthDAO();
        AuthData authData = authDB.getAuth(req.getAuthToken());
        // create game
        GameDAO gameDB = new GameDAO();
        return new CreateGameResult(gameDB.createGame(req.getGameName()));
    }

    public JoinGameResult JoinGameService(JoinGameRequest req) throws DataAccessException {
        // check if good request
        if (req.isBadRequest()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        // get authToken
        AuthDAO authDB = new AuthDAO();
        AuthData authData = authDB.getAuth(req.getAuthToken());
        // update game
        GameDAO gameDB = new GameDAO();
        gameDB.updateGame(req.getGameID(), req.getPlayerColor(), authData.username());

        return new JoinGameResult();
    }

    public ListGamesResult ListGamesService(ListGamesRequest req) throws DataAccessException {
        // get authToken
        AuthDAO authDB = new AuthDAO();
        AuthData authData = authDB.getAuth(req.getAuthToken());
        // list games
        GameDAO gameDB = new GameDAO();
        return new ListGamesResult(gameDB.listGames());
    }
}
