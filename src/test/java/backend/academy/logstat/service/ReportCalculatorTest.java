package backend.academy.logstat.service;

import backend.academy.logstat.model.LogField;
import backend.academy.logstat.model.stats.BasicReport;
import backend.academy.logstat.service.filter.ValueLogFilter;
import backend.academy.logstat.service.parser.NginxParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class ReportCalculatorTest {
    @Test
    @DisplayName("Correctness of collecting stats")
    void collectStats() {
        ReportCalculator calculator = new ReportCalculator(new NginxParser(),
            List.of(new ValueLogFilter("Debian.*", LogField.AGENT)));
        StringReader reader = new StringReader("""
            91.234.194.89 - - [17/May/2015:08:05:22 +0000] "GET /downloads/product_2 HTTP/1.1" 304 0 "-" "Debian APT-HTTP/1.3 (0.9.7.9)"
            80.91.33.133 - - [17/May/2015:08:05:07 +0000] "GET /downloads/product_1 HTTP/1.1" 304 0 "-" "Debian APT-HTTP/1.3 (0.8.16~exp12ubuntu10.17)"
            37.26.93.214 - - [17/May/2015:08:05:38 +0000] "GET /downloads/product_2 HTTP/1.1" 404 319 "-" "Go 1.1 package http"
            """);

        calculator.collectStats(new BufferedReader(reader));

        BasicReport report = calculator.stats();
        assertThat(report.totalAmount()).isEqualTo(2);
        assertThat(report.ansSizes()).containsExactly(Map.entry(0L, 2L));
        assertThat(report.methods()).containsExactly(Map.entry("GET", 2L));
        assertThat(report.ansCodes()).containsExactly(Map.entry(304L, 2L));
        assertThat(report.resources()).containsExactlyInAnyOrderEntriesOf(
            Map.of("/downloads/product_2", 1L,"/downloads/product_1", 1L));
    }
}
