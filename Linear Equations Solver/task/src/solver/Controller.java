package solver;

import solver.Command;

class Controller {
    private Command command;
    private BooleanCommand bCommand;
    private DoubleArrCommand dArrCommand;

    Controller(Command command) {
        this.command = command;
    }

    Controller(DoubleArrCommand dArrCommand) {
        this.dArrCommand = dArrCommand;
    }

    Controller(BooleanCommand bCommand) {
        this.bCommand = bCommand;
    }

    void setCommand(Command command) {
        this.command = command;
    }

    void setbCommand(BooleanCommand bCommand) {
        this.bCommand = bCommand;
    }

    void setdArrCommand(DoubleArrCommand dArrCommand) {
        this.dArrCommand = dArrCommand;
    }

    void executeCommand() {
        command.execute();
    }

    boolean executeBooleanCommand() {
        return bCommand.execute();
    }

    double[] executeDoubleArrCommand() {
        return dArrCommand.execute();
    }


}