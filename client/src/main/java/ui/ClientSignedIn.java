package ui;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.JoinRequest;
import ui.serverfacade.ServerFacade;

import java.util.HashMap;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class ClientSignedIn implements Client {
    private final ServerFacade server;
    private State state = State.LOGGEDIN;
    private final AuthData currentUserAuth;
    private final Scanner scanner = new Scanner(System.in);
    private final HashMap<Integer, Integer> gameMap = new HashMap<>();
    private int numGames = 0;

    public ClientSignedIn(int port, AuthData currUserAuth) {
        this.server = new ServerFacade(port);
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
        try {
            server.logout(currentUserAuth);
            state = State.LOGGEDOUT;
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
        System.out.print(SET_TEXT_COLOR_GREEN);
        return "User " + currentUserAuth.username() + " successfully logged out" + SET_TEXT_COLOR_WHITE;
    }

    private String createGame() {
        System.out.println("Okay! Please input a game name: ");
        System.out.print(">>> ");
        String input = scanner.nextLine();

        try {
            GameData game = new GameData(0, null, null, input, null);
            int gameID = server.createGame(game, currentUserAuth);
            numGames++;
            gameMap.put(numGames, gameID);
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Error: " + e.getMessage() + SET_TEXT_COLOR_WHITE;
        }
        return SET_TEXT_COLOR_GREEN + "Game \"" + input + "\" created." + SET_TEXT_COLOR_WHITE;
    }

    private String listGames() {
        GameData[] games = server.listGames(currentUserAuth);
        int gameNum = 1;
        StringBuilder result = new StringBuilder();
        for (GameData game : games) {
            String white = game.whiteUsername();
            String black = game.blackUsername();
            if (white == null) {
                white = "Available";
            }
            if (black == null) {
                black = "Available";
            }
            result.append(gameNum).append(": ").append(game.gameName()).append("\nWhite: ");
            result.append(white).append("\nBlack: ").append(black).append("\n\n");
            gameMap.put(gameNum, game.gameID());
            gameNum += 1;
        }
        return result.toString();
    }

    private String playGame() {
        this.listGames();
        int num = getGameNum();
        System.out.print("Desired player color: ");
        String color = scanner.nextLine().toUpperCase();

        int gameID;
        try {
            gameID = gameMap.get(num);
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Invalid game number. Please try again." + SET_TEXT_COLOR_WHITE;
        }
        var req = new JoinRequest(color, gameID);
        try {
            server.joinGame(req, currentUserAuth);
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Error: " + e.getMessage() + SET_TEXT_COLOR_WHITE;
        }
        System.out.println(SET_TEXT_COLOR_GREEN + "Successfully joined game " + num + " as " + color);
        System.out.print(SET_TEXT_COLOR_WHITE);
        return new BoardUI(new ChessGame().getBoard(), color).displayBoard();
    }

    private String observeGame() {
        int num = getGameNum();
        System.out.println(SET_TEXT_COLOR_GREEN + "Successfully joined game " + num + " as an observer.");
        System.out.print(SET_TEXT_COLOR_WHITE);
        return new BoardUI(new ChessGame().getBoard()).displayBoard();
    }

    private int getGameNum() {
        int num = 0;
        while (num == 0) {
            System.out.print("Join game number: ");
            String input = scanner.nextLine();
            try {
                num = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println(SET_TEXT_COLOR_RED + "Error: Must be a number. Please input a game number, not a game name.");
                System.out.print(SET_TEXT_COLOR_WHITE);
            }
        }
        return num;
    }

    @Override
    public String help() {
        return """
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
