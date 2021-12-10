package com.misha.mrz2.network;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.Matrix;

public class BidirectionalAssociativeMemory {
    public static final Logger logger = LogManager.getLogger();
    private Matrix weights = null;
    private int rowsX;
    private int columnsX;
    private int rowsY;
    private int columnsY;

    public BidirectionalAssociativeMemory() {
    }

    public void learn(Pattern patternX, Pattern patternY) {
        Matrix X = patternX.getVector();
        Matrix Y = patternY.getVector();
        Matrix W = X.transpose().multiply(Y).get();
        if (weights == null) {
            weights = W;
            rowsX = patternX.getRows();
            rowsY = patternY.getRows();
            columnsX = patternX.getColumns();
            columnsY = patternY.getColumns();
        } else {
            weights = weights.sum(W).get();
        }
    }

    public Pattern searchPatternByX(Pattern pattern) {
        int energyPast;
        int energy = 0;
        int iteration = -1;
        Matrix X = pattern.getVector();
        Matrix Y;
        do {
            energyPast = energy;
            Y = X.multiply(weights).get();
            activationFunction(Y);
            X = Y.multiply(weights.transpose()).get();
            activationFunction(X);
            energy = calculateEnergy(Y, X);
            iteration++;
        } while (energy != energyPast);
        logger.log(Level.INFO, "iterations- " + iteration);
        return new Pattern(Y, rowsY, columnsY);
    }

    public Pattern searchPatternByY(Pattern pattern) {
        int energyPast;
        int energy = 0;
        int iteration = -1;
        Matrix X;
        Matrix Y = pattern.getVector();
        do {
            energyPast = energy;
            X = Y.multiply(weights.transpose()).get();
            activationFunction(X);
            Y = X.multiply(weights).get();
            activationFunction(Y);
            energy = calculateEnergy(Y, X);
            iteration++;
        } while (energy != energyPast);
        logger.log(Level.INFO, "iterations- " + iteration);
        return new Pattern(X, rowsX, columnsX);
    }

    private void activationFunction(Matrix vector) {
        for (int i = 0; i < vector.getColumns(); i++) {
            double value = vector.getValue(0, i) > 0 ? 1 : -1;
            vector.setValue(value, 0, i);
        }
    }

    private int calculateEnergy(Matrix first, Matrix second) {
        int energy = 0;
        Matrix temp = first.multiply(weights.transpose()).get();
        Matrix E = temp.multiply(second.transpose()).get();
        for (int i = 0; i < E.getRows(); i++) {
            for (int j = 0; j < E.getColumns(); j++) {
                energy += E.getValue(i, j);
            }
        }
        return energy;
    }
}