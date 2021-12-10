package com.misha.mrz2.network;

import service.Matrix;

public class BidirectionalAssociativeMemory {
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
        Matrix result = pattern.getVector().multiply(weights).get();
        activationFunction(result);
        return new Pattern(result, rowsY, columnsY);
    }

    public Pattern searchPatternByY(Pattern pattern) {
        Matrix result = pattern.getVector().multiply(weights.transpose()).get();
        activationFunction(result);
        return new Pattern(result, rowsX, columnsX);
    }

    private void activationFunction(Matrix vector) {
        for (int i = 0; i < vector.getColumns(); i++) {
            double value = vector.getValue(0, i) > 0 ? 1 : -1;
            vector.setValue(value, 0, i);
        }
    }
}