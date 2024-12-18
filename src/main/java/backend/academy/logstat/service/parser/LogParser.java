package backend.academy.logstat.service.parser;

import backend.academy.logstat.model.LogNote;
import java.util.function.Function;

public interface LogParser extends Function<String, LogNote> {
}
