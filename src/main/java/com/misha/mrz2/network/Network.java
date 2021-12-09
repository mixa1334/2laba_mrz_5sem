package network;

import service.Matrix;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private List<NetworkPattern> patternsX;
    private List<NetworkPattern> patternsY;
    private Matrix weights = null;
    private int rowsXToY;
    private int columnsXtoY;
    private int rowsYToX;
    private int columnsYtoX;

    public Network(int rowsXToY, int columnsXtoY, int rowsYToX, int columnsYtoX) {
        this.rowsXToY = rowsXToY;
        this.columnsXtoY = columnsXtoY;
        this.rowsYToX = rowsYToX;
        this.columnsYtoX = columnsYtoX;
        patternsX = new ArrayList<>();
        patternsY = new ArrayList<>();
    }

    public void learn() {
        if (patternsY.size() != patternsX.size()) {
            throw new IllegalArgumentException("patternsX and patternsY should have the smae size");
        }

        for (int i = 0; i < patternsX.size(); i++) {
            NetworkPattern firstPattern = patternsX.get(i);
            NetworkPattern secondPattern = patternsY.get(i);
            Matrix S = firstPattern.getVector();
            Matrix T = secondPattern.getVector();
            Matrix W = S.transpose().multiply(T).get();
            if (weights == null) {
                weights = W;
            } else {
                weights = weights.sum(W).get();
            }
        }
    }

    public void addPatternX(NetworkPattern pattern) {
        patternsX.add(pattern);
    }

    public void addPatternY(NetworkPattern pattern) {
        patternsY.add(pattern);
    }

    public void searchPatternByX(NetworkPattern pattern) {
        Matrix outputFirst = pattern.getVector().multiply(weights).get();
        activationFunction(outputFirst);
        System.out.println("Ассоциированный образ: ");
        printPattern(outputFirst, rowsYToX, columnsYtoX);
    }

    public void searchPatternByY(NetworkPattern pattern) {
        Matrix output = pattern.getVector().multiply(weights.transpose()).get();
        activationFunction(output);
        System.out.println("Ассоциированный образ: ");
        printPattern(output, rowsXToY, columnsXtoY);
    }

    private Matrix activationFunction(Matrix vector) {
        for (int i = 0; i < vector.getColumns(); i++) {
            double value = vector.getValue(0, i) > 0 ? 1 : -1;
            vector.setValue(value, 0, i);
        }
        return vector;
    }

    private void printPattern(Matrix vector, int rows, int columns) {
        int k = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                char value = vector.getValue(0, k++) == 1 ? '#' : '.';
                System.out.print(value);
            }
            System.out.println("");
        }
    }
}