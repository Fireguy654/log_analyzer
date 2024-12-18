package backend.academy.logstat.service;

import backend.academy.logstat.exception.IOLogStatException;
import backend.academy.logstat.model.LogNote;
import backend.academy.logstat.model.stats.BasicReport;
import backend.academy.logstat.service.filter.LogFilter;
import backend.academy.logstat.service.parser.LogParser;
import com.google.common.base.Predicates;
import java.io.BufferedReader;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ReportCalculator {
    private final LogParser logParser;
    private final Collection<LogFilter> filters;
    @Getter
    private final BasicReport stats = new BasicReport();

    public ReportCalculator(LogParser logParser, Collection<LogFilter> filters) {
        this.logParser = logParser;
        this.filters = filters;
    }

    public void collectStats(BufferedReader reader) {
        try {
            filterAll(reader.lines().map(logParser)).forEach(stats);
        } catch (UncheckedIOException e) {
            throw new IOLogStatException("Couldn't parse log file", e);
        }
    }

    private Stream<LogNote> filterAll(Stream<LogNote> stream) {
        Stream<LogNote> res = stream.filter(Predicates.notNull());
        for (LogFilter filter : filters) {
            res = res.filter(filter);
        }
        return res;
    }
}
