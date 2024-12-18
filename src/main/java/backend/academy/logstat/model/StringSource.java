package backend.academy.logstat.model;

import backend.academy.logstat.exception.SourceException;
import it.unimi.dsi.fastutil.chars.CharPredicate;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StringSource implements CharSource {
    private final String source;
    private int ind = 0;

    @Override
    public void skipWhitespace() {
        while (!isEmpty() && Character.isWhitespace(source.charAt(ind))) {
            ++ind;
        }
    }

    @Override
    public char next() {
        expectNotEmpty();
        return source.charAt(ind++);
    }

    @Override
    public void skip() {
        expectNotEmpty();
        ++ind;
    }

    @Override
    public boolean check(char expected) {
        return check(c -> c == expected);
    }

    @Override
    public boolean check(CharPredicate predicate) {
        return !isEmpty() && predicate.test(source.charAt(ind));
    }

    @Override
    public void expect(char expected) {
        expectNotEmpty();
        if (!check(expected)) {
            throw new SourceException(String.format("Expected '%c' instead of '%c' in '%s' at index %d",
                expected, source.charAt(ind), source, ind));
        } else {
            ++ind;
        }
    }

    @Override
    public void expectNotEmpty() {
        if (ind == source.length()) {
            throw new SourceException("Expected not empty source from empty one");
        }
    }

    @Override
    public boolean isEmpty() {
        return ind == source.length();
    }

    @Override
    public String getUntil(char expected) {
        return getUntil(c -> c == expected);
    }

    @Override
    public String getUntil(CharPredicate predicate) {
        StringBuilder sb = new StringBuilder();
        while (!isEmpty() && !predicate.test(source.charAt(ind))) {
            sb.append(source.charAt(ind++));
        }
        return sb.toString();
    }

    @Override
    public void close() {}
}
