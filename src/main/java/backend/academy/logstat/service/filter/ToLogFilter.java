package backend.academy.logstat.service.filter;

import backend.academy.logstat.model.LogNote;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ToLogFilter implements LogFilter {
    private final LocalDateTime to;

    @Override
    public boolean test(LogNote logNote) {
        return logNote.dateTime().isBefore(to);
    }

    @Override
    public String description() {
        return String.format("Before date: %s", to);
    }
}
