package backend.academy.logstat.model;

import it.unimi.dsi.fastutil.chars.CharPredicate;

public interface CharSource {
    void skipWhitespace();

    char next();

    void skip();

    boolean check(char expected);

    boolean check(CharPredicate predicate);

    void expect(char expected);

    void expectNotEmpty();

    boolean isEmpty();

    String getUntil(char expected);

    String getUntil(CharPredicate predicate);

    void close();
}
