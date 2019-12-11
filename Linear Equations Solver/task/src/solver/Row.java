package solver;

import java.util.Arrays;

public class Row {
    private Complex[] row;
    private int numOfVariables;

    Row(Complex... coefficients) {
        this.row = coefficients;
        this.numOfVariables = row.length - 1;
    }

    int size() {
        return row.length;
    }

    @Override
    public String toString() {
        return Arrays.toString(row);
    }

    Complex getValueAtRowIndex(int index) {
        return this.row[index];
    }

    void setValueAtRowIndex(int index, Complex value) {
        this.row[index] = value;
    }

    Row multiply(Complex multiple) {
        Complex[] newRow = row.clone();
        for (int i = 0; i < newRow.length; i++) {
            newRow[i] = newRow[i].multiply(multiple);
        }
        return new Row(newRow);
    }

    Row subtract(Row rowToSubtract) {
        for (int i = 0; i < row.length; i++) {
            row[i] = row[i].subtract(rowToSubtract.getValueAtRowIndex(i));
        }
        return this;
    }

    boolean doesRowHaveAllZeroCoefficients() {
        for (int variableNum = 0; variableNum < numOfVariables; variableNum++) {
            if (!row[variableNum].equals(Complex.ZERO)) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

    }
}