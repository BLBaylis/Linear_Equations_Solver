package solver;

public class Row {
    private double[] row;

    Row(double... coefficients) {
        this.row = coefficients;
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
        double[] newRow = this.row.clone();
        for (int i = 0; i < newRow.length; i++) {
            newRow[i] *= multiple;
        }
        return new Row(newRow);
    }

    Row subtract(Row rowToSubtract) {
        double[] newRow = this.row.clone();
        double[] rowToSubtractArr = rowToSubtract.getRow();
        for (int i = 0; i < newRow.length; i++) {
            newRow[i] -= rowToSubtractArr[i];
        }
        return new Row(newRow);
    }

    public static void main(String[] args) {

    }
}