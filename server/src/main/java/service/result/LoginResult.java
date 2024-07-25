package service.result;

public class LoginResult extends LoginRegisterBase {
    public LoginResult() {}

    public LoginResult(String username, String authToken) {
        super(username, authToken);
    }
}
