package solver;

class CheckForInfiniteSolutionsCommand implements Command {
    private Matrix matrix;

    CheckForInfiniteSolutionsCommand(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public void execute() {
        matrix.checkForInfiniteSolutions();
    }
}
