package dataaccess.sqldao;

import chess.ChessGame;
import dataaccess.DataAccessException;
import model.GameData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameDAOTest {

    @BeforeEach
    void setup() throws DataAccessException {
        new GameDAO().clear();
    }

    @AfterAll
    static void cleanup() throws DataAccessException {
        new GameDAO().clear();
    }

    @Test
    void createGameSuccess() throws DataAccessException {
        GameDAO gameDao = new GameDAO();
        int gameID = gameDao.createGame("gameNew");
        Assertions.assertTrue(gameID > 0);
        Assertions.assertEquals("gameNew", gameDao.getGame(gameID).gameName());
    }

    @Test
    void createGameFailure() throws DataAccessException {
        GameDAO gameDao = new GameDAO();
        gameDao.createGame("gameNew");
        Assertions.assertThrows(DataAccessException.class, ()->gameDao.getGame(0));
    }

    @Test
    void getGameSuccess() throws DataAccessException {
        GameDAO gameDao = new GameDAO();
        int gameID = gameDao.createGame("gameNew");
        Assertions.assertEquals("gameNew", gameDao.getGame(gameID).gameName());
        assertEquals(new ChessGame(), gameDao.getGame(gameID).game());
        Assertions.assertEquals(gameID, gameDao.getGame(gameID).gameID());
        assertNull(gameDao.getGame(gameID).whiteUsername());
        assertNull(gameDao.getGame(gameID).blackUsername());
    }

    @Test
    void getGameFailure() {
        GameDAO gameDao = new GameDAO();
        Assertions.assertThrows(DataAccessException.class, ()->gameDao.getGame(133));
        Assertions.assertThrows(DataAccessException.class, ()->gameDao.getGame(0));
    }

    @Test
    void listGamesSuccess() throws DataAccessException {
        GameDAO gameDao = new GameDAO();
        int gameID1 = gameDao.createGame("game1");
        int gameID2 = gameDao.createGame("game2");
        GameData[] gameList = gameDao.listGames();
        Assertions.assertEquals(gameID1, gameList[0].gameID());
        Assertions.assertEquals(gameID2, gameList[1].gameID());
    }

    @Test
    void listGamesFailure() throws DataAccessException {
        GameDAO gameDao = new GameDAO();
        assertEquals(0, gameDao.listGames().length);
    }

    @Test
    void joinGameSuccess() throws DataAccessException {
        GameDAO gameDao = new GameDAO();
        int gameID = gameDao.createGame("gameNew");
        gameDao.joinGame(gameID, "WHITE", "me");
        Assertions.assertEquals("me", gameDao.getGame(gameID).whiteUsername());
    }

    @Test
    void joinGameFailure() throws DataAccessException {
        GameDAO gameDao = new GameDAO();
        int gameID = gameDao.createGame("gameNew");
        gameDao.joinGame(gameID, "WHITE", "me");
        Assertions.assertThrows(DataAccessException.class, ()->gameDao.joinGame(gameID, "WHITE", "me"));
    }

    @Test
    void clearSuccess() throws DataAccessException {
        GameDAO gameDao = new GameDAO();
        int gameID = gameDao.createGame("newGame");
        gameDao.clear();
        Assertions.assertThrows(DataAccessException.class, ()->gameDao.getGame(gameID));
    }
}