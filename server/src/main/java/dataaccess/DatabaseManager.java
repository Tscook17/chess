package dataaccess;

import java.sql.*;
import java.util.Properties;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseManager {
    private static final String DATABASE_NAME;
    private static final String USER;
    private static final String PASSWORD;
    private static final String CONNECTION_URL;

    /*
     * Load the database information for the db.properties file.
     */
    static {
        try {
            try (var propStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("db.properties")) {
                if (propStream == null) {
                    throw new Exception("Unable to load db.properties");
                }
                Properties props = new Properties();
                props.load(propStream);
                DATABASE_NAME = props.getProperty("db.name");
                USER = props.getProperty("db.user");
                PASSWORD = props.getProperty("db.password");

                var host = props.getProperty("db.host");
                var port = Integer.parseInt(props.getProperty("db.port"));
                CONNECTION_URL = String.format("jdbc:mysql://%s:%d", host, port);
            }
        } catch (Exception ex) {
            throw new RuntimeException("unable to process db.properties. " + ex.getMessage());
        }
    }

    /**
     * Creates the database if it does not already exist.
     */
    public static void createDatabase() throws DataAccessException {
        var statement = "CREATE DATABASE IF NOT EXISTS " + DATABASE_NAME;
        try (var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD)) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
            }
            conn.setCatalog(DATABASE_NAME);
            createUserTable(conn);
            createGameTable(conn);
            createAuthTable(conn);
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    private static void createUserTable(Connection conn) throws SQLException {
        var statement = """
                CREATE TABLE IF NOT EXISTS UserData (
                    username varchar(255) NOT NULL,
                    password varchar(255) NOT NULL,
                    email varchar(255) NOT NULL,
                    PRIMARY KEY (username)
                )""";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void createAuthTable(Connection conn) throws SQLException {
        var statement = """
                CREATE TABLE IF NOT EXISTS AuthData (
                    authToken varchar(255) NOT NULL,
                    userName varchar(255) NOT NULL,
                    PRIMARY KEY (authToken)
                )""";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }
    }

    private static void createGameTable(Connection conn) throws SQLException {
        var statement = """
                CREATE TABLE IF NOT EXISTS GameData (
                    gameID int NOT NULL AUTO_INCREMENT,
                    whiteUsername varchar(255) DEFAULT NULL,
                    blackUsername varchar(255) DEFAULT NULL,
                    gameName varchar(255) NOT NULL,
                    game longText NOT NULL,
                    PRIMARY KEY (gameID)
                )""";
        try (var preparedStatement = conn.prepareStatement(statement)) {
            preparedStatement.executeUpdate();
        }
    }

    /**
     * Create a connection to the database and sets the catalog based upon the
     * properties specified in db.properties. Connections to the database should
     * be short-lived, and you must close the connection when you are done with it.
     * The easiest way to do that is with a try-with-resource block.
     * <br/>
     * <code>
     * try (var conn = DbInfo.getConnection(databaseName)) {
     * // execute SQL statements.
     * }
     * </code>
     */
    public static Connection getConnection() throws DataAccessException {
        try {
            var conn = DriverManager.getConnection(CONNECTION_URL, USER, PASSWORD);
            conn.setCatalog(DATABASE_NAME);
            return conn;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public static int executeStatement(String statement, Object... params) throws DataAccessException {
        try (var conn = getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> ps.setString(i + 1, p);
                        case Integer p -> ps.setInt(i + 1, p);
                        case null -> ps.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (SQLException e) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()),500);
        }
    }
}
