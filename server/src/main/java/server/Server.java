package server;

import spark.*;

public class Server {

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        createRoutes();

        Spark.awaitInitialization();
        System.out.println("Listening on port " + desiredPort);
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void createRoutes() {
        Spark.post("/user", Handler::HandleRegister);
        Spark.post("/session", Handler::HandleLogin);
        Spark.delete("/session", Handler::HandleLogout);
        Spark.get("/game", Handler::HandleListGames);
        Spark.post("/game", Handler::HandleCreateGame);
        Spark.put("/game", Handler::HandleJoinGame);
        Spark.delete("/db", Handler::HandleClear);
    }
}
