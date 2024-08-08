package ui.consolerepls;

import chess.*;
import com.google.gson.Gson;
import ui.WebSocketFacade;
import websocket.messages.LoadGameMessage;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

import static ui.EscapeSequences.*;

public class GameplayRepl implements Runnable, GameHandler {
    private final Map<ChessPiece.PieceType, String> pieceMap = Map.of(
            ChessPiece.PieceType.KING, "K",
            ChessPiece.PieceType.QUEEN, "Q",
            ChessPiece.PieceType.KNIGHT, "N",
            ChessPiece.PieceType.BISHOP, "B",
            ChessPiece.PieceType.ROOK, "R",
            ChessPiece.PieceType.PAWN, "P"
    );

    private final Map<Character, Integer> coordinateMap = Map.of(
            'a', 1,
            'b', 2,
            'c', 3,
            'd', 4,
            'e', 5,
            'f', 6,
            'g', 7,
            'h', 8
    );

    private WebSocketFacade wsFacade;
    private String authToken;
    private Integer gameID;
    private ChessGame localGame;
    private boolean isObserver;

    public GameplayRepl(String url, String authToken, int gameID, boolean isObserver) {
        this.authToken = authToken;
        this.gameID = gameID;
        this.isObserver = isObserver;
        try {
            wsFacade = new WebSocketFacade(url, this);
            wsFacade.connect(authToken, gameID);
        } catch(Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + "\nError: unable to connect");
        }
    }

    public void run() {
        // print available commands
        printHelp(false);
        // main game loop
        Scanner scanner = new Scanner(System.in);
        String input = "";
        while (!input.equalsIgnoreCase("leave")) {
            // print cursor
            System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR + "[IN_GAME] >>> " + SET_TEXT_COLOR_GREEN);
            // check input
            input = scanner.nextLine();
            var tokens = input.toLowerCase().split(" ");
            var cmd = tokens[0];
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            switch (cmd) {
                case "help" -> printHelp(false);
                case "leave" -> {
                    if (!leave()) {
                        input = "";
                    }
                }
                case "move" -> move(params);
                case "resign" -> resign();
                case "highlight" -> highlight(params);
                case "redraw" -> redraw();
                default -> printHelp(true);
            }
        }
    }

    @Override
    public void updateGame(LoadGameMessage message) {
        localGame = new Gson().fromJson(message.getGame(), ChessGame.class);
    }

    @Override
    public void printMessage(String message, boolean redText) {
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR);
        if (redText) {
            System.out.println(SET_TEXT_COLOR_RED + RESET_BG_COLOR + "\n" + message);
        } else {
            System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR + "\n" + message);
        }
        // print cursor
        System.out.print(RESET_TEXT_COLOR + RESET_BG_COLOR + "\n[IN_GAME] >>> " + SET_TEXT_COLOR_GREEN);
    }

    private boolean leave() {
        boolean result = wsFacade.leaveGame(authToken, gameID);
        if (!result) {
            System.out.println(SET_TEXT_COLOR_RED + RESET_BG_COLOR + "\nError: failed to leave");
        }
        return result;
    }

    private void resign() {
        try {
            wsFacade.resignGame(authToken, gameID);
        } catch (Exception e) {
            System.out.println(SET_TEXT_COLOR_RED + RESET_BG_COLOR + "\nError: failed to resign");
        }
    }

    // todo: add pawn promotion, add bad input
    private void move(String[] params) {
        if (isObserver) {
            System.out.println(SET_TEXT_COLOR_RED + RESET_BG_COLOR + "\nError: observer cannot make move");
            return;
        }
        char letterStart;
        int numStart;
        char letterFinish;
        int numFinish;
        String start;
        String finish;
        if (params.length != 2) {
            Scanner scanner = new Scanner(System.in);
            // ask for start
            System.out.print(RESET_TEXT_COLOR + "\nStart position: ");
            start = scanner.nextLine();
            // ask for finish
            System.out.print(RESET_TEXT_COLOR + "\nFinish position: ");
            finish = scanner.nextLine();
        } else {
            start = params[0];
            finish = params[1];
        }
        letterStart = start.charAt(0);
        numStart = Integer.parseInt(start.charAt(0) + "");
        letterFinish = finish.charAt(0);
        numFinish = Integer.parseInt(finish.charAt(0) + "");
        if (Character.isLetter(letterStart) && Character.isLetter(letterFinish)) {
            ChessPosition startPosition = new ChessPosition(numStart, coordinateMap.get(letterStart));
            ChessPosition finalPosition = new ChessPosition(numFinish, coordinateMap.get(letterFinish));
            wsFacade.makeMove(authToken, gameID, new ChessMove(startPosition, finalPosition, null));
        } else {
            System.out.println(SET_TEXT_COLOR_RED + "\nError: bad request");
        }
    }

    private void highlight(String[] params) {

    }

    private void redraw() {

    }

    private void printHelp(boolean isBadCommand) {
        if (isBadCommand) {
            System.out.println(SET_TEXT_COLOR_RED + "\nError: Please enter a valid command");
        }
        System.out.println(SET_TEXT_COLOR_BLUE + """
                \nredraw - redraws the chess board
                move <letter#> <letter#> - make a move, list start then final position
                highlight <letter#> - highlights all legal moves for the given space
                leave - leave the game
                resign - forfeit the game
                help - lists possible commands
                """);
    }

    private void printBoard(chess.ChessBoard board, boolean isWhite) {
        if (isWhite) {
            printLetters(false);
            printWhiteBoard(board);
            printLetters(false);
        } else {
            printLetters(true);
            printBlackBoard(board);
            printLetters(true);
        }
        System.out.println(RESET_TEXT_COLOR + RESET_BG_COLOR);
    }

    private void printWhiteBoard(ChessBoard board) {
        int colNum = 8;
        for (int i = 8; i > 0; i--) {
            boolean isLight = (i % 2 == 0);
            for (int j = 0; j < 10; j++) {
                isLight = printPiece(i, j, colNum, isLight, board);
            }
            System.out.print(RESET_BG_COLOR + "\n");
            colNum--;
        }
    }

    private void printBlackBoard(ChessBoard board) {
        int colNum = 1;
        for (int i = 1; i < 9; i++) {
            boolean isLight = (i % 2 == 1);
            for (int j = 9; j >= 0; j--) {
                isLight = printPiece(i, j, colNum, isLight, board);
            }
            System.out.print(RESET_BG_COLOR + "\n");
            colNum++;
        }
    }

    private boolean printPiece(int row, int col, int colNum, boolean isLight, ChessBoard board) {
        if (col == 0 || col == 9) {
            System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + colNum + " ");
            return isLight;
        } else {
            System.out.print((isLight ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK));
            ChessPiece piece = board.getPiece(new ChessPosition(row,col));
            if (piece == null) {
                System.out.print("   ");
            } else {
                System.out.print((piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE));
                System.out.print(" " + (pieceMap.get(piece.getPieceType())) + " ");
            }
            return !isLight;
        }
    }

    private void printLetters(boolean isReversed) {
        System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
        if (isReversed) {
            char letter = 'h';
            for (int i = 0; i < 10; i++) {
                if (i != 0 && i != 9) {
                    System.out.print(" " + letter + " ");
                    letter--;
                } else {
                    System.out.print("   ");
                }
            }
        } else {
            char letter = 'a';
            for (int i = 0; i < 10; i++) {
                if (i != 0 && i != 9) {
                    System.out.print(" "+ letter + " ");
                    letter++;
                } else {
                    System.out.print("   ");
                }
            }
        }
        System.out.print(RESET_BG_COLOR + "\n");
    }
}
