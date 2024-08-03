package ui.consolerepls;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Map;

import static ui.EscapeSequences.*;

public class GameplayRepl implements Runnable {
    private static Map<ChessPiece.PieceType, String> pieceMap = Map.of(
            ChessPiece.PieceType.KING, "K",
            ChessPiece.PieceType.QUEEN, "Q",
            ChessPiece.PieceType.KNIGHT, "N",
            ChessPiece.PieceType.BISHOP, "B",
            ChessPiece.PieceType.ROOK, "R",
            ChessPiece.PieceType.PAWN, "P"
    );

    public void run() {

    }

    public static void printBoard(chess.ChessBoard board, boolean isWhite) {
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

    private static void printWhiteBoard(ChessBoard board) {
        int colNum = 8;
        for (int i = 8; i > 0; i--) {
            boolean isLight = (i % 2 == 0);
            for (int j = 0; j < 10; j++) {
                if (j == 0 || j == 9) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                    System.out.print(" " + colNum + " ");
                } else {
                    System.out.print((isLight ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK));
                    isLight = !isLight;
                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                    if (piece == null) {
                        System.out.print("   ");
                    } else {
                        System.out.print((piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE));
                        System.out.print(" " + (pieceMap.get(piece.getPieceType())) + " ");
                    }
                }
            }
            System.out.print(RESET_BG_COLOR + "\n");
            colNum--;
        }
    }

    private static void printBlackBoard(ChessBoard board) {
        int colNum = 1;
        for (int i = 1; i < 9; i++) {
            boolean isLight = (i % 2 == 1);
            for (int j = 9; j >= 0; j--) {
                if (j == 0 || j == 9) {
                    System.out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK);
                    System.out.print(" " + colNum + " ");
                } else {
                    System.out.print((isLight ? SET_BG_COLOR_WHITE : SET_BG_COLOR_BLACK));
                    isLight = !isLight;
                    ChessPiece piece = board.getPiece(new ChessPosition(i,j));
                    if (piece == null) {
                        System.out.print("   ");
                    } else {
                        System.out.print((piece.getTeamColor() == ChessGame.TeamColor.WHITE ? SET_TEXT_COLOR_RED : SET_TEXT_COLOR_BLUE));
                        System.out.print(" " + (pieceMap.get(piece.getPieceType())) + " ");
                    }
                }
            }
            System.out.print(RESET_BG_COLOR + "\n");
            colNum++;
        }
    }

    private static void printLetters(boolean isReversed) {
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
