package backend.academy.logstat.model;

import backend.academy.logstat.exception.SourceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.*;

class StringSourceTest {
    @Test
    @DisplayName("Correctness of skipping whitespaces")
    void skipWhitespace() {
        StringSource stringSource = new StringSource("    \t  " + System.lineSeparator() + " ");

        stringSource.skipWhitespace();

        assertThat(stringSource.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Correctness of getting one char")
    void next() {
        StringSource stringSource = new StringSource("H");

        char result = stringSource.next();

        assertThat(result).isEqualTo('H');
        assertThat(stringSource.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Correctness of skipping one char")
    void skip() {
        StringSource stringSource = new StringSource("H");

        stringSource.skip();

        assertThat(stringSource.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Correctness of checking a char")
    void check() {
        StringSource a = new StringSource("H");
        StringSource b = new StringSource("g");
        StringSource c = new StringSource("");

        boolean resA = a.check('H');
        boolean resB = b.check('H');
        boolean resC = c.check('H');

        assertThat(resA).isTrue();
        assertThat(resB).isFalse();
        assertThat(resC).isFalse();
    }

    @Test
    @DisplayName("Correctness of true expecting")
    void trueExpect() {
        assertThatCode(() -> {
            StringSource a = new StringSource("H");

            a.expect('H');
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Correctness of false expecting")
    void falseExpect() {
        assertThatCode(() -> {
            StringSource a = new StringSource("H");

            a.expect('g');
        }).isExactlyInstanceOf(SourceException.class).hasMessage("Expected 'g' instead of 'H' in 'H' at index 0");
    }

    @Test
    @DisplayName("Correctness of true expecting non emptiness")
    void expectTrueNotEmpty() {
        assertThatCode(() -> {
            StringSource a = new StringSource("H");

            a.expectNotEmpty();
        }).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Correctness of false expecting non emptiness")
    void expectFalseNotEmpty() {
        assertThatCode(() -> {
            StringSource a = new StringSource("");

            a.expectNotEmpty();
        }).isExactlyInstanceOf(SourceException.class).hasMessage("Expected not empty source from empty one");
    }

    @Test
    @DisplayName("Correctness of emptiness")
    void isEmpty() {
        StringSource a = new StringSource("H");
        StringSource b = new StringSource("");

        boolean resA = a.isEmpty();
        boolean resB = b.isEmpty();

        assertThat(resA).isFalse();
        assertThat(resB).isTrue();
    }

    @Test
    @DisplayName("Correctness of taking String until some char")
    void getUntilChar() {
        StringSource a = new StringSource("Hangman");
        StringSource b = new StringSource("Hahaha");

        String resA = a.getUntil('n');
        String resB = b.getUntil('n');

        assertThat(resA).isEqualTo("Ha");
        assertThat(a.next()).isEqualTo('n');
        assertThat(resB).isEqualTo("Hahaha");
        assertThat(b.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Correctness of getting until predicate is true")
    void getUntilPredicate() {
        StringSource a = new StringSource("Hangman");
        StringSource b = new StringSource("Hahaha");

        String resA = a.getUntil(c -> c == 'n');
        String resB = b.getUntil(c -> c == 'n');

        assertThat(resA).isEqualTo("Ha");
        assertThat(a.next()).isEqualTo('n');
        assertThat(resB).isEqualTo("Hahaha");
        assertThat(b.isEmpty()).isTrue();
    }
}
