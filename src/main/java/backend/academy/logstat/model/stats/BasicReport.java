package backend.academy.logstat.model.stats;

import backend.academy.logstat.model.LogNote;
import it.unimi.dsi.fastutil.longs.Long2LongAVLTreeMap;
import it.unimi.dsi.fastutil.longs.Long2LongMap;
import it.unimi.dsi.fastutil.longs.Long2LongSortedMap;
import it.unimi.dsi.fastutil.longs.Long2LongSortedMaps;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongMaps;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.function.Consumer;
import lombok.Getter;

public class BasicReport implements Consumer<LogNote> {
    @Getter
    private long totalAmount = 0;
    private final Long2LongSortedMap ansCodes = new Long2LongAVLTreeMap();
    private final Long2LongSortedMap ansSizes = new Long2LongAVLTreeMap();
    private final Object2LongMap<String> resources = new Object2LongOpenHashMap<>();
    private final Object2LongMap<String> methods = new Object2LongOpenHashMap<>();

    @Override
    public void accept(LogNote logNote) {
        ++totalAmount;
        addKey(ansCodes, logNote.status());
        addKey(ansSizes, logNote.bytesCnt());
        addKey(resources, logNote.resource());
        addKey(methods, logNote.method());
    }

    public Long2LongSortedMap ansCodes() {
        return Long2LongSortedMaps.unmodifiable(ansCodes);
    }

    public Long2LongSortedMap ansSizes() {
        return Long2LongSortedMaps.unmodifiable(ansSizes);
    }

    public Object2LongMap<String> resources() {
        return Object2LongMaps.unmodifiable(resources);
    }

    public Object2LongMap<String> methods() {
        return Object2LongMaps.unmodifiable(methods);
    }

    private static void addKey(Long2LongMap map, long key) {
        map.put(key, map.getOrDefault(key, 0L) + 1);
    }

    private static <T> void addKey(Object2LongMap<T> map, T key) {
        map.put(key, map.getOrDefault(key, 0L) + 1);
    }
}
