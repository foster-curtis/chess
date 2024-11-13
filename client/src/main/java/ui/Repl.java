package ui;

import java.util.Scanner;

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

        System.out.print("Enter your name: ");
        String name = scanner.nextLine(); // Reading a line of text

        System.out.print("Enter your age: ");
        int age = scanner.nextInt(); // Reading an integer

        System.out.println("Hello, " + name + "! You are " + age + " years old.");

        scanner.close();
    }
}
