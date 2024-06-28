package chess.helperClasses;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.util.Collection;
import java.util.LinkedList;

public class MoveCalculator {
    private final ChessBoard board;
    private final ChessPosition myPosition;

    public MoveCalculator(ChessBoard board, ChessPosition myPosition) {
        this.board = board;
        this.myPosition = myPosition;
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
}
