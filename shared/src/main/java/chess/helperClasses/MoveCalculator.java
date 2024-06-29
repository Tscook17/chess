package chess.helperClasses;

import chess.*;

import java.util.Collection;
import java.util.LinkedList;

public class MoveCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;
    private final int positionRow;
    private final int positionCol;
    private final Options enemyColor;
    private final Options teamColor;

    private enum Options {
        WHITE,
        BLACK,
        EMPTY
    }

    public MoveCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
        this.positionRow = myPosition.getRow();
        this.positionCol = myPosition.getColumn();
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE) {
            teamColor = Options.WHITE;
            enemyColor = Options.BLACK;
        } else {
            teamColor = Options.BLACK;
            enemyColor = Options.WHITE;
        }
    }

    public Collection<ChessMove> calculateMoves() {
        ChessPiece.PieceType pieceType = board.getPiece(myPosition).getPieceType();
        Collection<ChessMove> moveCollection;

        switch (pieceType) {
            case KING -> { moveCollection = calculateKing(); }
            case QUEEN -> { moveCollection = calculateQueen(); }
            case BISHOP -> { moveCollection = calculateBishop(); }
            case KNIGHT -> { moveCollection = calculateKnight(); }
            case ROOK -> { moveCollection = calculateRook(); }
            case PAWN -> { moveCollection = calculatePawn(); }
            default -> { moveCollection = new LinkedList<ChessMove>(); }
        }
        return moveCollection;
    }

    private Collection<ChessMove> calculateKing() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        if (pieceAtLocation(positionRow + 1,positionCol) != teamColor && !isOffBoard(positionRow,positionCol)) {
            moveCollection.add(new ChessMove())
        }

        return moveCollection;
    }

    private Collection<ChessMove> calculateQueen() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        return moveCollection;
    }

    private Collection<ChessMove> calculateBishop() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        return moveCollection;
    }

    private Collection<ChessMove> calculateKnight() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        return moveCollection;
    }

    private Collection<ChessMove> calculateRook() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        return moveCollection;
    }

    private Collection<ChessMove> calculatePawn() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        return moveCollection;
    }

    private boolean isOffBoard(int row, int col) {
        if (row >= 8 || row < 0) {
            return true;
        } else return col >= 8 || col < 0;
    }

    private Options pieceAtLocation(int row, int col) {
        ChessPosition location = new ChessPosition(row, col);
        if (board.getPiece(location).getTeamColor() == ChessGame.TeamColor.WHITE) {
            return Options.WHITE;
        } else if (board.getPiece(location).getTeamColor() == ChessGame.TeamColor.BLACK) {
            return Options.BLACK;
        } else {
            return Options.EMPTY;
        }
    }
}
