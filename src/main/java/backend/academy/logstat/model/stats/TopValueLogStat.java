package backend.academy.logstat.model.stats;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.LongFunction;
import java.util.stream.Stream;

public abstract class TopValueLogStat implements LogStat {
    protected List<ValueWithScore<String>> values = List.of();

    public List<ValueWithScore<String>> values() {
        return Collections.unmodifiableList(values);
    }

    public abstract String header();

    protected static <U> List<ValueWithScore<U>> getTopN(Object2LongMap<U> values, long amount) {
        return getTopN(
            values.object2LongEntrySet().stream()
                .map(entry -> new ValueWithScore<>(entry.getKey(), entry.getLongValue())),
            amount
        );
    }

    protected static <U> List<ValueWithScore<U>> getTopN(Long2LongMap values, LongFunction<U> mapper, long amount) {
        return getTopN(
            values.long2LongEntrySet().stream()
                .map(entry -> new ValueWithScore<>(mapper.apply(entry.getLongKey()), entry.getLongValue())),
            amount);
    }

    protected static <U> List<ValueWithScore<U>> getTopN(Stream<ValueWithScore<U>> values, long amount) {
        return values.sorted(Comparator.reverseOrder()).limit(amount).toList();
    }
}
