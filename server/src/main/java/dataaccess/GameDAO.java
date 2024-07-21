package dataaccess;

import chess.ChessGame;
import dataaccess.DAOInterfaces.GameDAOInterface;
import model.GameData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GameDAO implements GameDAOInterface {
    private static Map<Integer, GameData> GameDataDB = new HashMap<>();
    private static int currentGameID = 1;

    @Override
    public int createGame(String gameName) {
        GameData newGame =
                new GameData(currentGameID, null, null, gameName, new ChessGame());
        GameDataDB.put(currentGameID, newGame);

        return currentGameID++;
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        if (GameDataDB.containsKey(gameID)) {
            return GameDataDB.get(gameID);
        } else {
            throw new DataAccessException("Error: "); // fixme
        }
    }

    @Override
    public GameData[] listGames() {
        return GameDataDB.values().toArray(new GameData[0]);
    }

    @Override
    public void updateGame(String gameID, String playerColor) throws DataAccessException {
        // fixme
    }

    @Override
    public void clear() {
        GameDataDB.clear();
    }
}
