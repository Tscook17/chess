package service.result;

public class LogoutResult extends ResultBase {
    public LogoutResult() {}

    public LogoutResult(String message, int errorCode) {
        super(message, errorCode);
    }

    @Override
    void callError() {}
}
