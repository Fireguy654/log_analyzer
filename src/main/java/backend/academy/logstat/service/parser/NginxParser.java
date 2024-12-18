package backend.academy.logstat.service.parser;

import backend.academy.logstat.exception.SourceException;
import backend.academy.logstat.model.CharSource;
import backend.academy.logstat.model.LogNote;
import backend.academy.logstat.model.StringSource;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

@Log4j2
public class NginxParser implements LogParser {
    private static final DateTimeFormatter DT_FORMATTER =
        DateTimeFormatter.ofPattern("dd'/'MMM'/'yyyy':'HH':'mm':'ss' 'xx").localizedBy(Locale.ENGLISH);

    @Override
    public LogNote apply(String s) {
        try {
            CharSource source = new StringSource(s);
            String remAddr = getElementUntilExpected(source, '-');
            String remUsr = getElementUntilExpected(source, '[');
            LocalDateTime dateTime = getDateTime(source, ']');
            String[] req = StringUtils.split(getQuotBounded(source));
            int status = getNumber(source);
            int bytesCnt = getNumber(source);
            String ref = getQuotBounded(source);
            String usrAgent = getQuotBounded(source);
            return new LogNote(remAddr, remUsr, dateTime, req[0], req[1], req[2], status, bytesCnt, ref, usrAgent);
        } catch (SourceException e) {
            log.error(String.format("Couldn't parse '%s'. Returning null.", s), e);
        } catch (ArrayIndexOutOfBoundsException e) {
            log.error(
                String.format("Invalid request format. Expected 3 tokens in request string in %s. Returning null", s),
                e);
        }
        return null;
    }

    private String getElementUntilExpected(CharSource source, char c) {
        source.skipWhitespace();
        String res = source.getUntil(c).stripTrailing();
        source.expect(c);
        return res;
    }

    private String getQuotBounded(CharSource source) {
        source.skipWhitespace();
        source.expect('"');
        String res = source.getUntil('"');
        source.expect('"');
        return res;
    }

    private int getNumber(CharSource source) {
        source.skipWhitespace();
        String res = source.getUntil(Character::isWhitespace);
        try {
            return Integer.parseInt(res);
        } catch (NumberFormatException e) {
            throw new SourceException(String.format("Couldn't parse number '%s'", res), e);
        }
    }

    private LocalDateTime getDateTime(CharSource source, char c) {
        String res = getElementUntilExpected(source, c);
        try {
            return LocalDateTime.parse(res, DT_FORMATTER);
        } catch (DateTimeException e) {
            throw new SourceException(String.format("Couldn't parse datetime '%s'", res), e);
        }
    }
}
