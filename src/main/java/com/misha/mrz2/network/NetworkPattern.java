package com.misha.mrz2.network;

import com.misha.mrz2.service.Matrix;

public class NetworkPattern {
    private Matrix matrix;
    private Matrix vector;
    private String[] string;

    public NetworkPattern(String[] string) {
        this.string = string;
        convertToBipolarMatrix();
    }

    private void convertToBipolarMatrix() {
        double[][] matrix = new double[string.length][string[0].length()];
        double[][] vector = new double[1][string.length * string[0].length()];
        int i = 0;
        for (int row = 0; row < string.length; row++) {
            String line = string[row];
            for (int column = 0; column < line.length(); column++) {
                double value = line.charAt(column) == '#' ? 1 : -1;
                matrix[row][column] = value;
                vector[0][i] = value;
                i++;
            }
        }
        this.matrix = new Matrix(matrix);
        this.vector = new Matrix(vector);
    }

    public void printPattern() {
        for (int lines = 0; lines < string.length; lines++) {
            String line = string[lines];
            for (int column = 0; column < line.length(); column++) {
                System.out.print(line.charAt(column));
            }
            System.out.println("");
        }
        System.out.println("\n");
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public Matrix getVector() {
        return vector;
    }
}
