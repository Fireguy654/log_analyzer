package backend.academy.logstat.model.stats;

import it.unimi.dsi.fastutil.longs.Long2LongMap;

public class AnsSizeMode implements OneValueLogStat {
    private long mode = 0;

    @Override
    public String value() {
        return mode + "b";
    }

    @Override
    public String description() {
        return "Mode of answer sizes";
    }

    @Override
    public void set(BasicReport basicReport) {
        long curMode = 0;
        long amount = 0;
        for (Long2LongMap.Entry entry : basicReport.ansSizes().long2LongEntrySet()) {
            if (entry.getLongValue() > amount) {
                curMode = entry.getLongKey();
                amount = entry.getLongValue();
            }
        }
        this.mode = curMode;
    }
}
