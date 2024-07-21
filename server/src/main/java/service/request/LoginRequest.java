package service.request;

public class LoginRequest {
    private String username = null;
    private String password = null;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        setUsername(username);
        setPassword(password);
    }

    public boolean isBadRequest() {
        return (username == null || password == null);
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
