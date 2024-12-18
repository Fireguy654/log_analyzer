package backend.academy.logstat.exception;

public class LogStatException extends RuntimeException {
    public LogStatException(String message) {
        super(message);
    }

    public LogStatException() {
    }

    public LogStatException(Throwable cause) {
        super(cause);
    }

    public LogStatException(String message, Throwable cause) {
        super(message, cause);
    }
}
