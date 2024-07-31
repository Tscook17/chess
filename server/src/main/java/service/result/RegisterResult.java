package service.result;

public class RegisterResult extends LoginRegisterBase {
    public RegisterResult() {
        super();
    }

    public RegisterResult(String message, int errorCode) {
        super(message, errorCode);
    }

    public RegisterResult(String username, String authToken) {
        super(username, authToken);
    }
}
