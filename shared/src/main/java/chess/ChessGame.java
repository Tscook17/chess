package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard chessBoard = new ChessBoard();
    private TeamColor currentTurn = TeamColor.WHITE;

    public ChessGame() {
        chessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        if (chessBoard.getPiece(startPosition) == null) {
            return null;
        }
        // create temp game
        Collection<ChessMove> preMoveList = chessBoard.getPiece(startPosition).pieceMoves(chessBoard, startPosition);
        ChessGame tempGame = new ChessGame();

        TeamColor teamColor = chessBoard.getPiece(startPosition).getTeamColor();
        Collection<ChessMove> postMoveList = new ArrayList<ChessMove>();

        for (ChessMove move : preMoveList) {
            // pass temp board
            ChessBoard tempBoard = new ChessBoard(chessBoard);
            tempGame.setBoard(tempBoard);

            // make move
            tempGame.makeTempMove(move);

            // check if not in check
            if (!tempGame.isInCheck(teamColor)) {
                postMoveList.add(move);
            }
        }
        return postMoveList;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        // check if teams turn
        if (piece == null || piece.getTeamColor() != currentTurn) {
            throw new InvalidMoveException();
        }
        // check if move is among valid moves else throw error
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        for (ChessMove validMove : validMoves) {
            if (validMove.equals(move)) {
                makeTempMove(move);
                // update current turn
                if (piece.getTeamColor() == TeamColor.WHITE) {
                    setTeamTurn(TeamColor.BLACK);
                } else {
                    setTeamTurn(TeamColor.WHITE);
                }
                return;
            }
        }
        throw new InvalidMoveException();
    }

    private void makeTempMove(ChessMove move) {
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        // add new
        if (move.getPromotionPiece() == null) {
            chessBoard.addPiece(move.getEndPosition(), piece);
        } else {
            ChessPiece promotionPiece = new ChessPiece(piece.getTeamColor(),move.getPromotionPiece());
            chessBoard.addPiece(move.getEndPosition(), promotionPiece);
        }

        // erase old
        chessBoard.addPiece(move.getStartPosition(), null);
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // get king location
        ChessPosition kingPosition = null;
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i,j));
                // if not null and same team and king, save as king position
                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    kingPosition = new ChessPosition(i,j);
                }
            }
        }

        // check if in danger
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i,j));
                // if not null and not on the same team, check move list for if a move kills king
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moveList = piece.pieceMoves(chessBoard, new ChessPosition(i,j));
                    for (ChessMove move : moveList) {
                        // if move kills king, return check true
                        if (move.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        // if no moves kill king return check false
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }
}
