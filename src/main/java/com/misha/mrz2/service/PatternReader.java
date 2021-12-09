package com.misha.mrz2.service;

import com.misha.mrz2.network.Pattern;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class PatternReader {
    public static List<Optional<Pattern>> readPatternsFromDirectory(String fileName) {
        List<Optional<Pattern>> result = new ArrayList<>();
        try (Stream<Path> paths = Files.walk(Paths.get(fileName))) {
            result = paths.filter(Files::isRegularFile).map(p -> {
                Optional<Pattern> pattern = Optional.empty();
                try (Stream<String> strings = Files.lines(p)) {
                    pattern = Optional.of(new Pattern(strings.toArray(String[]::new)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return pattern;
            }).toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
