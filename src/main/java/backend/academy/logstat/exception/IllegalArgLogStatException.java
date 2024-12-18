package backend.academy.logstat.exception;

public class IllegalArgLogStatException extends LogStatException {
    public IllegalArgLogStatException(String message) {
        super(message);
    }

    public IllegalArgLogStatException() {
    }

    public IllegalArgLogStatException(Throwable cause) {
        super(cause);
    }

    public IllegalArgLogStatException(String message, Throwable cause) {
        super(message, cause);
    }
}
