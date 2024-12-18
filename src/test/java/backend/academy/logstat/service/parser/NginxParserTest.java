package backend.academy.logstat.service.parser;

import backend.academy.logstat.model.LogNote;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;

class NginxParserTest {
    @Test
    @DisplayName("Nginx parser correctness")
    void apply() {
        NginxParser nginxParser = new NginxParser();

        LogNote ans1 = nginxParser.apply(
            "67.132.206.254 - - [04/Jun/2015:04:06:17 +0000] \"GET /downloads/product_1 HTTP/1.1\" 200 17632"
                + " \"-\" \"urlgrabber/3.1.0 yum/3.2.22\"");
        LogNote ans2 = nginxParser.apply("SMTH");

        assertThat(ans1).isEqualTo(
            new LogNote("67.132.206.254", "-",
                LocalDateTime.of(2015, 6, 4, 4, 6, 17),
                "GET", "/downloads/product_1", "HTTP/1.1",
                200, 17632, "-", "urlgrabber/3.1.0 yum/3.2.22")
        );
        assertThat(ans2).isNull();
    }
}
