package servicepackets.result;

public abstract class ResultBase {
    private String message = null;
    private Integer errorCode = null;

    public ResultBase() {}

    public ResultBase(String message, int errorCode) {
        setMessage(message);
        setErrorCode(errorCode);
    }

    public boolean isError() {
        if (message == null) {
            return false;
        } else {
            callError();
            return true;
        }
    }

    abstract void callError();

    public void setError(String message, int errorCode) {
        setMessage(message);
        setErrorCode(errorCode);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getErrorCode() {
        int temp = errorCode;
        errorCode = null;
        return temp;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
