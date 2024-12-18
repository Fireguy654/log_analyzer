package backend.academy.logstat.model.stats;

import lombok.Getter;

@Getter
public class TotalAmount implements OneValueLogStat {
    private long amount = 0;

    @Override
    public String value() {
        return Long.toString(amount);
    }

    @Override
    public String description() {
        return "Total amount of log entries";
    }

    @Override
    public void set(BasicReport basicReport) {
        amount = basicReport.totalAmount();
    }
}
