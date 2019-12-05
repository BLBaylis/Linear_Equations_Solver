package solver;

import java.text.DecimalFormat;

public class Matrix {
    private Row[] matrix;
    private int numOfVariables;
    private int numOfEquations;

    Matrix(Row... equations) {
        this.matrix = equations;
        this.numOfEquations = equations.length;
        this.numOfVariables = equations[0].size() - 1;
    }

    int getNumOfVariables() {
        return numOfVariables;
    }

    int getNumOfEquations() {
        return numOfEquations;
    }

    Row getRowAtMatrixIndex(int index) {
        return this.matrix[index];
    }

    void formatMatrix() {
        for (int equationNo = 0, variableNum = 0; equationNo < numOfEquations; equationNo++, variableNum++) {
            Row equation = matrix[equationNo];
            //check for equation for zero leading number
            if (equation.getValueAtRowIndex(variableNum) != 0) {
                continue;
            }
            //if a non-zero value can be found in a row below swap them
            for (int row = equationNo + 1; row < numOfEquations; row++) {
                Row currRow = matrix[row];
                if (currRow.getValueAtRowIndex(variableNum) != 0) {
                    swapRows(equationNo, row);
                    break;
                }
            }
        }
    }

    private void swapRows(int rowIndex1, int rowIndex2) {
        Row temp = matrix[rowIndex1];
        matrix[rowIndex1] = matrix[rowIndex2];
        matrix[rowIndex2] = temp;
    }

    void reduceDiagonalElementToOne(int index) {
        Row oldRow = this.getRowAtMatrixIndex(index);
        /* if the diagonal element is 1 then no action is needed, if 0 it
        can't be changed from 0*/
        if (oldRow.getValueAtRowIndex(index) == 0.0 || oldRow.getValueAtRowIndex(index) == 1.0) {
            return;
        }
        double multiple = 1/oldRow.getValueAtRowIndex(index);
        Row newRow = oldRow.multiply(multiple);
        matrix[index] = newRow;
        System.out.println(multiple + " * R" + (index + 1) + " => R" + (index + 1));
        this.printMatrix();
    }

    void reduceElementToZero(int rowNo, int pivotRowNo) {
        //colNo is unnecessary but it it makes the code easier to comprehend
        int colNo = pivotRowNo;
        Row rowToBeReduced = matrix[rowNo];
        Row pivotRow = matrix[pivotRowNo];
        /*if either of the targeted values are zero, the operation is redundant
        and will also create bugs */
        if (pivotRow.getValueAtRowIndex(colNo) == 0.0 || rowToBeReduced.getValueAtRowIndex(colNo) == 0.0) {
            return;
        }
        /*multiply each row by the other row's value in that col.  This makes them
        equal, so when the pivotRow is subtracted from the row to be reduced, the
        value of the row to be reduced in that column becomes zero*/
        double pivotRowMultiple = pivotRow.getValueAtRowIndex(colNo);
        double rowToBeReducedMultiple = rowToBeReduced.getValueAtRowIndex(colNo);
        Row pivotRowMultiplied = pivotRow.multiply(rowToBeReducedMultiple);
        Row rowToBeReducedMultiplied = rowToBeReduced.multiply(pivotRowMultiple);
        matrix[rowNo] = rowToBeReducedMultiplied.subtract(pivotRowMultiplied);
        this.printMatrix();
    }

    void reduceToRREF() {
        /*for every row reduces element at (rowNo, rowNo) to 1, then reduces
         every other value BELOW it in its column to zero by subtracting multiples
         of the pivot row.*/
        for (int pivotRowNo = 0; pivotRowNo < numOfEquations; pivotRowNo++) {
            this.reduceDiagonalElementToOne(pivotRowNo);
            for (int otherRowNo = pivotRowNo + 1; otherRowNo < numOfEquations; otherRowNo++) {
                this.reduceElementToZero(otherRowNo, pivotRowNo);
            }
        }

        /*Starting from the bottom right variable value reduces every other value
        ABOVE it in its column to zero in the same way as on the way down*/
        for (int pivotRow = numOfEquations - 1; pivotRow >= 0; pivotRow--) {
            for (int otherRowNo = pivotRow - 1; otherRowNo >= 0; otherRowNo--) {
                this.reduceElementToZero(otherRowNo, pivotRow);
            }
        }
    }

    double[] getSolution() {
        double[] solution = new double[numOfVariables];
        for (int i = 0; i < numOfVariables; i++) {
            solution[i] = this.matrix[i].getValueAtRowIndex(numOfVariables);
        }
        return solution;
    }

    void printMatrix() {
        System.out.println();
        DecimalFormat df = new DecimalFormat("0.#####");
        for (Row row : this.matrix) {
            for (int j = 0; j < row.size(); j++) {
                double currEle = row.getValueAtRowIndex(j);
                String eleToPrint;
                if (currEle >= 0.0) {
                    eleToPrint = "  " + df.format(currEle) + "  ";
                } else {
                    eleToPrint = " " + df.format(currEle) + "  ";
                }
                if (j == 0) {
                    System.out.print("|" + eleToPrint + "|");
                    continue;
                }
                if (j == row.size() - 1) {
                    System.out.println(eleToPrint + "|");
                    continue;
                }
                System.out.print(eleToPrint + "|");
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
    }
}
