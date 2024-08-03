package ui.consolerepls;

import servicepackets.result.LoginResult;
import servicepackets.result.RegisterResult;
import ui.ServerFacade;
import static ui.EscapeSequences.*;

import java.util.Arrays;
import java.util.Scanner;

public class PreLoginRepl implements Runnable{
    private final ServerFacade facade;

    public PreLoginRepl(String serverURL) {
        facade = new ServerFacade(serverURL);
    }

    public void run() {
        // welcome message
        System.out.println(RESET_TEXT_COLOR + WHITE_KING +
                "Welcome to my cs240 chess server. Type 'Help' to get started." + BLACK_KING);

        // main pre login loop
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equalsIgnoreCase("quit")) {
            // print cursor
            System.out.print(RESET_TEXT_COLOR + "[LOGGED_OUT] >>> " + SET_TEXT_COLOR_GREEN);
            // check input
            input = scanner.nextLine();
            var tokens = input.toLowerCase().split(" ");
            var cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "help" -> printHelp(false);
                case "quit" -> System.out.println(RESET_TEXT_COLOR + "Thanks for playing!");
                case "login" -> login(params);
                case "register" -> register(params);
                default -> printHelp(true);
            }
        }
    }

    private void printHelp(boolean isBadCommand) {
        if (isBadCommand) {
            System.out.println(SET_TEXT_COLOR_RED + "\nError: Please enter a valid command");
        }
        System.out.println(SET_TEXT_COLOR_BLUE + """
                \nregister <USERNAME> <PASSWORD> <EMAIL> - to create a new account
                login <USERNAME> <PASSWORD> - to login
                quit - to quit the application
                help - lists possible commands
                """);
    }

    private void login(String[] params) {
        LoginResult result;
        if (params.length != 2) {
            Scanner scanner = new Scanner(System.in);
            // ask for username
            System.out.print(RESET_TEXT_COLOR + "\nUsername: ");
            String username = scanner.nextLine();
            // ask for password
            System.out.print("\nPassword: ");
            String password = scanner.nextLine();

            result = facade.login(username, password);
        } else {
            result = facade.login(params[0], params[1]);
        }
        if (result.getErrorCode() == 200) {
            System.out.println("\nLogged in as " + result.getUsername());
            PostLoginRepl repl = new PostLoginRepl(facade);
            repl.run();
        } else {
            System.out.println(SET_TEXT_COLOR_RED + "\n" + result.getMessage());
        }
    }

    private void register(String[] params) {
        RegisterResult result;
        if (params.length != 3) {
            Scanner scanner = new Scanner(System.in);
            // ask for username
            System.out.print(RESET_TEXT_COLOR + "\nUsername: ");
            String username = scanner.nextLine();
            // ask for password
            System.out.print("\nPassword: ");
            String password = scanner.nextLine();
            // ask for email
            System.out.print("\nEmail: ");
            String email = scanner.nextLine();

            result = facade.register(username, password, email);
        } else {
            result = facade.register(params[0], params[1], params[2]);
        }
        if (result.getErrorCode() == 200) {
            System.out.println("\nRegistered in as " + result.getUsername());
            System.out.println("\nLogged in as " + result.getUsername());
            PostLoginRepl repl = new PostLoginRepl(facade);
            repl.run();
        } else {
            System.out.println(SET_TEXT_COLOR_RED + "\n" + result.getMessage());
        }
    }
}
