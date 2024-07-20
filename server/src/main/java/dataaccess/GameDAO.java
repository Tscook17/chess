package dataaccess;

import dataaccess.DAOInterfaces.GameDAOInterface;
import model.GameData;

public class GameDAO implements GameDAOInterface {
    @Override
    public String createGame(String gameName) {
        return "";
    }

    @Override
    public GameData getGame(String gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData[] listGames() {
        return new GameData[0];
    }

    @Override
    public void updateGame(String gameID, String playerColor) throws DataAccessException {

    }

    @Override
    public void clear() {

    }
}
