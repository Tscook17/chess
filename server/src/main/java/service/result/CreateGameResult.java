package service.result;

public class CreateGameResult extends ResultBase {
    Integer gameID = null;

    public CreateGameResult() {}

    @Override
    void callError() {
        setGameID(null);
    }

    public CreateGameResult(int gameID) {
        setGameID(gameID);
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(Integer gameID) {
        this.gameID = gameID;
    }
}
