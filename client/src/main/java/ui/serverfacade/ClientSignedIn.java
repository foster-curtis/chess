package ui.serverfacade;

import ui.Client;

public class ClientSignedIn implements Client {
    private ServerFacade server;

    public ClientSignedIn(int port) {
        this.server = new ServerFacade(port);
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public String help() {
        return "";
    }
}
