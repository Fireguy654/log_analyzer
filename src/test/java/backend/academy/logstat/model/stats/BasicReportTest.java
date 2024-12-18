package backend.academy.logstat.model.stats;

import backend.academy.logstat.model.LogNote;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.instancio.Select.field;

class BasicReportTest {
    @Test
    @DisplayName("Correctness of accepting of log note")
    void accept() {
        BasicReport basicReport = new BasicReport();
        LogNote noteA = Instancio.of(LogNote.class)
            .set(field("method"), "GET").set(field("resource"), "/resource1")
            .set(field("status"), 200).set(field("bytesCnt"), 50)
            .create();
        LogNote noteB = Instancio.of(LogNote.class)
            .set(field("method"), "POST").set(field("resource"), "/resource2")
            .set(field("status"), 404).set(field("bytesCnt"), 50)
            .create();

        basicReport.accept(noteA);
        basicReport.accept(noteA);
        basicReport.accept(noteB);

        assertThat(basicReport.totalAmount()).isEqualTo(3);
        assertThat(basicReport.methods()).contains(Map.entry("GET", 2L), Map.entry("POST", 1L));
        assertThat(basicReport.resources()).contains(Map.entry("/resource1", 2L), Map.entry("/resource2", 1L));
        assertThat(basicReport.ansCodes()).contains(Map.entry(200L, 2L), Map.entry(404L, 1L));
        assertThat(basicReport.ansSizes()).contains(Map.entry(50L, 3L));
    }

    @Test
    @DisplayName("Correctness of unmodifiable access")
    void unmodifiable() {
        BasicReport basicReport = new BasicReport();
        LogNote note = Instancio.of(LogNote.class)
            .set(field("method"), "GET").set(field("resource"), "/resource1")
            .set(field("status"), 200).set(field("bytesCnt"), 50)
            .create();
        basicReport.accept(note);

        Object2LongMap<String> resources = basicReport.resources();
        Object2LongMap<String> methods = basicReport.methods();
        Long2LongMap sizes = basicReport.ansSizes();
        Long2LongMap codes = basicReport.ansCodes();

        assertThatCode(() -> resources.put("smth", 0)).isExactlyInstanceOf(UnsupportedOperationException.class);
        assertThatCode(() -> methods.put("smth", 0)).isExactlyInstanceOf(UnsupportedOperationException.class);
        assertThatCode(() -> sizes.put(1, 2)).isExactlyInstanceOf(UnsupportedOperationException.class);
        assertThatCode(() -> codes.put(1, 2)).isExactlyInstanceOf(UnsupportedOperationException.class);
    }
}
