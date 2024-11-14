package ui;

import java.util.Scanner;

import static ui.EscapeSequences.*;

public class Repl {
    public final Client client;

    public Repl(int port) {
        this.client = new ClientMain(port);
    }

    public void run() {
        System.out.println("♕ Welcome to Chess! ♕");
        System.out.println("Press enter at any time to view the help menu.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        String result = "";
        while (!result.equals("quit")) {
            System.out.print("Type a command number: ");
            String input = scanner.nextLine();

            try {
                result = client.eval(input);
                System.out.print(SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(SET_TEXT_COLOR_RED + msg);
            }
        }

        scanner.close();
    }
}
