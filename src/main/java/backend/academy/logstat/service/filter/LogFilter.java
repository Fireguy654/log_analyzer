package backend.academy.logstat.service.filter;

import backend.academy.logstat.model.LogNote;
import java.util.function.Predicate;

public interface LogFilter extends Predicate<LogNote> {
    String description();
}
