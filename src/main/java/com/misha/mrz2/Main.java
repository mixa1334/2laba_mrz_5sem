package com.misha.mrz2;

import com.misha.mrz2.network.BidirectionalAssociativeMemory;
import com.misha.mrz2.network.Pattern;
import com.misha.mrz2.service.PatternReader;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;

public class Main {
    private static final Logger logger = LogManager.getLogger();

    public static void main(String arg[]) {
        BidirectionalAssociativeMemory network = new BidirectionalAssociativeMemory();
        var patternsX = PatternReader.readPatternsFromDirectory("src/main/resources/patterns/x");
        var patternsY = PatternReader.readPatternsFromDirectory("src/main/resources/patterns/y");
        for (int i = 0; i < patternsX.size(); i++) {
            network.learn((Pattern) patternsX.get(i).get(), (Pattern) patternsY.get(i).get());
        }
        var testPatternsX = PatternReader.readPatternsFromDirectory("src/main/resources/test_patterns/x");
        var testPatternsY = PatternReader.readPatternsFromDirectory("src/main/resources/test_patterns/y");
        for (Optional<Pattern> pattern : testPatternsX) {
            logger.log(Level.INFO, "input -> ");
            pattern.get().printPattern();
            logger.log(Level.INFO, "output -> ");
            network.searchPatternByX(pattern.get()).printPattern();
        }
        for (Optional<Pattern> pattern : testPatternsY) {
            logger.log(Level.INFO, "input -> ");
            pattern.get().printPattern();
            logger.log(Level.INFO, "output -> ");
            network.searchPatternByY(pattern.get()).printPattern();
        }
    }
}
