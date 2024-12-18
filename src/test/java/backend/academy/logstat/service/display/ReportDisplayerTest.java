package backend.academy.logstat.service.display;

import backend.academy.logstat.model.stats.FullReport;
import backend.academy.logstat.model.stats.OneValueLogStat;
import backend.academy.logstat.model.stats.TopValueLogStat;
import backend.academy.logstat.model.stats.ValueWithScore;
import backend.academy.logstat.service.filter.LogFilter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.BufferedWriter;
import java.io.StringWriter;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportDisplayerTest {
    @Test
    @DisplayName("Correctness of markdown display")
    void printMDReport(@Mock OneValueLogStat singleStat, @Mock TopValueLogStat multiStat, @Mock LogFilter filter) {
        StringWriter stringWriter = new StringWriter();
        ReportDisplayer displayer = new MarkdownReportDisplayer(new BufferedWriter(stringWriter));
        doReturn("value").when(singleStat).value();
        doReturn("single description").when(singleStat).description();
        doReturn(List.of(new ValueWithScore<>("val1", 2), new ValueWithScore<>("val2", 3)))
            .when(multiStat).values();
        doReturn("multi description").when(multiStat).header();
        FullReport report = new FullReport(List.of(singleStat, singleStat), List.of(multiStat, multiStat));
        doReturn("Filter").when(filter).description();
        String expect = """
            # Log Report

            ### Resources
            - resource1
            - resource2

            ### Filters
            - Filter
            - Filter

            ### Single statistics
            - single description: value
            - single description: value

            ### multi description
            |Value|Score|
            |:-:|:-:|
            |val1|2|
            |val2|3|

            ### multi description
            |Value|Score|
            |:-:|:-:|
            |val1|2|
            |val2|3|

            """;

        displayer.printReport(report, List.of("resource1", "resource2"), List.of(filter, filter));


        assertThat(stringWriter.toString()).isEqualTo(expect);
    }

    @Test
    @DisplayName("Correctness of adoc display")
    void printAdocReport(@Mock OneValueLogStat singleStat, @Mock TopValueLogStat multiStat, @Mock LogFilter filter) {
        StringWriter stringWriter = new StringWriter();
        ReportDisplayer displayer = new AdocReportDisplayer(new BufferedWriter(stringWriter));
        doReturn("value").when(singleStat).value();
        doReturn("single description").when(singleStat).description();
        doReturn(List.of(new ValueWithScore<>("val1", 2), new ValueWithScore<>("val2", 3)))
            .when(multiStat).values();
        doReturn("multi description").when(multiStat).header();
        FullReport report = new FullReport(List.of(singleStat, singleStat), List.of(multiStat, multiStat));
        doReturn("Filter").when(filter).description();
        String expect = """
            = Log Report

            === Resources
            * resource1
            * resource2

            === Filters
            * Filter
            * Filter

            === Single statistics
            * single description: value
            * single description: value

            === multi description
            |===
            |Value|Score

            |val1|2

            |val2|3
            |===

            === multi description
            |===
            |Value|Score

            |val1|2

            |val2|3
            |===

            """;

        displayer.printReport(report, List.of("resource1", "resource2"), List.of(filter, filter));


        assertThat(stringWriter.toString()).isEqualTo(expect);
    }
}
