package backend.academy.logstat.service.display;

import backend.academy.logstat.exception.IOLogStatException;
import backend.academy.logstat.model.stats.FullReport;
import backend.academy.logstat.model.stats.OneValueLogStat;
import backend.academy.logstat.model.stats.TopValueLogStat;
import backend.academy.logstat.model.stats.ValueWithScore;
import backend.academy.logstat.service.filter.LogFilter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class ReportDisplayer implements AutoCloseable {
    public static final Function<OneValueLogStat, String> SINGLE_STAT_MAPPER =
        stat -> String.format("%s: %s", stat.description(), stat.value());
    public static final int HEADER_DEFAULT_LEVEL = 3;
    private final BufferedWriter writer;

    public void printReport(FullReport report, List<String> resources, List<LogFilter> filters) {
        printHeader(1, "Log Report");
        printStep();
        printListWithHeader(HEADER_DEFAULT_LEVEL, "Resources", resources);
        printListWithHeader(HEADER_DEFAULT_LEVEL, "Filters", filters, LogFilter::description);
        printListWithHeader(HEADER_DEFAULT_LEVEL, "Single statistics", report.singleStats(), SINGLE_STAT_MAPPER);
        for (TopValueLogStat stat : report.topStats()) {
            printHeader(HEADER_DEFAULT_LEVEL, stat.header());
            printTable(stat.values());
            printStep();
        }
        flush();
    }

    private void printListWithHeader(int level, String header, List<String> elems) {
        printListWithHeader(level, header, elems, Function.identity());
    }

    private <T> void printListWithHeader(int level, String header, List<T> elems, Function<T, String> mapper) {
        printHeader(level, header);
        printList(elems, mapper);
        printStep();
    }

    protected abstract void printStep();

    protected abstract void printHeader(int level, String header);

    protected abstract <T> void printList(List<T> list, Function<? super T, String> mapper);

    protected abstract void printTable(List<ValueWithScore<String>> values);

    protected void println(String line) {
        try {
            writer.write(line);
        } catch (IOException e) {
            throw new IOLogStatException(String.format("Couldn't write %s", line), e);
        }
        println();
    }

    protected void println() {
        try {
            writer.newLine();
        } catch (IOException e) {
            throw new IOLogStatException("Couldn't go to new line", e);
        }
    }

    private void flush() {
        try {
            writer.flush();
        } catch (IOException e) {
            throw new IOLogStatException("Couldn't flush the buffered reader", e);
        }
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}
