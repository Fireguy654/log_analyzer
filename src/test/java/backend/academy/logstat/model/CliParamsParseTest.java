package backend.academy.logstat.model;

import com.beust.jcommander.JCommander;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;

class CliParamsParseTest {
    @Test
    @DisplayName("Correctness of collecting resources")
    void resources() {
        String[] args = {"--path", "t.txt", "b.txt", "--path", "tmplog.txt", "c.txt"};
        LogCliParams logCliParams = new LogCliParams();

        JCommander.newBuilder().addObject(logCliParams).build().parse(args);

        assertThat(logCliParams.resources()).containsExactlyInAnyOrder("t.txt", "b.txt", "tmplog.txt", "c.txt");
    }

    @Test
    @DisplayName("Correctness of parsing date")
    void dates() {
        String[] args = {"--from", "2022-08-01", "--to", "2022-08-31T08:12:32"};
        LogCliParams logCliParams = new LogCliParams();
        LogNote corNote =
            Instancio.of(LogNote.class)
            .set(field("dateTime"), LocalDateTime.of(2022, 8, 20, 0, 0))
                .create();
        LogNote prevNote =
            Instancio.of(LogNote.class)
                .set(field("dateTime"), LocalDateTime.of(2022, 8, 1, 0, 0, 0))
                .create();
        LogNote pastNote =
            Instancio.of(LogNote.class)
                .set(field("dateTime"), LocalDateTime.of(2022, 8, 31, 8, 12, 32))
                .create();


        JCommander.newBuilder().addObject(logCliParams).build().parse(args);

        assertThat(logCliParams.filters()).allMatch(filter -> filter.test(corNote));
        assertThat(logCliParams.filters()).anyMatch(filter -> !filter.test(prevNote));
        assertThat(logCliParams.filters()).anyMatch(filter -> !filter.test(pastNote));
    }
}
