package servicepackets.request;

public class LogoutRequest extends RequestBase {
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

    @Override
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
