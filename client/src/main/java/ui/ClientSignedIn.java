package ui;

import model.AuthData;
import model.GameData;
import ui.serverfacade.ServerFacade;

import java.util.Scanner;

public class ClientSignedIn implements Client {
    private final ServerFacade server;
    private State state;
    private final AuthData currentUserAuth;
    private final Scanner scanner;

    public ClientSignedIn(int port, AuthData currUserAuth) {
        this.server = new ServerFacade(port);
        this.state = State.LOGGEDIN;
        currentUserAuth = currUserAuth;
        scanner = new Scanner(System.in);
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
        try {
            server.logout(currentUserAuth);
            state = State.LOGGEDOUT;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return "User " + currentUserAuth.username() + " successfully logged out";
    }

    private String createGame() {
        System.out.println("Okay! What should the game name be?");
        System.out.print(">>> ");
        String input = scanner.nextLine();

        try {
            GameData game = new GameData(0, null, null, input, null);
            server.createGame(game, currentUserAuth);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        return "Game \"" + input + "\" created.";
    }

    private String listGames() {
        GameData[] games = server.listGames(currentUserAuth);
        int gameNum = 1;
        for (GameData game : games) {
            String white = game.whiteUsername();
            String black = game.blackUsername();
            if (white == null) {
                white = "Available";
            }
            if (black == null) {
                black = "Available";
            }
            System.out.println(gameNum + ": " + game.gameName() + "\nWhite: " + white + "\nBlack: " + black);
            System.out.println();
            gameNum += 1;
        }
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
                Enter a number and press enter to execute a command:
                
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
