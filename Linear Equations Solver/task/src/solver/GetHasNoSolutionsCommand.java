package solver;

class GetHasNoSolutionsCommand implements BooleanCommand {
    private Matrix matrix;

    GetHasNoSolutionsCommand(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public boolean execute() {
        return matrix.getHasNoSolutions();
    }
}