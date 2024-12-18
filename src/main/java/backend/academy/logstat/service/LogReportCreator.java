package backend.academy.logstat.service;

import backend.academy.logstat.exception.IOLogStatException;
import backend.academy.logstat.exception.IllegalArgLogStatException;
import backend.academy.logstat.model.stats.FullReport;
import backend.academy.logstat.service.display.ReportDisplayer;
import backend.academy.logstat.service.filter.LogFilter;
import backend.academy.logstat.service.parser.LogParser;
import com.google.common.base.Predicates;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.PatternSyntaxException;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class LogReportCreator {
    private final LogParser parser;
    private final ReportDisplayer displayer;

    public void createReport(List<String> resources, List<LogFilter> filters, FullReport template) {
        Set<String> visited = HashSet.newHashSet(resources.size());
        ReportCalculator calculator = new ReportCalculator(parser, filters);
        Stream<String> resResources = Stream.empty();
        for (String resource : resources) {
            resResources = Stream.concat(resResources, addResource(resource, visited, calculator).stream());
        }
        template.setBasic(calculator.stats());
        displayer.printReport(template, resResources.toList(), filters);
    }

    private List<String> addResource(String resource, Set<String> visited, ReportCalculator calculator) {
        try {
            return addResource(new URI(resource), visited, calculator);
        } catch (URISyntaxException | IllegalArgumentException urlException) {
            try {
                PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:" + resource);
                return addResource(matcher, visited, calculator);
            } catch (UnsupportedOperationException | PatternSyntaxException patternException) {
                patternException.addSuppressed(urlException);
                throw new IllegalArgLogStatException(
                    String.format("'%s' is nor valid url, neither valid glob", resource), patternException);
            }
        }
    }

    private List<String> addResource(URI resource, Set<String> visited, ReportCalculator calculator) {
        if (visited.contains(resource.toString())) {
            return List.of();
        }
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpResponse<InputStream> response = client.send(
                HttpRequest.newBuilder().uri(resource).build(),
                HttpResponse.BodyHandlers.ofInputStream()
            );
            calculator.collectStats(new BufferedReader(new InputStreamReader(response.body(), StandardCharsets.UTF_8)));
            visited.add(resource.toString());
            return List.of(resource.toString());
        } catch (IOException | InterruptedException e) {
            throw new IOLogStatException(String.format("Couldn't read from URL '%s'", resource), e);
        }
    }

    private List<String> addResource(PathMatcher matcher, Set<String> visited, ReportCalculator calculator) {
        try (Stream<Path> matched = Files.find(Path.of(""), Integer.MAX_VALUE,
            (path, attrs) -> matcher.matches(path) && attrs.isRegularFile() && !visited.contains(path.toString()))) {
            return matched.map(path -> {
                try {
                    calculator.collectStats(Files.newBufferedReader(path));
                    visited.add(path.toString());
                    return path.toString();
                } catch (IOException e) {
                    log.error("Couldn't read from path: {}", path, e);
                    return null;
                }
            }).filter(Predicates.notNull()).toList();
        } catch (IOException | UncheckedIOException e) {
            throw new IOLogStatException("Couldn't walk throw directories", e);
        }
    }
}
