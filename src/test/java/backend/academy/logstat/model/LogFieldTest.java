package backend.academy.logstat.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class LogFieldTest {
    @Test
    @DisplayName("Correctness of equality with ignored case")
    void fromString() {
        LogField res = LogField.fromString("meThOd");
        LogField directRes = LogField.fromString("METHOD");

        assertThat(res).isEqualTo(LogField.METHOD);
        assertThat(directRes).isEqualTo(LogField.METHOD);
    }

    @Test
    @DisplayName("Correctness of getting absent field")
    void fromStringGetsAbsentField() {
        LogField res = LogField.fromString("something");

        assertThat(res).isNull();
    }
}
