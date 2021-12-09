package network;

import service.Matrix;

public class NetworkPattern {
    private Matrix vector;
    private String[] string;
    private int rows;
    private int columns;

    public NetworkPattern(Matrix vector, int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.vector = vector;
        convertToString();
    }

    public NetworkPattern(String[] string) {
        this.string = string;
        rows = string.length;
        columns = string[0].length();
        convertToVector();
    }

    private void convertToVector() {
        double[][] vector = new double[1][rows * columns];
        int i = 0;
        for (String line : string) {
            for (int column = 0; column < line.length(); column++) {
                double value = line.charAt(column) == '#' ? 1 : -1;
                vector[0][i++] = value;
            }
        }
        this.vector = new Matrix(vector);
    }

    private void convertToString() {
        string = new String[rows];
        int k = 0;
        for (int i = 0; i < rows; i++) {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < columns; j++) {
                char value = vector.getValue(0, k++) == 1 ? '#' : '.';
                builder.append(value);
            }
            string[i] = builder.toString();
        }
    }

    public void printPattern() {
        for (String line : string) {
            System.out.println(line);
        }
    }

    public String[] getString() {
        return string;
    }

    public Matrix getVector() {
        return vector;
    }

    public int getRows() {
        return rows;
    }

    public int getColumns() {
        return columns;
    }
}
