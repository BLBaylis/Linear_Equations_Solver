package solver;

class Controller {
    private Command command;

    Controller(Command command) {
        this.command = command;
    }

    void setCommand(Command command) {
        this.command = command;
    }

    void executeCommand() {
        command.execute();
    }

}