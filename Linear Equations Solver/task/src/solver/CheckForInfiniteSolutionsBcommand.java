package solver;

class CheckForInfiniteSolutionsBcommand implements BooleanCommand {
    private Matrix matrix;

    CheckForInfiniteSolutionsBcommand(Matrix matrix) {
        this.matrix = matrix;
    }

    @Override
    public boolean execute() {
        return matrix.checkForInfiniteSolutions();
    }
}
