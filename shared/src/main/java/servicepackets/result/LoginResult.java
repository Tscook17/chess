package servicepackets.result;

public class LoginResult extends LoginRegisterBase {
    public LoginResult() {}

    public LoginResult(String message, int errorCode) {
        super(message, errorCode);
    }

    public LoginResult(String username, String authToken) {
        super(username, authToken);
    }
}
