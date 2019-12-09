package solver;

public class Row {
    private double[] row;
    private int numOfVariables;

    Row(double... coefficients) {
        this.row = coefficients;
        this.numOfVariables = row.length - 1;
    }

    int size() {
        return row.length;
    }

    double[] getRow() {
        return this.row;
    }

    double getValueAtRowIndex(int index) {
        return this.row[index];
    }

    void setValueAtRowIndex(int index, double value) {
        this.row[index] = value;
    }

    Row multiply(double multiple) {
        double[] newRow = row.clone();
        for (int i = 0; i < newRow.length; i++) {
            newRow[i] *= multiple;
        }
        return new Row(newRow);
    }

    Row subtract(Row rowToSubtract) {
        double[] rowToSubtractArr = rowToSubtract.getRow();
        for (int i = 0; i < row.length; i++) {
            row[i] -= rowToSubtractArr[i];
        }
        return this;
    }

    boolean doesRowHaveAllZeroCoefficients() {
        for (int variableNum = 0; variableNum < numOfVariables; variableNum++) {
            if (row[variableNum] != 0) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

    }
}