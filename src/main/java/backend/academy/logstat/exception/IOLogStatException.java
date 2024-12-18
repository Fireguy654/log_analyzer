package backend.academy.logstat.exception;

public class IOLogStatException extends LogStatException {
    public IOLogStatException(String message) {
        super(message);
    }

    public IOLogStatException() {
    }

    public IOLogStatException(Throwable cause) {
        super(cause);
    }

    public IOLogStatException(String message, Throwable cause) {
        super(message, cause);
    }
}
