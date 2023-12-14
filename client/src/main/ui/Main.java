package ui;

import java.util.Scanner;

import static util.EscapeSequences.RESET_TEXT_COLOR;

public class Main {
    public static void main(String[] args) {
        try {
            ChessClient client = new ChessClient();
            System.out.println("Welcome to Ethan's chess game client! Type Help to see commands");
            Scanner scanner = new Scanner(System.in);

            var result = "";
            while (!result.equals("quit")) {
                client.printPrompt();
                String input = scanner.nextLine();

                try {
                    result = client.execute(input);
                    System.out.print(RESET_TEXT_COLOR + result);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to connect to the server");
        }
    }
}