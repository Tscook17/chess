package service.result;

import org.eclipse.jetty.util.log.Log;

public class LoginResult extends LoginRegisterBase {
    public LoginResult() {}

    public LoginResult(String message, int errorCode) {
        super(message, errorCode);
    }

    public LoginResult(String username, String authToken) {
        super(username, authToken);
    }
}
