package service.result;

public class RegisterResult {
    private String username = null;
    private String authToken = null;

    public RegisterResult() {}

    public RegisterResult(String username, String authToken) {
        setUsername(username);
        setAuthToken(authToken);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
}
