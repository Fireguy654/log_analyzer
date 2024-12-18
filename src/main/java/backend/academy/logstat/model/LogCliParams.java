package backend.academy.logstat.model;

import backend.academy.logstat.service.filter.FromLogFilter;
import backend.academy.logstat.service.filter.LogFilter;
import backend.academy.logstat.service.filter.ToLogFilter;
import com.beust.jcommander.Parameter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;

public class LogCliParams {
    private static final DateTimeFormatter FORMATTER = new DateTimeFormatterBuilder()
        .appendOptional(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        .optionalStart()
        .append(DateTimeFormatter.ISO_LOCAL_DATE)
        .parseDefaulting(ChronoField.CLOCK_HOUR_OF_DAY, 0)
        .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
        .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
        .optionalEnd()
        .toFormatter();

    @Parameter
    private List<String> unnamedResources = new ArrayList<>();

    @Parameter(names = "--path", description = "Resources adder")
    private List<String> namedResources = new ArrayList<>();

    public List<String> resources() {
        return Stream.concat(unnamedResources.stream(), namedResources.stream()).toList();
    }

    @Getter
    private List<LogFilter> filters = new ArrayList<>();

    @Parameter(names = "--from", description = "From date")
    public void addFrom(String from) {
        filters.add(new FromLogFilter(LocalDateTime.parse(from, FORMATTER)));
    }

    @Parameter(names = "--to", description = "To date")
    public void addTo(String to) {
        filters.add(new ToLogFilter(LocalDateTime.parse(to, FORMATTER)));
    }

    @Getter
    @Parameter(names = "--filter-field", description = "Field to filter")
    private String filterField;

    @Getter
    @Parameter(names = "--filter-value", description = "Value for filtering")
    private String filterValue;

    @Getter
    @Parameter(names = "--format", description = "Output format")
    private String format = "markdown";
}
