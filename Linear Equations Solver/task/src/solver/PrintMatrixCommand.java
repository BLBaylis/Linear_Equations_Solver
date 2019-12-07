package solver;

class PrintMatrixCommand implements Command{
    private Matrix matrix;

    PrintMatrixCommand(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public void execute() {
        matrix.printMatrix();
    }
}