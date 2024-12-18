package backend.academy.logstat.model;

public enum LogField {
    ADDRESS,
    USER,
    DATE,
    METHOD,
    RESOURCE,
    PROTOCOL,
    STATUS,
    SIZE,
    REFERER,
    AGENT;

    public static LogField fromString(String value) {
        for (LogField logField : LogField.values()) {
            if (logField.name().equalsIgnoreCase(value)) {
                return logField;
            }
        }
        return null;
    }
}
