package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import service.request.CreateGameRequest;
import service.result.CreateGameResult;

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
}
