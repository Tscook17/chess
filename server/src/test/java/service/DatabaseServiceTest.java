package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DatabaseServiceTest {
    @Test
    void clearServiceTest() throws Exception {
        // fill user db
        UserDAO userDB = new UserDAO();
        userDB.createUser(new UserData("user1","pass1","email1"));
        userDB.createUser(new UserData("user2","pass2","email2"));
        // fill authToken db
        AuthDAO authDB = new AuthDAO();
        authDB.createAuth(new AuthData("123","user1"));
        authDB.createAuth(new AuthData("456","user2"));
        // fill game db
        GameDAO gameDB = new GameDAO();
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