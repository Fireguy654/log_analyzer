package backend.academy.logstat.service.filter;

import backend.academy.logstat.model.LogField;
import backend.academy.logstat.model.LogNote;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ValueLogFilter implements LogFilter {
    private final String pattern;
    private final LogField logField;

    @Override
    public boolean test(LogNote logNote) {
        return valueGetter(logNote).matches(pattern);
    }

    @Override
    public String description() {
        return String.format("Matching value '%s' by pattern '%s'", logField, pattern);
    }

    private String valueGetter(LogNote logNote) {
        return switch (logField) {
            case ADDRESS -> logNote.remAddr();
            case USER -> logNote.remUsr();
            case DATE -> logNote.dateTime().toString();
            case METHOD -> logNote.method();
            case RESOURCE -> logNote.resource();
            case PROTOCOL -> logNote.protocol();
            case STATUS -> Integer.toString(logNote.status());
            case SIZE -> Long.toString(logNote.bytesCnt());
            case REFERER -> logNote.ref();
            case AGENT -> logNote.usrAgent();
        };
    }
}
