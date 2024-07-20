package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception{
    private int errorCode = 500;

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(String message, int code) {
        super(message);
        errorCode = code;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
