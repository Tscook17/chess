package service.result;

public class LoginRegisterBase extends ResultBase {
    private String username = null;
    private String authToken = null;

    public LoginRegisterBase() {}

    public LoginRegisterBase(String message, int errorCode) {
        super(message,errorCode);
    }

    public LoginRegisterBase(String username, String authToken) {
        setUsername(username);
        setAuthToken(authToken);
    }

    @Override
    void callError() {
        setUsername(null);
        setAuthToken(null);
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
