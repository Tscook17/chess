package service.result;

public class CreateGameResult extends ResultBase {
    Integer gameID = null;

    public CreateGameResult() {}

    public CreateGameResult(String message, int errorCode) {
        super(message, errorCode);
    }

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
