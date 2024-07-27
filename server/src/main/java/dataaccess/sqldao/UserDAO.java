package dataaccess.sqldao;

import dataaccess.DataAccessException;
import dataaccess.daointerfaces.UserDAOInterface;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.executeStatement;
import static dataaccess.DatabaseManager.getConnection;

public class UserDAO implements UserDAOInterface {
    @Override
    public void createUser(UserData userData) throws DataAccessException {
        String statement = "INSERT INTO userData (username, password, email) VALUES(?, ?, ?)";
        String hashPW = hashPassword(userData.password());
        executeStatement(statement, userData.username(), hashPW, userData.email());
    }

    @Override
    public UserData getUser(String findUsername) throws DataAccessException {
        try (var conn = getConnection()) {
            String statement = "SELECT username, password, email FROM userData WHERE username=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, findUsername);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var username = rs.getString("username");
                        var password = rs.getString("password");
                        var email = rs.getString("email");
                        return new UserData(username, password, email);
                    } else {
                        return null;
                    }
                }
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE userData";
        executeStatement(statement);
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public boolean verifyPassword(String hashPW, String normalPW) {
        return BCrypt.checkpw(normalPW, hashPW);
    }
}
