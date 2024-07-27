package server;

import dataaccess.DataAccessException;
import spark.*;

import static dataaccess.DatabaseManager.createDatabase;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.awaitInitialization();
        System.out.println("Listening on port " + desiredPort);

        // Check if database exists
        try {
            createDatabase();
        } catch(DataAccessException e) {
            throw new Error(e.getMessage());
        }

        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void createRoutes() {
        Spark.post("/user", Handler::handleRegister);
        Spark.post("/session", Handler::handleLogin);
        Spark.delete("/session", Handler::handleLogout);
        Spark.get("/game", Handler::handleListGames);
        Spark.post("/game", Handler::handleCreateGame);
        Spark.put("/game", Handler::handleJoinGame);
        Spark.delete("/db", Handler::handleClear);
        Spark.exception(Exception.class, Handler::errorHandler);
    }
}
