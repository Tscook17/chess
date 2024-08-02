package service.result;

public class JoinGameResult extends ResultBase {
    public JoinGameResult() {}

    public JoinGameResult(String message, int errorCode) {
        super(message, errorCode);
    }

    @Override
    void callError() {}
}
