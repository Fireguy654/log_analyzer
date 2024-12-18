package backend.academy.logstat.model;

import java.time.LocalDateTime;

@SuppressWarnings("RecordComponentNumber")
public record LogNote(String remAddr, String remUsr, LocalDateTime dateTime, String method, String resource,
                      String protocol, int status, long bytesCnt, String ref, String usrAgent) {
}
