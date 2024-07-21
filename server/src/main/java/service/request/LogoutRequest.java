package service.request;

public class LogoutRequest {
    private String authToken = null;

    public LogoutRequest() {}

    public LogoutRequest(String authToken) {
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
