package solver;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Scanner;

public class Main {

    private static Row[] createEquations(Scanner scanner) {
        int numOfVariables = scanner.nextInt();
        int numOfEquations = scanner.nextInt();
        Row[] equations = new Row[numOfEquations];
        int equationNo = 0;
        while (scanner.hasNext()) {
            double[] equation = new double[numOfVariables + 1];
            for (int i = 0; i < numOfVariables + 1; i++) {
                equation[i] = scanner.nextDouble();
            }
            equations[equationNo] = new Row(equation);
            equationNo++;
        }
        return equations;
    }

    private static String checkSolutionType(Matrix matrix) {
        int numOfVariables = matrix.getNumOfVariables();
        int numOfEquations = matrix.getNumOfEquations();
        int significantEqs = numOfEquations;
        for (int equationNo = 0; equationNo < numOfEquations; equationNo++) {
            Row equation = matrix.getRowAtMatrixIndex(equationNo);
            boolean allZeroRow = true;
            //if a row contains a non-zero values, move to next row
            for (int variableNum = 0; variableNum < numOfVariables; variableNum++) {
                if (equation.getValueAtRowIndex(variableNum) != 0) {
                    allZeroRow = false;
                    break;
                }
            }
            if (!allZeroRow) {
                continue;
            }
            /*if a row is all zeros but constant is non-zero, it is inconsistent as
            you can't have 0x + 0y = 10*/
            if (equation.getValueAtRowIndex(numOfVariables) != 0) {
                return "No Solutions";
            } else {
                //this section pertains to an all zero row including the constant
                //as a result it isn't a significant eq and can be decremented
                significantEqs--;

            }
        }
        if (significantEqs < numOfVariables) {
            /*if there are less significant equations then there are variables, you
             can't find unique values for each*/
            return "Infinitely many solutions";
        }
        return "Unique";
    }

    public static void main(String[] args) {
        String inputPath = "";
        String outputPath = "";
        for (int i = 1; i < 4; i += 2) {
            if (i == 1) {
                inputPath = args[i];
            } else {
                outputPath = args[i];
            }
        }

        //create matrix from inputs
        File inputFile = Paths.get(inputPath).toFile();
        Row[] equations;
        try (Scanner scanner = new Scanner(inputFile)) {
            equations = Main.createEquations(scanner);
        } catch (Exception e) {
            System.out.println("No file found.");
            return;
        }
        Matrix matrix = new Matrix(equations);
        System.out.println("Initial matrix");
        matrix.printMatrix();

        matrix.formatMatrix();
        matrix.reduceToRREF();

        //generate solution
        String solutionType = Main.checkSolutionType(matrix);
        double[] solutions = new double[0];
        if ("Unique".equals(solutionType)) {
            solutions = matrix.getSolution();
        }

        //print to specified file
        File outputFile = Paths.get(outputPath).toFile();
        try (PrintWriter printWriter = new PrintWriter(outputFile)) {
            if (!"Unique".equals(solutionType)) {
                System.out.println(solutionType);
                printWriter.println(solutionType);
            } else {
                System.out.println("Solution: " + Arrays.toString(solutions));
                for (double variable : solutions) {
                    printWriter.println(variable);
                }
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("There was an error writing the results to file.");
        }
    }
}
