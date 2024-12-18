package backend.academy.logstat.model.stats;

public record ValueWithScore<T>(T value, long score) implements Comparable<ValueWithScore<T>> {
    @Override
    public int compareTo(ValueWithScore<T> o) {
        return Long.compare(score, o.score);
    }
}
