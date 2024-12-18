package backend.academy.logstat.model.stats;

import it.unimi.dsi.fastutil.longs.Long2LongLinkedOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongSortedMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OneValueLogStatTest {
    @Test
    @DisplayName("Total amount correctness")
    void getTotalAmount(@Mock BasicReport basicReport) {
        doReturn(10L).when(basicReport).totalAmount();
        OneValueLogStat totalAmount = new TotalAmount();

        totalAmount.set(basicReport);

        assertThat(totalAmount.value()).isEqualTo("10");
    }

    @Test
    @DisplayName("Answer size mean correctness")
    void getAnswerSizeMean(@Mock BasicReport basicReport) {
        Long2LongSortedMap values = new Long2LongLinkedOpenHashMap();
        values.put(1L, 2L);
        values.put(11L, 3L);
        doReturn(5L).when(basicReport).totalAmount();
        doReturn(values).when(basicReport).ansSizes();
        OneValueLogStat mean = new AnsSizeMean();

        mean.set(basicReport);

        assertThat(mean.value()).isEqualTo("7.0b");
    }

    @Test
    @DisplayName("Answer size mode correctness")
    void getAnswerSizeMode(@Mock BasicReport basicReport) {
        Long2LongSortedMap values = new Long2LongLinkedOpenHashMap();
        values.put(1L, 2L);
        values.put(11L, 3L);
        doReturn(values).when(basicReport).ansSizes();
        OneValueLogStat mode = new AnsSizeMode();

        mode.set(basicReport);

        assertThat(mode.value()).isEqualTo("11b");
    }

    @Test
    @DisplayName("Answer size perc correctness")
    void getAnswerSizePerc(@Mock BasicReport basicReport) {
        Long2LongSortedMap values = new Long2LongLinkedOpenHashMap();
        values.put(1L, 10L);
        values.put(5L, 4L);
        values.put(11L, 6L);
        doReturn(values).when(basicReport).ansSizes();
        doReturn(20L).when(basicReport).totalAmount();
        OneValueLogStat perc = new AnsSizePerc(0.75);

        perc.set(basicReport);

        assertThat(perc.value()).isEqualTo("11b");
    }
}
