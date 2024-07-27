package dataaccess.sqldao;

import dataaccess.DataAccessException;
import dataaccess.daointerfaces.GameDAOInterface;
import model.GameData;

public class GameDAO implements GameDAOInterface {
    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        return new GameData[0];
    }

    @Override
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }
}
