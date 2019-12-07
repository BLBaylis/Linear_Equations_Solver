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

    public static void main(String[] args) {
        String inputPath = args[1];
        String outputPath = args[3];

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

        Controller controller = new Controller(new PrintMatrixCommand(matrix));
        controller.executeCommand();

        controller.setCommand(new ReduceToRREFCommand(matrix));
        controller.executeCommand();

        controller.setbCommand(new GetHasNoSolutionsCommand(matrix));
        boolean hasNoSolutions = controller.executeBooleanCommand();

        controller.setbCommand(new CheckForInfiniteSolutionsBcommand((matrix)));
        boolean hasInfiniteSolutions = controller.executeBooleanCommand();

        String solutionType = "Unique";
        if (hasNoSolutions) {
            solutionType = "No solutions";
        } else if (hasInfiniteSolutions) {
            solutionType = "Infinitely many solutions";
        }

        File outputFile = Paths.get(outputPath).toFile();
        controller.setdArrCommand(new GetSolutionsCommand(matrix));

        try (PrintWriter printWriter = new PrintWriter(outputFile)) {
            if (!"Unique".equals(solutionType)) {
                System.out.println(solutionType);
                printWriter.println(solutionType);
            } else {
                double[] solutions = controller.executeDoubleArrCommand();
                System.out.println("Solution: " + Arrays.toString(solutions));
                for (double variable : solutions) {
                    printWriter.println(variable);
                }
            }
        } catch (Exception e) {
            System.out.println("There was an error writing the results to file.");
        }
    }
}