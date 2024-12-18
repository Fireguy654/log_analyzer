package backend.academy.logstat.model.stats;

import it.unimi.dsi.fastutil.longs.Long2LongMap;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnsSizePerc implements OneValueLogStat {
    private final double alpha;
    private long percentile = 0;

    @Override
    public String value() {
        return percentile + "b";
    }

    @SuppressWarnings("MagicNumber")
    @Override
    public String description() {
        return String.format("The %.2f percentile of answer sizes", alpha * 100);
    }

    @Override
    public void set(BasicReport basicReport) {
        long curAmount = 0;
        double pos = alpha * basicReport.totalAmount();
        for (Long2LongMap.Entry entry : basicReport.ansSizes().long2LongEntrySet()) {
            curAmount += entry.getLongValue();
            if (curAmount > pos) {
                percentile = entry.getLongKey();
                return;
            }
        }
    }
}
