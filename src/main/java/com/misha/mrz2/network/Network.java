package com.misha.mrz2.network;

import com.misha.mrz2.service.Matrix;

import java.util.ArrayList;
import java.util.List;

public class Network {
    private List<NetworkPattern> patterns;
    private List<Matrix> listOfWeights;
    private Matrix weights;
    private int rowsXToY;
    private int columnsXtoY;
    private int rowsYToX;
    private int columnsYtoX;

    public Network() {
        patterns = new ArrayList<>();
        listOfWeights = new ArrayList<>();
    }

    public void learn() {
        int i = 0;
        while (i < patterns.size()) {
            NetworkPattern firstPattern = patterns.get(i);
            NetworkPattern secondPattern = patterns.get(i + 1);
            associatePair(firstPattern, secondPattern);
            i += 2;
        }
        calculateWeight();
    }

    public void addPattern(NetworkPattern pattern) {
        patterns.add(pattern);
    }

    private void associatePair(NetworkPattern firstPattern, NetworkPattern secondPattern) {
        Matrix S = firstPattern.getVector();
        Matrix T = secondPattern.getVector();
        Matrix W = S.transpose().multiply(T).get();
        this.rowsXToY = firstPattern.getMatrix().getRows();
        this.columnsXtoY = firstPattern.getMatrix().getColumns();
        this.rowsYToX = secondPattern.getMatrix().getRows();
        this.columnsYtoX = secondPattern.getMatrix().getColumns();

        listOfWeights.add(W);
    }

    private void calculateWeight() {
        double[][] array = new double[listOfWeights.get(0).getRows()][listOfWeights.get(0).getColumns()];
        weights = new Matrix(array);
        for (Matrix weightOfPair : listOfWeights) {
            weights = weights.add(weightOfPair).get();
        }
    }

    public void searchPatternByX(NetworkPattern pattern) {
        int energyPast = -1;
        int energy = 0;
        int iteration = 0;
        Matrix outputFirst = outputFirstInputSecond(pattern.getVector());
        while (energy != energyPast) {
            energyPast = energy;
            Matrix outputSecond = outputSecondInputFirst(outputFirst);
            outputFirst = outputSecond.multiply(weights).get();
            outputFirst = activationFunction(outputFirst);
            energy = calculateEnergy(outputFirst, outputSecond);
            iteration++;
        }
//        outputFirst.print();
        System.out.println("Ассоциированный образ: ");
        printPatternY(outputFirst);
        System.out.println("Number of iterations: " + iteration);
    }

    public void searchPatternByY(NetworkPattern pattern) {
        int energyPast = -1;
        int energy = 0;
        int iteration = 0;
        Matrix outputSecondLayer = outputSecondInputFirst(pattern.getVector());
        while (energy != energyPast) {
            energyPast = energy;
            Matrix outputFirstLayer = outputFirstInputSecond(outputSecondLayer);
            outputSecondLayer = outputFirstLayer.multiply(weights.transpose()).get();
            outputSecondLayer = activationFunction(outputSecondLayer);
            energy = calculateEnergy(outputFirstLayer, outputSecondLayer);
            iteration++;
        }
        System.out.println("Ассоциированный образ: ");
        printPatternX(outputSecondLayer);
        System.out.println("Number of iterations: " + iteration);
    }

    private Matrix outputFirstInputSecond(Matrix inputVector) {
        Matrix Y = inputVector.multiply(weights).get();
        return activationFunction(Y);
    }

    private Matrix outputSecondInputFirst(Matrix inputVector) {
        Matrix X = inputVector.multiply(weights.transpose()).get();
        return activationFunction(X);
    }

    private Matrix activationFunction(Matrix vector) {
        for (int i = 0; i < vector.getColumns(); i++) {
            double value = vector.getValue(0, i) > 0 ? 1 : -1;
            vector.setValue(value, 0, i);
        }
        return vector;
    }

    private int calculateEnergy(Matrix outputFirstLayer, Matrix outputSecondLayer) {
        int energy = 0;
        Matrix temp = outputFirstLayer.multiply(weights.transpose()).get();
        Matrix E = temp.multiply(outputSecondLayer.transpose()).get();
        for (int i = 0; i < E.getRows(); i++) {
            for (int j = 0; j < E.getColumns(); j++) {
                energy += E.getValue(i, j);
            }
        }
        return energy;
    }

    private void printPatternY(Matrix vector) {
        double[][] array = new double[rowsYToX][columnsYtoX];
        int k = 0;
        for (int i = 0; i < rowsYToX; i++) {
            for (int j = 0; j < columnsYtoX; j++) {
                if (k == vector.getColumns()) break;
                array[i][j] = vector.getValue(0, k);
                k++;
            }
        }
        Matrix output = new Matrix(array);
        printPattern(output);
    }

    private void printPatternX(Matrix vector) {
        double[][] array = new double[rowsXToY][columnsXtoY];
        int k = 0;
        for (int i = 0; i < rowsXToY; i++) {
            for (int j = 0; j < columnsXtoY; j++) {
                if (k == vector.getColumns()) break;
                array[i][j] = vector.getValue(0, k);
                k++;
            }
        }
        Matrix output = new Matrix(array);
        printPattern(output);
    }

    private void printPattern(Matrix bipolarMatrix) {
        for (int i = 0; i < bipolarMatrix.getRows(); i++) {
            for (int j = 0; j < bipolarMatrix.getColumns(); j++) {
                if (bipolarMatrix.getValue(i, j) == 1) {
                    System.out.print("#");
                } else if (bipolarMatrix.getValue(i, j) == -1) {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
    }
}
