package dataaccess.DAOInterfaces;

import dataaccess.DataAccessException;
import model.GameData;

public interface GameDAOInterface {
    int createGame(String gameName);
    GameData getGame(String gameID) throws DataAccessException;
    GameData[] listGames();
    void updateGame(String gameID, String playerColor) throws DataAccessException;
    void clear();
}
