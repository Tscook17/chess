package dataaccess.sqldao;

import dataaccess.DataAccessException;
import dataaccess.daointerfaces.AuthDAOInterface;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;

import static dataaccess.DatabaseManager.executeStatement;
import static dataaccess.DatabaseManager.getConnection;

public class AuthDAO implements AuthDAOInterface {
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        String statement = "INSERT INTO AuthData (authToken, username) VALUES(?, ?)";
        executeStatement(statement, authData.authToken(), authData.username());
    }

    @Override
    public AuthData getAuth(String findAuthToken) throws DataAccessException {
        try (var conn = getConnection()) {
            String statement = "SELECT authToken, username FROM AuthData WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, findAuthToken);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var authToken = rs.getString("authToken");
                        var username = rs.getString("username");
                        return new AuthData(authToken, username);
                    }
                }
            }
            throw new DataAccessException("Error: unauthorized", 401);
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        getAuth(authToken);
        String statement = "DELETE FROM AuthData WHERE authToken=?";
        executeStatement(statement, authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE AuthData";
        executeStatement(statement);
    }
}
