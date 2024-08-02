package service.request;

public class CreateGameRequest extends RequestBase {
    private String authToken = null;
    private String gameName = null;

    public CreateGameRequest() {}

    public CreateGameRequest(String authToken, String gameName) {
        setAuthToken(authToken);
        setGameName(gameName);
    }

    public boolean isBadRequest() {
        return (authToken == null || gameName == null);
    }

    public String getAuthToken() {
        return authToken;
    }

    @Override
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
