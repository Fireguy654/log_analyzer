package backend.academy.logstat.model.stats;

public class MethodTop extends TopValueLogStat {
    private static final int AMOUNT = 3;

    @Override
    public String header() {
        return "Most usual methods of requests";
    }

    @Override
    public void set(BasicReport basicReport) {
        values = getTopN(basicReport.methods(), AMOUNT);
    }
}
