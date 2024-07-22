package service.request;

import dataaccess.DataAccessException;

public class RegisterRequest {
    private String username;
    private String password;
    private String email;

    public RegisterRequest(String username, String password, String email) {
        setUsername(username);
        setPassword(password);
        setEmail(email);
    }

    public boolean isBadRequest() {
        return (username == null || password == null || email == null);
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
