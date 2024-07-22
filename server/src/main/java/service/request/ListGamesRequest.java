package service.request;

public class ListGamesRequest {
    private String authToken = null;

    public ListGamesRequest() {}

    public ListGamesRequest(String authToken) {
        setAuthToken(authToken);
    }

    public boolean isBadRequest() {
        return (authToken == null);
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
