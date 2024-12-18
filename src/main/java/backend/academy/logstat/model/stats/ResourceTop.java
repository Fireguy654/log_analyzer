package backend.academy.logstat.model.stats;

public class ResourceTop extends TopValueLogStat {
    private static final long AMOUNT = 5;

    @Override
    public String header() {
        return "Most requested resources";
    }

    @Override
    public void set(BasicReport basicReport) {
        values = TopValueLogStat.getTopN(basicReport.resources(), AMOUNT);
    }
}
