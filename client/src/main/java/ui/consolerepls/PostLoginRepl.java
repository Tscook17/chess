package ui.consolerepls;

import chess.ChessGame;
import model.GameData;
import servicepackets.result.*;
import ui.ServerFacade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class PostLoginRepl implements Runnable {
    private final ServerFacade facade;
    private ArrayList<Integer> gameList = new ArrayList<>();

    public PostLoginRepl(ServerFacade facade) {
        this.facade = facade;
    }

    public void run() {
        // print available commands
        printHelp(false);
        // main post login loop
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equalsIgnoreCase("logout")) {
            // print cursor
            System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR + "[LOGGED_IN] >>> " + SET_TEXT_COLOR_GREEN);
            // check input
            input = scanner.nextLine();
            var tokens = input.toLowerCase().split(" ");
            var cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "help" -> printHelp(false);
                case "logout" -> {
                    if (!logout()) {
                        input = "";
                    }
                }
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                default -> printHelp(true);
            }
        }
    }

    private void printHelp(boolean isBadCommand) {
        if (isBadCommand) {
            System.out.println(SET_TEXT_COLOR_RED + "\nError: Please enter a valid command");
        }
        System.out.println(SET_TEXT_COLOR_BLUE + """
                \ncreate <GameName> - creates a new game with given name 
                list - list games
                join <game#> [WHITE or BLACK] - join a game with given # as given color
                observe <game#> - observe a game with given #
                logout - logout of application
                help - lists possible commands
                """);
    }

    private boolean logout() {
        LogoutResult result = facade.logout(facade.getUserAuthToken());
        if (result.getErrorCode() != 200) {
            System.out.println(SET_TEXT_COLOR_RED + "\n" + result.getMessage());
            return false;
        } else {
            return true;
        }
    }

    private void create(String[] params) {
        CreateGameResult result;
        String gameName;
        if (params.length != 1) {
            Scanner scanner = new Scanner(System.in);
            // ask for game name
            System.out.print(RESET_TEXT_COLOR + "\nGameName: ");
            gameName = scanner.nextLine();

        } else {
            gameName = params[0];
        }
        result = facade.createGame(facade.getUserAuthToken(), gameName);
        if (result.getErrorCode() == 200) {
            System.out.println(RESET_TEXT_COLOR + "\nCreated " + gameName);
        } else {
            System.out.println(SET_TEXT_COLOR_RED + "\n" + result.getMessage());
        }
    }

    private void list() {
        ListGamesResult result = facade.listGames(facade.getUserAuthToken());
        if (result.getErrorCode() != 200) {
            System.out.println(SET_TEXT_COLOR_RED + "\n" + result.getMessage());
        } else {
            GameData[] games = result.getGames();
            gameList.clear();
            System.out.print(SET_TEXT_COLOR_BLUE);
            for (int i = 0; i < games.length; i++) {
                System.out.println((i+1) + ". GameName: " + games[i].gameName() +
                        ", White: " + games[i].whiteUsername() +
                        ", Black: " + games[i].blackUsername());
                gameList.add(games[i].gameID());
            }
        }
    }

    private void join(String[] params) {
        JoinGameResult result;
        String gameNum;
        String color;
        if (params.length != 2) {
            Scanner scanner = new Scanner(System.in);
            // ask for game number
            System.out.print(RESET_TEXT_COLOR + "\nGameNumber: ");
            gameNum = scanner.nextLine();
            // ask for color
            System.out.print("\nColor to join (WHITE/BLACK): ");
            color = scanner.nextLine().toUpperCase();
        } else {
            gameNum = params[0];
            color = params[1].toUpperCase();
        }
        int gameNumInt = Integer.parseInt(gameNum);
        if (gameNumInt <= gameList.size() && gameNumInt > 0) {
            result = facade.joinGame(facade.getUserAuthToken(), color, gameList.get(gameNumInt-1));
        } else {
            result = new JoinGameResult("Error: bad request", 400);
        }
        // more functionality added in phase 6
        if (result.getErrorCode() == 200) {
            System.out.println("\nJoined game number " + gameNum);
            GameplayRepl repl =
                    new GameplayRepl(facade.getURL(), facade.getUserAuthToken(), gameNumInt, color.equals("WHITE"));
            repl.run();
        } else {
            System.out.println(SET_TEXT_COLOR_RED + "\n" + result.getMessage());
        }
    }

    private void observe(String[] params) {
        String gameNum;
        if (params.length != 1) {
            Scanner scanner = new Scanner(System.in);
            // ask for game number
            System.out.print(RESET_TEXT_COLOR + "\nGameNumber: ");
            gameNum = scanner.nextLine();
        } else {
            gameNum = params[0];
        }
        int gameNumInt = Integer.parseInt(gameNum);
        // observe functionality added in phase 6
        if (gameNumInt <= gameList.size() && gameNumInt > 0) {
            System.out.println(RESET_TEXT_COLOR + "\nObserving game number " + gameNumInt);
            GameplayRepl repl =
                    new GameplayRepl(facade.getURL(), facade.getUserAuthToken(), gameNumInt, true);
            repl.run();
        } else {
            System.out.println(SET_TEXT_COLOR_RED + "\nError: bad request");
        }
    }
}
