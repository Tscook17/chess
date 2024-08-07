package dataaccess.daointerfaces;

import dataaccess.DataAccessException;
import model.GameData;

public interface GameDAOInterface {
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    GameData[] listGames() throws DataAccessException;
    void joinGame(int gameID, String playerColor, String username) throws DataAccessException;
    void clear() throws DataAccessException;
}
