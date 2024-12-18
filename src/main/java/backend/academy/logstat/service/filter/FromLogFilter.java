package backend.academy.logstat.service.filter;

import backend.academy.logstat.model.LogNote;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FromLogFilter implements LogFilter {
    private final LocalDateTime from;

    @Override
    public boolean test(LogNote logNote) {
        return logNote.dateTime().isAfter(from);
    }

    @Override
    public String description() {
        return String.format("After date: %s", from);
    }
}
