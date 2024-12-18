package backend.academy.logstat.model.stats;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class FullReportTest {
    @Test
    void setBasic(@Mock OneValueLogStat singleStat, @Mock TopValueLogStat topStat) {
        BasicReport basicReport = new BasicReport();
        FullReport report = new FullReport(List.of(singleStat), List.of(topStat));

        report.setBasic(basicReport);

        verify(singleStat, times(1)).set(basicReport);
        verify(topStat, times(1)).set(basicReport);
    }
}
