package ui;

import chess.ChessGame;
import model.*;
import ui.serverfacade.ServerFacade;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;


public class ClientMain implements Client {
    private final ServerFacade server;
    private final Scanner scanner;
    private State state;
    private AuthData currentUserAuth;

    public ClientMain(int port) {
        this.server = new ServerFacade(port);
        scanner = new Scanner(System.in);
        currentUserAuth = null;
        state = State.LOGGEDOUT;
    }

    public AuthData getCurrentUserAuth() {
        return currentUserAuth;
    }

    @Override
    public Integer getGameID() {
        return 0;
    }

    @Override
    public ChessGame.TeamColor getPlayerColor() {
        return null;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String eval(String input) {
        return switch (input) {
            case "2" -> quit();
            case "3" -> login();
            case "4" -> register();
            default -> help();
        };
    }

    private String quit() {
        return "quit";
    }

    private String login() {
        System.out.println("""
                Please enter your username and password separated by spaces as shown:
                    -> username Pa$$word
                """);
        System.out.print(">>> ");
        String[] fields;
        while (true) {
            String input = scanner.nextLine();
            fields = input.split(" ");
            if (fields.length != 2) {
                System.out.println(SET_TEXT_COLOR_RED + "Invalid input. Please fill all required fields (2), separated by spaces.");
                System.out.print(SET_TEXT_COLOR_WHITE);
                System.out.print(">>> ");
            } else {
                break;
            }
        }
        UserData user = new UserData(fields[0], fields[1], null);
        try {
            currentUserAuth = server.login(user);
            state = State.LOGGEDIN;
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Error: " + e.getMessage() + SET_TEXT_COLOR_WHITE;
        }
        return "Welcome back, " + user.username() + "!";
    }

    private String register() {
        System.out.print("""
                Great, lets get you registered so you can play some chess!
                Please enter your username, password, and email separated by spaces as shown:
                    -> username Pa$$word email@email.com
                """);
        System.out.print(">>> ");
        String[] fields;
        while (true) {
            String input = scanner.nextLine();
            fields = input.split(" ");
            if (fields.length != 3) {
                System.out.println(SET_TEXT_COLOR_RED + "Invalid input. Please fill all required fields (3), separated by spaces.");
                System.out.print(SET_TEXT_COLOR_WHITE);
            } else {
                break;
            }
        }
        UserData user = new UserData(fields[0], fields[1], fields[2]);
        try {
            currentUserAuth = server.register(user);
            state = State.LOGGEDIN;
        } catch (Exception e) {
            return SET_TEXT_COLOR_RED + "Error: " + e.getMessage() + SET_TEXT_COLOR_WHITE;
        }
        return "Welcome to the Chess Server, " + user.username() + "!";
    }

    @Override
    public String help() {
        return """
                1. Help -> View list of available commands
                2. Quit -> Quit the program
                3. Login -> Login as returning user
                4. Register -> Create a new user
                """;
    }
}
