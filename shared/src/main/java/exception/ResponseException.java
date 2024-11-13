package exception;

public class ResponseException extends RuntimeException {
    private final int statusCode;

    public ResponseException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int status() {
        return this.statusCode;
    }

}
