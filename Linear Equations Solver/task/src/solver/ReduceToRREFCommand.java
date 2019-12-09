package solver;

class ReduceToRREFCommand implements Command {
    private Matrix matrix;

    ReduceToRREFCommand(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public void execute() {
        matrix.reduceToRREF();
    }
}