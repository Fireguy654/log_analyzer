package backend.academy.logstat.service;

import backend.academy.logstat.model.stats.FullReport;
import backend.academy.logstat.service.display.ReportDisplayer;
import backend.academy.logstat.service.parser.LogParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogReportCreatorTest {
    @Test
    @DisplayName("Correctness of taking local path glob pattern request")
    void createPathReport(@Mock LogParser parser, @Mock ReportDisplayer displayer, @Mock FullReport template) {
        try {
            Path dir = Files.createTempDirectory(Path.of(""), "tmp");
            try {
                Files.createDirectory(dir.resolve("logs"));
                Files.write(Files.createFile(dir.resolve("log1.txt")), List.of("log11", "log12"));
                Files.write(Files.createFile(dir.resolve("logs", "log2.txt")), List.of("log21", "log22"));
                Files.write(Files.createFile(dir.resolve("logs", "log3.txt")), List.of("log31", "log32"));
                ArgumentCaptor<String> infoCaptor = ArgumentCaptor.forClass(String.class);
                LogReportCreator creator = new LogReportCreator(parser, displayer);

                creator.createReport(List.of(dir.resolve("**").toString(), dir.resolve("*").toString())
                    , List.of(), template);

                verify(parser, times(6)).apply(infoCaptor.capture());
                assertThat(infoCaptor.getAllValues()).containsExactlyInAnyOrder(
                    "log11", "log12", "log21", "log22", "log31", "log32"
                );
            } finally {
                try (Stream<Path> paths = Files.walk(dir)) {
                    paths.sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
                }
            }
        } catch (IOException e) {
            assertThat(false).isTrue().as("Couldn't create testing files");
        }
    }

    @Test
    @DisplayName("Correctness of taking url request")
    void createURLReport(@Mock LogParser parser, @Mock ReportDisplayer displayer, @Mock FullReport template) {
        ArgumentCaptor<String> infoCaptor = ArgumentCaptor.forClass(String.class);
        LogReportCreator creator = new LogReportCreator(parser, displayer);

        creator.createReport(List.of(
                "https://raw.githubusercontent.com"
                    + "/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs"),
            List.of(), template);

        verify(parser, times(51462)).apply(infoCaptor.capture());
        assertThat(infoCaptor.getAllValues().getFirst()).isEqualTo(
            "93.180.71.3 - - [17/May/2015:08:05:32 +0000] \"GET /downloads/product_1 HTTP/1.1\" 304 0"
                + " \"-\" \"Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.21)\"");
    }
}
