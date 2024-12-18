package backend.academy.logstat.model.stats;

public interface OneValueLogStat extends LogStat {
    String value();

    String description();
}
