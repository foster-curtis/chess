package ui;

import ui.serverfacade.ServerFacade;

public class ClientMain implements Client {
    private final ServerFacade server;

    public ClientMain(int port) {
        this.server = new ServerFacade(port);
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public String help() {
        return """
                Enter a number and press enter to execute a command!
                
                1. Help -> View list of available commands
                2. Quit -> Quit the program
                3. Login -> Login as returning user
                4. Register -> Create a new user
                """;
    }
}
