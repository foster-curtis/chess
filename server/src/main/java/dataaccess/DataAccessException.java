package dataaccess;

/**
 * Indicates there was an error connecting to the database
 */
public class DataAccessException extends Exception {
    private final int statusCode;

    public DataAccessException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int StatusCode() {
        return this.statusCode;
    }
}
