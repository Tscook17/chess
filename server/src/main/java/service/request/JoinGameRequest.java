package service.request;

public class JoinGameRequest extends RequestBase {
    private String authToken = null;
    private String playerColor = null;
    private int gameID = 0;

    public boolean isBadRequest() {
        if (playerColor == null ||
           (!playerColor.equalsIgnoreCase("WHITE") &&
            !playerColor.equalsIgnoreCase("BLACK"))) {
            return true;
        } else {
            return (authToken == null || gameID == 0);
        }
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public int getGameID() {
        return gameID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }
}
