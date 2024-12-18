package backend.academy.logstat.service.filter;

import backend.academy.logstat.model.LogField;
import backend.academy.logstat.model.LogNote;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

class LogFilterTest {
    @Test
    @DisplayName("To date log filter correctness")
    void toDateLogFilter() {
        LogNote goodNote = Instancio.of(LogNote.class).set(field("dateTime"),
            LocalDateTime.of(2022, 11, 29, 0, 0, 0)).create();
        LogNote badNote = Instancio.of(LogNote.class).set(field("dateTime"),
            LocalDateTime.of(2022, 11, 29, 0, 0, 1)).create();
        LogFilter toDate = new ToLogFilter(LocalDateTime.of(2022, 11, 29, 0, 0, 1));

        assertThat(toDate.test(goodNote)).isTrue();
        assertThat(toDate.test(badNote)).isFalse();
    }

    @Test
    @DisplayName("From date log filter correctness")
    void fromDateLogFilter() {
        LogNote badNote = Instancio.of(LogNote.class).set(field("dateTime"),
            LocalDateTime.of(2022, 11, 29, 0, 0, 0)).create();
        LogNote goodNote = Instancio.of(LogNote.class).set(field("dateTime"),
            LocalDateTime.of(2022, 11, 29, 0, 0, 1)).create();
        LogFilter toDate = new FromLogFilter(LocalDateTime.of(2022, 11, 29, 0, 0, 0));

        assertThat(toDate.test(goodNote)).isTrue();
        assertThat(toDate.test(badNote)).isFalse();
    }

    @Test
    @DisplayName("Value log filter correctness")
    void valueLogFilter() {
        LogNote goodNote = Instancio.of(LogNote.class).set(field("usrAgent"), "Mozilla...").create();
        LogNote badNote = Instancio.of(LogNote.class).set(field("usrAgent"), "Debian...").create();
        LogFilter toDate = new ValueLogFilter("Mozilla.*", LogField.AGENT);

        assertThat(toDate.test(goodNote)).isTrue();
        assertThat(toDate.test(badNote)).isFalse();
    }
}
