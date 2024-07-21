package service.result;

public class CreateGameResult {
    int gameID = -1;

    public CreateGameResult() {}

    public CreateGameResult(int gameID) {
        setGameID(gameID);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
