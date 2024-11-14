package ui;

import model.AuthData;
import ui.serverfacade.ServerFacade;

import java.util.HashMap;

public class ClientSignedIn implements Client {
    private ServerFacade server;
    private State state;
    private final AuthData currentUserAuth;

    public ClientSignedIn(int port, AuthData currUserAuth) {
        this.server = new ServerFacade(port);
        this.state = State.LOGGEDIN;
        currentUserAuth = currUserAuth;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String eval(String input) {
        return switch (input) {
            case "2" -> logout();
            case "3" -> createGame();
            case "4" -> listGames();
            case "5" -> playGame();
            case "6" -> observeGame();
            default -> help();
        };
    }

    private String logout() {

        return "";
    }

    private String createGame() {
        return "";
    }

    private String listGames() {
        return "";
    }

    private String playGame() {
        return "";
    }

    private String observeGame() {
        return "";
    }

    @Override
    public String help() {
        return """
                Enter a number and press enter to execute a command!
                
                 1. Help -> View list of available commands
                 2. Logout -> End this session
                 3. Create Game -> Create a new game
                 4. List Games -> List all available games
                 5. Play Game -> Join an existing game as a player
                 6. Observe Game -> Join an existing game as an observer
                """;
    }

    @Override
    public AuthData getCurrentUserAuth() {
        return currentUserAuth;
    }
}
