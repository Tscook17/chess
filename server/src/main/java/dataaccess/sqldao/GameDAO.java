package dataaccess.sqldao;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.daointerfaces.GameDAOInterface;
import model.GameData;
import chess.ChessGame;

import java.sql.SQLException;
import java.util.ArrayList;

import static dataaccess.DatabaseManager.executeStatement;
import static dataaccess.DatabaseManager.getConnection;

public class GameDAO implements GameDAOInterface {
    @Override
    public int createGame(String gameName) throws DataAccessException {
        String statement = "INSERT INTO gameData (gameName, game) VALUES(?, ?)";
        Gson g = new Gson();
        String gameJson = g.toJson(new ChessGame());
        return executeStatement(statement, gameName, gameJson);
    }

    @Override
    public GameData getGame(int findGameID) throws DataAccessException {
        try (var conn = getConnection()) {
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData WHERE gameID=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setInt(1, findGameID);
                try (var rs = preparedStatement.executeQuery()) {
                    if (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var gameJson = rs.getString("game");
                        Gson g = new Gson();
                        ChessGame game = g.fromJson(gameJson, ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
            throw new DataAccessException("Error: bad request", 400);
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    @Override
    public GameData[] listGames() throws DataAccessException {
        try (var conn = getConnection()) {
            String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, game FROM gameData";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                try (var rs = preparedStatement.executeQuery()) {
                    ArrayList<GameData> gameList = new ArrayList<>();
                    while (rs.next()) {
                        var gameID = rs.getInt("gameID");
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        var gameJson = rs.getString("game");
                        Gson g = new Gson();
                        ChessGame game = g.fromJson(gameJson, ChessGame.class);
                        gameList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
                    }
                    return gameList.toArray(new GameData[0]);
                }
            }
        } catch(SQLException e) {
            throw new DataAccessException(e.getMessage(), 500);
        }
    }

    @Override
    public void updateGame(int gameID, String playerColor, String username) throws DataAccessException {
        GameData oldGame = getGame(gameID);
        // check if available
        if (isPlayerColorFree(oldGame, playerColor)) {
            if (playerColor.equalsIgnoreCase("WHITE")) {
                String statement = "UPDATE gameData SET whiteUsername=? WHERE gameID=?";
                executeStatement(statement, username, gameID);
            } else {
                String statement = "UPDATE gameData SET blackUsername=? WHERE gameID=?";
                executeStatement(statement, username, gameID);
            }
        } else {
            throw new DataAccessException("Error: already taken", 403);
        }
    }

    @Override
    public void clear() throws DataAccessException {
        String statement = "TRUNCATE TABLE gameData";
        executeStatement(statement);
    }

    private boolean isPlayerColorFree(GameData game, String playerColor) throws DataAccessException {
        if (playerColor.equalsIgnoreCase("WHITE")) {
            return (game.whiteUsername() == null);
        } else {
            return (game.blackUsername() == null);
        }
    }
}
