package backend.academy.logstat.model.stats;

import it.unimi.dsi.fastutil.longs.Long2LongAVLTreeMap;
import it.unimi.dsi.fastutil.longs.Long2LongSortedMap;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TopValueLogStatTest {
    @Test
    @DisplayName("Resources top correctness")
    void resTop(@Mock BasicReport basicReport) {
        Object2LongMap<String> values = new Object2LongOpenHashMap<>();
        values.put("res1", 15);
        values.put("res2", 20);
        values.put("res3", 5);
        values.put("res4", 10);
        values.put("res5", 1);
        values.put("res6", 200);
        doReturn(values).when(basicReport).resources();
        TopValueLogStat resTop = new ResourceTop();

        resTop.set(basicReport);
        List<ValueWithScore<String>> res = resTop.values();

        assertThat(res).containsExactly(
            new ValueWithScore<>("res6", 200),
            new ValueWithScore<>("res2", 20),
            new ValueWithScore<>("res1", 15),
            new ValueWithScore<>("res4", 10),
            new ValueWithScore<>("res3", 5));
    }

    @Test
    @DisplayName("Answer code top correctness")
    void answerCodeTop(@Mock BasicReport basicReport) {
        Long2LongSortedMap values = new Long2LongAVLTreeMap();
        values.put(200, 50);
        values.put(201, 40);
        values.put(202, 10);
        values.put(203, 15);
        values.put(204, 51);
        values.put(404, 2000);
        doReturn(values).when(basicReport).ansCodes();
        TopValueLogStat ansCodeTop = new AnsCodeTop();

        ansCodeTop.set(basicReport);
        List<ValueWithScore<String>> res = ansCodeTop.values();

        assertThat(res).containsExactly(
            new ValueWithScore<>("404(Not Found)", 2000),
            new ValueWithScore<>("204(No Content)", 51),
            new ValueWithScore<>("200(OK)", 50),
            new ValueWithScore<>("201(Created)", 40),
            new ValueWithScore<>("203(Non-Authoritative Information)", 15)
        );
    }

    @Test
    @DisplayName("Method top correctness")
    void methodTop(@Mock BasicReport basicReport) {
        Object2LongMap<String> values = new Object2LongOpenHashMap<>();
        values.put("GET", 15);
        values.put("POST", 20);
        values.put("DELETE", 5);
        values.put("HEAD", 1000);
        doReturn(values).when(basicReport).methods();
        TopValueLogStat methodTop = new MethodTop();

        methodTop.set(basicReport);
        List<ValueWithScore<String>> res = methodTop.values();

        assertThat(res).containsExactly(
            new ValueWithScore<>("HEAD", 1000),
            new ValueWithScore<>("POST", 20),
            new ValueWithScore<>("GET", 15)
        );
    }

    @Test
    @DisplayName("Get top N correctness method")
    void getTopN() {
        Stream<ValueWithScore<String>> values = Stream.of(
            new ValueWithScore<>("Almost best", 100),
            new ValueWithScore<>("Worst", 1),
            new ValueWithScore<>("Best", 1000)
        );

        List<ValueWithScore<String>> res = TopValueLogStat.getTopN(values, 2);

        assertThat(res).containsExactly(
            new ValueWithScore<>("Best", 1000),
            new ValueWithScore<>("Almost best", 100)
        );
    }
}
