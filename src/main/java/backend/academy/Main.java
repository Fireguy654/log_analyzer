package backend.academy;

import backend.academy.logstat.exception.LogStatException;
import backend.academy.logstat.model.LogCliParams;
import backend.academy.logstat.model.LogField;
import backend.academy.logstat.model.stats.AnsCodeTop;
import backend.academy.logstat.model.stats.AnsSizeMean;
import backend.academy.logstat.model.stats.AnsSizeMode;
import backend.academy.logstat.model.stats.AnsSizePerc;
import backend.academy.logstat.model.stats.FullReport;
import backend.academy.logstat.model.stats.MethodTop;
import backend.academy.logstat.model.stats.ResourceTop;
import backend.academy.logstat.model.stats.TotalAmount;
import backend.academy.logstat.service.LogReportCreator;
import backend.academy.logstat.service.display.AdocReportDisplayer;
import backend.academy.logstat.service.display.MarkdownReportDisplayer;
import backend.academy.logstat.service.display.ReportDisplayer;
import backend.academy.logstat.service.filter.ValueLogFilter;
import backend.academy.logstat.service.parser.NginxParser;
import com.beust.jcommander.JCommander;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    @SuppressWarnings("MagicNumber")
    public static void main(String[] args) {
        LogCliParams params = new LogCliParams();
        JCommander.newBuilder().addObject(params).build().parse(args);

        boolean correct = true;

        if (params.filterField() != null) {
            LogField field = LogField.fromString(params.filterField());
            if (params.filterValue() == null || field == null) {
                System.err.printf("Incorrect use for field filter '%s': null value pattern or unsupported field%n",
                    params.filterField());
                System.err.println("Supported fields: " + Arrays.deepToString(LogField.values()));
                correct = false;
            } else {
                params.filters().add(new ValueLogFilter(params.filterValue(), field));
            }
        }

        String filename = "result";
        FullReport template = new FullReport(
            List.of(new TotalAmount(), new AnsSizeMean(), new AnsSizeMode(), new AnsSizePerc(0.95)),
            List.of(new AnsCodeTop(), new MethodTop(), new ResourceTop())
        );
        try {
            ReportDisplayer displayer = null;
            if ("markdown".equalsIgnoreCase(params.format())) {
                displayer = new MarkdownReportDisplayer(Files.newBufferedWriter(Path.of(filename + ".md")));
            } else if ("adoc".equalsIgnoreCase(params.format())) {
                displayer = new AdocReportDisplayer(Files.newBufferedWriter(Path.of(filename + ".adoc")));
            } else {
                System.err.println("Unsupported format: " + params.format());
                correct = false;
            }

            if (!correct) {
                return;
            }
            try {
                LogReportCreator creator = new LogReportCreator(new NginxParser(), displayer);
                creator.createReport(params.resources(), params.filters(), template);
            } catch (LogStatException e) {
                System.err.println("Got error while collecting stats: " + e.getMessage());
            } finally {
                displayer.close();
            }
        } catch (IOException e) {
            System.err.println("Couldn't open file: " + filename);
        }
    }
}
