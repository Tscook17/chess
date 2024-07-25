package service.request;

public class ListGamesRequest extends RequestBase {
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

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
