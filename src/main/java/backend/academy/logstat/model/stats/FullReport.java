package backend.academy.logstat.model.stats;

import java.util.List;

public record FullReport(List<OneValueLogStat> singleStats, List<TopValueLogStat> topStats) {
    public void setBasic(BasicReport basicReport) {
        singleStats.forEach(stat -> stat.set(basicReport));
        topStats.forEach(stat -> stat.set(basicReport));
    }
}
