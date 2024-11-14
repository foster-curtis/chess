package ui;

import java.util.Objects;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    public Client client;
    private final int port;

    public Repl(int port) {
        this.client = new ClientMain(port);
        this.port = port;
    }

    public void run() {
        System.out.println("♕ Welcome to Chess! ♕");
        System.out.println("Press enter at any time to view the help menu.");
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
            System.out.println("Now that you're logged in, you have some new commands available to you!");
            System.out.println(client.help());

            while (client.getState() == State.LOGGEDIN) {
                result = loop(result, scanner);
            }
        }

        scanner.close();
    }

    private String loop(String result, Scanner scanner) {
        System.out.print(">>> ");
        String input = scanner.nextLine();

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
