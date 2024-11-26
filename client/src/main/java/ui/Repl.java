package ui;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.SET_BG_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class Repl {
    public Client client;
    private final int port;

    public Repl(int port) {
        this.client = new ClientMain(port);
        this.port = port;
    }

    public void run() {
        System.out.print(SET_BG_COLOR_BLACK);
        System.out.print(SET_TEXT_COLOR_WHITE);
        System.out.println("♕ Welcome to Chess! ♕");
        System.out.println("Press enter at any time to view the help menu.");
        System.out.println("\nType in a number and press enter to execute a command:");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";

        while (true) {
            this.client = new ClientMain(port);
            while (client.getState() == State.LOGGEDOUT) {
                result = loop(result, scanner);
                if (Objects.equals(result, "quit")) {
                    break;
                }
            }
            if (Objects.equals(result, "quit")) {
                break;
            }
            this.client = new ClientSignedIn(port, client.getCurrentUserAuth());
            System.out.println("Now that you're logged in, you have some new commands available!");
            System.out.println(client.help());

            while (client.getState() == State.LOGGEDIN) {
                result = loop(result, scanner);
                if (client.getState() == State.INGAME) {
                    this.client = new ClientInGame(port, client.getCurrentUserAuth());
                    System.out.println("Here are your new commands while you play the game!\n" +
                            "Press enter at any time to view them again.");
                    System.out.println(client.help());
                }
                while (client.getState() == State.INGAME) {
                    result = loop(result, scanner);
                }
            }
        }

        scanner.close();
    }

    private String loop(String result, Scanner scanner) {
        System.out.print(">>> ");
        String input = scanner.nextLine();
        System.out.flush();

        try {
            result = client.eval(input);
            System.out.println(result);
        } catch (Throwable e) {
            var msg = e.toString();
            System.out.println(msg);
        }
        return result;
    }
}
