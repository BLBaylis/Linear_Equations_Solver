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
            Complex[] equation = new Complex[numOfVariables + 1];
            for (int i = 0; i < numOfVariables + 1; i++) {
                equation[i] = new Complex(scanner.next());
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
        Controller controller = new Controller(new ReduceToRREFCommand(matrix));
        controller.executeCommand();

        boolean hasNoSolutions = matrix.getHasNoSolutions();

        controller.setCommand(new CheckForInfiniteSolutionsCommand(matrix));
        controller.executeCommand();
        boolean hasInfiniteSolutions = matrix.getHasInfiniteSolutions();

        String solutionType = "Unique";
        if (hasNoSolutions) {
            solutionType = "No solutions";
        } else if (hasInfiniteSolutions) {
            solutionType = "Infinitely many solutions";
        }

        controller.setCommand(new FindUniqueSolutionCommand(matrix));

        File outputFile = Paths.get(outputPath).toFile();

        try (PrintWriter printWriter = new PrintWriter(outputFile)) {
            if (!"Unique".equals(solutionType)) {
                System.out.println(solutionType);
                printWriter.println(solutionType);
            } else {
                controller.executeCommand();
                String[] solution = matrix.getSolution();
                System.out.println("Solution: " + Arrays.toString(solution));
                for (String variable : solution) {
                    printWriter.println(variable);
                }
            }
        } catch (Exception e) {
            System.out.println("There was an error writing the results to file.");
        }
    }
}