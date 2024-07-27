package service;

import dataaccess.DataAccessException;
import dataaccess.mainmemory.GameDAOBasic;
import dataaccess.sqldao.AuthDAO;
import dataaccess.sqldao.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DatabaseServiceTest {
    @Test
    void clearServiceTest() throws DataAccessException {
        // fill user db
        UserDAO userDB = new UserDAO();
        userDB.createUser(new UserData("user1","pass1","email1"));
        userDB.createUser(new UserData("user2","pass2","email2"));
        // fill authToken db
        AuthDAO authDB = new AuthDAO();
        authDB.createAuth(new AuthData("123","user1"));
        authDB.createAuth(new AuthData("456","user2"));
        // fill game db
        GameDAOBasic gameDB = new GameDAOBasic();
        gameDB.createGame("game1");
        gameDB.createGame("game2");

        // clear db's
        DatabaseService.clearService();

        // check
        Assertions.assertNull(userDB.getUser("user1"));
        Assertions.assertNull(userDB.getUser("user2"));
        Assertions.assertThrows(DataAccessException.class,  () -> authDB.getAuth("123"));
        Assertions.assertThrows(DataAccessException.class,  () -> authDB.getAuth("234"));
        Assertions.assertEquals(gameDB.listGames().length, new GameData[0].length);
    }
}