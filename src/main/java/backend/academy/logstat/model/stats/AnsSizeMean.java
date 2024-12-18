package backend.academy.logstat.model.stats;

public class AnsSizeMean implements OneValueLogStat {
    private double mean = 0;

    @Override
    public String value() {
        return mean + "b";
    }

    @Override
    public String description() {
        return "Mean answer size";
    }

    @Override
    public void set(BasicReport basicReport) {
        if (basicReport.totalAmount() == 0) {
            mean = 0;
            return;
        }
        mean = basicReport.ansSizes().long2LongEntrySet().stream().mapToLong(e -> e.getLongKey() * e.getLongValue())
            .sum() / (double) basicReport.totalAmount();
    }
}
