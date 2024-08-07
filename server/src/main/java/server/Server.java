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
        Spark.post("/user", APIHandler::handleRegister);
        Spark.post("/session", APIHandler::handleLogin);
        Spark.delete("/session", APIHandler::handleLogout);
        Spark.get("/game", APIHandler::handleListGames);
        Spark.post("/game", APIHandler::handleCreateGame);
        Spark.put("/game", APIHandler::handleJoinGame);
        Spark.delete("/db", APIHandler::handleClear);
        Spark.webSocket("/ws", WebSocketHandler.class);
        Spark.exception(Exception.class, APIHandler::errorHandler);
    }
}
