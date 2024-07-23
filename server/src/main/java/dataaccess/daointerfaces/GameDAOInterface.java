package dataaccess.daointerfaces;

import dataaccess.DataAccessException;
import model.GameData;

public interface GameDAOInterface {
    int createGame(String gameName);
    GameData getGame(int gameID) throws DataAccessException;
    GameData[] listGames();
    void updateGame(int gameID, String playerColor, String username) throws DataAccessException;
    void clear();
}
