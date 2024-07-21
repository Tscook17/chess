package service.request;

public class CreateGameRequest {
    private String authToken = null;
    private String gameName = null;

    public boolean isBadRequest() {
        return (authToken == null || gameName == null);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
