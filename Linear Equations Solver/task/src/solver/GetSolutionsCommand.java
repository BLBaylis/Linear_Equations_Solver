package solver;

public class GetSolutionsCommand implements DoubleArrCommand {
    private Matrix matrix;

    GetSolutionsCommand(Matrix matrix){
        this.matrix = matrix;
    }

    @Override
    public double[] execute() {
        return matrix.getSolution();
    }
}
