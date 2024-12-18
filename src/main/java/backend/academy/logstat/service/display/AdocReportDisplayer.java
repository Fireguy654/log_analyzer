package backend.academy.logstat.service.display;

import backend.academy.logstat.model.stats.ValueWithScore;
import java.io.BufferedWriter;
import java.util.List;
import java.util.function.Function;

public class AdocReportDisplayer extends ReportDisplayer {
    public static final String TABLE_SEPARATOR = "|===";

    public AdocReportDisplayer(BufferedWriter writer) {
        super(writer);
    }

    @Override
    protected void printStep() {
        println();
    }

    @Override
    protected void printHeader(int level, String header) {
        println("=".repeat(level) + " " + header);
    }

    @Override
    protected <T> void printList(List<T> list, Function<? super T, String> mapper) {
        for (T element : list) {
            println("* " + mapper.apply(element));
        }
    }

    @Override
    protected void printTable(List<ValueWithScore<String>> values) {
        println(TABLE_SEPARATOR);
        println("|Value|Score");
        for (ValueWithScore<String> elem : values) {
            println();
            println(String.format("|%s|%d", elem.value(), elem.score()));
        }
        println(TABLE_SEPARATOR);
    }
}
