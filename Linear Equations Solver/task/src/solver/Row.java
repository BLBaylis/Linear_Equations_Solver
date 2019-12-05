package solver;

public class Row {
    private double[] row;

    Row(double... coefficients) {
        this.row = coefficients;
    }

    int size() {
        return row.length;
    }

    double getValueAtRowIndex(int index) {
        return this.row[index];
    }

    /*there is repeated code between the two methods below but refactoring into
    a single function feels like it will add unnecessary complexity*/
    Row multiply(double multiple) {
        double[] newRow = this.row.clone();
        for (int i = 0; i < newRow.length; i++) {
            newRow[i] *= multiple;
        }
        return new Row(newRow);
    }

    Row subtract(Row rowToSubtract) {
        double[] newRow = this.row.clone();
        for (int i = 0; i < newRow.length; i++) {
            newRow[i] -= rowToSubtract.getValueAtRowIndex(i);
        }
        return new Row(newRow);
    }

     public static void main(String[] args) {

    }
}
