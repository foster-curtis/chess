package ui;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.SET_BG_COLOR_BLACK;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class Repl implements ServerMessageObserver {
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
        System.out.print(">>> ");

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
            newSignedInClient(false);

            while (client.getState() == State.LOGGEDIN) {
                loop(result, scanner);
                if (client.getState() == State.INGAME) {
                    this.client = new ClientInGame(port, client.getCurrentUserAuth(), client.getGameID(), client.getPlayerColor(), this);
                    System.out.println("Here are your new commands while in the game!\n" +
                            "Press enter at any time to view them again.");
                    System.out.println(client.help());
                    System.out.print(">>> ");

                    while (client.getState() == State.INGAME) {
                        loop(result, scanner);
                    }

                    newSignedInClient(true);
                }
            }
        }

        scanner.close();
    }

    private String loop(String result, Scanner scanner) {
        String input = scanner.nextLine();

        try {
            result = client.eval(input);
            System.out.println(result);
        } catch (Throwable e) {
            var msg = e.toString();
            System.out.println(msg);
        }
        System.out.print(">>> ");
        return result;
    }

    private void newSignedInClient(Boolean fromGame) {
        this.client = new ClientSignedIn(port, client.getCurrentUserAuth());
        if (!fromGame) {
            System.out.println("Now that you're logged in, you have some new commands available!");
        }
        System.out.println(!fromGame ? client.help() : "\n" + client.help());
        System.out.print(">>> ");
    }


    @Override
    public void notify(String notification) {
        System.out.println(notification);
        System.out.print(">>> ");
    }
}
