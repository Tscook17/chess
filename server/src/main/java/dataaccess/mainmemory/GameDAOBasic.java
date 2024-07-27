package dataaccess.mainmemory;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.daointerfaces.GameDAOInterface;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class GameDAOBasic implements GameDAOInterface {
    private static Map<Integer, GameData> gameDataDB = new HashMap<>();
    private static int currentGameID = 1;

    @Override
    public int createGame(String gameName) {
        GameData newGame =
                new GameData(currentGameID, null, null, gameName, new ChessGame());
        gameDataDB.put(currentGameID, newGame);

        return currentGameID++;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        if (gameDataDB.containsKey(gameID)) {
            return gameDataDB.get(gameID);
        } else {
            throw new DataAccessException("Error: bad request", 400);
        }
    }

    @Override
    public GameData[] listGames() {
        return gameDataDB.values().toArray(new GameData[0]);
    }

    @Override
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException {
        GameData oldGame = getGame(gameID);
        GameData newGame;
        // check if available
        if (isPlayerColorFree(oldGame, playerColor)) {
            if (playerColor.equals("WHITE")) {
                newGame =
                        new GameData(gameID, username, oldGame.blackUsername(), oldGame.gameName(), oldGame.game());
            } else {
                newGame =
                        new GameData(gameID, oldGame.whiteUsername(), username, oldGame.gameName(), oldGame.game());
            }
            gameDataDB.put(gameID, newGame);
        } else {
            throw new DataAccessException("Error: already taken", 403);
        }
    }

    @Override
    public void clear() {
        gameDataDB.clear();
    }

    private boolean isPlayerColorFree(GameData game, String playerColor) throws DataAccessException {
        if (playerColor.equalsIgnoreCase("WHITE")) {
            return (game.whiteUsername() == null);
        } else {
            return (game.blackUsername() == null);
        }
    }
}
