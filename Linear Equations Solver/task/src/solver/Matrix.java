package solver;

public class Matrix {
    private Row[] matrix;
    private final int numOfVariables;
    private final int numOfEquations;
    private boolean hasNoSolutions = false;
    private boolean hasInfiniteSolutions = false;
    private String[] solution;

    Matrix(Row... equations) {
        this.matrix = equations;
        this.numOfEquations = equations.length;
        this.numOfVariables = equations[0].size() - 1;
        printMatrix();
    }

    boolean getHasInfiniteSolutions() {
        return hasInfiniteSolutions;
    }

    boolean getHasNoSolutions() {
        return hasNoSolutions;
    }

    String[] getSolution() {
        if (hasNoSolutions || hasInfiniteSolutions) {
            System.out.println("Warning! Returned unique solution when none available!");
        }
        return solution;
    }

    private Row getRowAtMatrixIndex(int index) {
        return this.matrix[index];
    }

    private void swapRows(int rowIndex1, int rowIndex2) {
        Row temp = matrix[rowIndex1];
        matrix[rowIndex1] = matrix[rowIndex2];
        matrix[rowIndex2] = temp;
        System.out.println("R" + rowIndex1 + " <====> R" + rowIndex2);
        printMatrix();
    }

    private void swapCols(int colIndex1, int colIndex2) {
        for (int i = 0; i < numOfEquations; i++) {
            Row row = matrix[i];
            Complex temp = row.getValueAtRowIndex(colIndex1);
            row.setValueAtRowIndex(colIndex1, row.getValueAtRowIndex(colIndex2));
            row.setValueAtRowIndex(colIndex2, temp);
            System.out.println("C" + colIndex1 + " <====> C" + colIndex2);
            printMatrix();
        }
    }

    private void printMatrix() {
        System.out.println();
        for (Row row : this.matrix) {
            System.out.println(row.toString());
        }
        System.out.println();
    }

    private void preventLeadingZero(int rowNo, int colNo) {
        if (!matrix[rowNo].getValueAtRowIndex(colNo).equals(Complex.ZERO)) {
            return;
        }
        //if non-zero in column below below swap rows
        for (int otherRowNo = rowNo + 1; otherRowNo < numOfEquations; otherRowNo++) {
            if (!matrix[otherRowNo].getValueAtRowIndex(colNo).equals(Complex.ZERO)) {
                swapRows(rowNo, otherRowNo);
                return;
            }
        }
        //if non-zero in same row to the right
        for (int otherColNo = colNo + 1; otherColNo < numOfVariables; otherColNo++) {
            if (!matrix[rowNo].getValueAtRowIndex(otherColNo).equals(Complex.ZERO)) {
                swapCols(colNo, otherColNo);
                return;
            }
        }
        //swap with non-zero below and right if possible
        for (int otherRowNo = rowNo + 1; otherRowNo < numOfEquations; otherRowNo++) {
            for (int otherColNo = colNo + 1; otherColNo < numOfVariables; otherColNo++) {
                if (!matrix[otherRowNo].getValueAtRowIndex(otherColNo).equals(Complex.ZERO)) {
                    swapRows(rowNo, otherRowNo);
                    swapCols(colNo, otherColNo);
                    return;
                }
            }
        }
    }

    private boolean isMatrixInconsistent() {
        for (int equationNo = numOfEquations - 1; equationNo >= 0; equationNo--) {
            Row equation = matrix[equationNo];
            if (equation.doesRowHaveAllZeroCoefficients() && !equation.getValueAtRowIndex(numOfVariables).equals(Complex.ZERO)) {
                return true;
            }
        }
        return false;
    }

    void checkForInfiniteSolutions() {
        int significantEquations = numOfEquations;
        for (int equationNo = numOfEquations - 1; equationNo >= 0; equationNo--) {
            Row equation = matrix[equationNo];
            if (equation.doesRowHaveAllZeroCoefficients() && equation.getValueAtRowIndex(numOfVariables).equals(Complex.ZERO)) {
                significantEquations--;
            }
            if (significantEquations < numOfVariables) {
                hasInfiniteSolutions = true;
                return;
            }
        }
    }

    private void reduceDiagonalElementToOne(int index) {
        Row oldRow = this.getRowAtMatrixIndex(index);
        /* if the diagonal element is 1 then no action is needed, if 0 it
        can't be changed from 0*/
        if (oldRow.getValueAtRowIndex(index).equals(Complex.ZERO) || oldRow.getValueAtRowIndex(index).equals(Complex.ONE)) {
            return;
        }
        Complex multiple = Complex.ONE.divide(oldRow.getValueAtRowIndex(index));
        matrix[index] = oldRow.multiply(multiple);
        System.out.println(multiple + " * R" + (index + 1) + " => R" + (index + 1));
        this.printMatrix();
    }

    private void reduceElementToZero(int rowNo, int pivotRowNo) {
        //colNo is unnecessary but it it makes the code easier to comprehend
        int colNo = pivotRowNo;
        Row rowToBeReduced = matrix[rowNo];
        Row pivotRow = matrix[pivotRowNo];
        /*if either of the targeted values are zero, the operation is redundant
        and will also create bugs */
        if (pivotRow.getValueAtRowIndex(colNo).equals(Complex.ZERO) || rowToBeReduced.getValueAtRowIndex(colNo).equals(Complex.ZERO)) {
            return;
        }
        /*multiply each row by the other row's value in that col.  This makes them
        equal, so when the pivotRow is subtracted from the row to be reduced, the
        value of the row to be reduced in that column becomes zero*/
        Complex pivotRowMultiple = pivotRow.getValueAtRowIndex(colNo);
        Complex rowToBeReducedMultiple = rowToBeReduced.getValueAtRowIndex(colNo);
        Row pivotRowMultiplied = pivotRow.multiply(rowToBeReducedMultiple);
        Row rowToBeReducedMultiplied = rowToBeReduced.multiply(pivotRowMultiple);
        matrix[rowNo] = rowToBeReducedMultiplied.subtract(pivotRowMultiplied);
        System.out.println(pivotRowMultiple + "R" + (rowNo + 1) + " - " + rowToBeReducedMultiple + "R" + (pivotRowNo + 1) + " => R" + (rowNo + 1));
        this.printMatrix();
    }

    void reduceToRREF() {
        for (int i = 0; i < numOfEquations; i++) {
            this.preventLeadingZero(i, i);
            if (this.isMatrixInconsistent()) {
                this.hasNoSolutions = true;
                return;
            }
            this.reduceDiagonalElementToOne(i);
            for (int j = i + 1; j < numOfEquations; j++) {
                this.reduceElementToZero(j, i);
            }
        }

        for (int i = numOfEquations - 1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                this.reduceElementToZero(j, i);
            }
        }
    }

    void findUniqueSolution() {
        solution = new String[numOfVariables];
        for (int i = 0; i < numOfVariables; i++) {
            solution[i] = this.matrix[i].getValueAtRowIndex(numOfVariables).toString();
        }
    }

    public static void main(String[] args) {
    }
}