package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTurn = TeamColor.WHITE;
    private boolean gameOver = false;
    private ChessBoard chessBoard = new ChessBoard();
    private ArrayList<ChessMove> previousMoveList;

    public ChessGame() {
        chessBoard.resetBoard();
        previousMoveList = new ArrayList<ChessMove>();
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

        // check for en passant
        if (chessBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
            if (startPosition.getRow() == 4 || startPosition.getRow() == 5) {
                ChessMove passantMove = isEnPassant(startPosition);
                if (passantMove != null) {
                    preMoveList.add(passantMove);
                }
            }
        }

        // check for castling
        if (chessBoard.getPiece(startPosition).getPieceType() == ChessPiece.PieceType.KING) {
            ChessMove castlingMoveQueen = isCastling(startPosition,true);
            if (castlingMoveQueen != null) {
                preMoveList.add(castlingMoveQueen);
            }
            ChessMove castlingMoveKing = isCastling(startPosition,false);
            if (castlingMoveKing != null) {
                preMoveList.add(castlingMoveKing);
            }
        }

        // check all possible moves for validity
        Collection<ChessMove> postMoveList = new ArrayList<ChessMove>();
        for (ChessMove move : preMoveList) {
            // pass temp board
            ChessBoard tempBoard = new ChessBoard(chessBoard);
            tempGame.setBoard(tempBoard);
            // make move
            tempGame.makeMoveOnBoard(move);
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
        // update en passant if needed
        if (piece.getPieceType() == ChessPiece.PieceType.PAWN && isEnPassant(move.getStartPosition()) != null) {
            move.setEnPassant(true);
        }
        int startCol = move.getStartPosition().getColumn();
        int endCol = move.getEndPosition().getColumn();
        // update castling queen side if needed
        if (piece.getPieceType() == ChessPiece.PieceType.KING && isCastling(move.getStartPosition(),true) != null &&
                (startCol - endCol) == 2) {
            move.setCastlingQueenSide(true);
        }
        // update castling king side if needed
        if (piece.getPieceType() == ChessPiece.PieceType.KING && isCastling(move.getStartPosition(),false) != null &&
                (endCol - startCol) == 2) {
            move.setCastlingKingSide(true);
        }
        // check if move is among valid moves else throw error
        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        for (ChessMove validMove : validMoves) {
            if (validMove.equals(move)) {
                makeMoveOnBoard(move);
                previousMoveList.add(move);
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

    // updates board, according to param move, doesn't check if valid
    private void makeMoveOnBoard(ChessMove move) {
        ChessPiece piece = chessBoard.getPiece(move.getStartPosition());
        // add new
        if (move.getPromotionPiece() == null) {
            chessBoard.addPiece(move.getEndPosition(), piece);
        } else {
            ChessPiece promotionPiece = new ChessPiece(piece.getTeamColor(),move.getPromotionPiece());
            chessBoard.addPiece(move.getEndPosition(), promotionPiece);
        }
        // add and erase rook if castling queen side
        if (move.isCastlingQueenSide()) {
            // add rook
            ChessPiece queenRook =
                    chessBoard.getPiece(new ChessPosition(move.getEndPosition().getRow(),move.getEndPosition().getColumn()-2));
            chessBoard.addPiece(new ChessPosition(move.getEndPosition().getRow(),move.getEndPosition().getColumn()+1),queenRook);
            // erase rook
            chessBoard.addPiece(new ChessPosition(move.getEndPosition().getRow(),move.getEndPosition().getColumn()-2),null);
        }
        // add and erase rook if castling king side
        if (move.isCastlingKingSide()) {
            // add rook
            ChessPiece kingRook =
                    chessBoard.getPiece(new ChessPosition(move.getEndPosition().getRow(),move.getEndPosition().getColumn()+1));
            chessBoard.addPiece(new ChessPosition(move.getEndPosition().getRow(),move.getEndPosition().getColumn()-1),kingRook);
            // erase rook
            chessBoard.addPiece(new ChessPosition(move.getEndPosition().getRow(),move.getEndPosition().getColumn()+1),null);
        }
        // erase killed pawn if en passant
        if (move.isEnPassant()) {
            if (chessBoard.getPiece(move.getEndPosition()).getTeamColor() == TeamColor.WHITE) {
                chessBoard.addPiece(new ChessPosition(move.getEndPosition().getRow()-1,move.getEndPosition().getColumn()), null);
            } else {
                chessBoard.addPiece(new ChessPosition(move.getEndPosition().getRow()+1,move.getEndPosition().getColumn()), null);
            }
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
        return inDanger(kingPosition,teamColor);
    }

    private boolean inDanger(ChessPosition position, ChessGame.TeamColor teamColor) {
        // check if in danger
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i,j));
                // if not null and not on the same team, check move list for if a move kills king
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moveList = piece.pieceMoves(chessBoard, new ChessPosition(i,j));
                    if(isKillsPiece(moveList, position)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isKillsPiece(Collection<ChessMove> moveList, ChessPosition position) {
        for (ChessMove move : moveList) {
            // if move kills king, return check true
            if (move.getEndPosition().equals(position)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && isStuck(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // check if in check and possible moves
        return !isInCheck(teamColor) && isStuck(teamColor);
    }

    // checks if any possible moves exist for team
    private boolean isStuck(TeamColor teamColor) {
        // add all valid moves of friendly pieces
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = chessBoard.getPiece(new ChessPosition(i,j));
                // if not null and same team, add valid moves of piece
                if (piece != null && piece.getTeamColor() == teamColor) {
                    possibleMoves.addAll(validMoves(new ChessPosition(i,j)));
                }
            }
        }
        // if empty, stalemate
        return possibleMoves.isEmpty();
    }

    // takes current move and checks if en passant possible, if yes returns the move if not returns null
    private ChessMove isEnPassant(ChessPosition position) {
        ChessGame.TeamColor goodColor = chessBoard.getPiece(position).getTeamColor();
        // check if previous move on record
        if (previousMoveList.isEmpty()) {
            return null;
        }
        ChessMove previousMove = previousMoveList.getLast();
        ChessPiece.PieceType previousPiece = chessBoard.getPiece(previousMove.getEndPosition()).getPieceType();

        // check if last move was a pawn
        if (previousPiece == ChessPiece.PieceType.PAWN) {
            // if white pawn
            if (goodColor == TeamColor.WHITE) {
                // check if last move was a pawn double move
                if (previousMove.getStartPosition().getRow() == 7 && previousMove.getEndPosition().getRow() == 5) {
                    // if to the right
                    if (previousMove.getStartPosition().getColumn() == position.getColumn() + 1) {
                        return new ChessMove(position,new ChessPosition(6,position.getColumn() + 1),true);
                        // if to the left
                    } else if (previousMove.getStartPosition().getColumn() == position.getColumn() - 1) {
                        return new ChessMove(position,new ChessPosition(6,position.getColumn() - 1),true);
                    }
                }
                // if black pawn
            } else {
                // check if last move was a pawn double move
                if (previousMove.getStartPosition().getRow() == 2 && previousMove.getEndPosition().getRow() == 4) {
                    // if to the right
                    if (previousMove.getStartPosition().getColumn() == position.getColumn() + 1) {
                        return new ChessMove(position,new ChessPosition(3,position.getColumn() + 1),true);
                        // if to the left
                    } else if (previousMove.getStartPosition().getColumn() == position.getColumn() - 1) {
                        return new ChessMove(position,new ChessPosition(3,position.getColumn() - 1),true);
                    }
                }
            }
        }
        return null;
    }

    // checks if castling is possible
    private ChessMove isCastling(ChessPosition position, boolean checkQueenSide) {
        ChessGame.TeamColor goodColor = chessBoard.getPiece(position).getTeamColor();
        int posRow = position.getRow();
        int posCol = position.getColumn();

        if (checkQueenSide && (posRow == 1 || posRow == 8) && posCol == 5 &&
             (chessBoard.getPiece(new ChessPosition(posRow, posCol - 1)) == null) &&
             (chessBoard.getPiece(new ChessPosition(posRow, posCol - 2)) == null) &&
             (chessBoard.getPiece(new ChessPosition(posRow, posCol - 3)) == null) &&
             !isInCheck(goodColor) &&
             !inDanger(new ChessPosition(posRow, posCol - 2), goodColor) &&
             !inDanger(new ChessPosition(posRow, posCol - 1), goodColor)) {
            // check if previous moves on record
            if (!previousMoveList.isEmpty()) {
                for (ChessMove move : previousMoveList) {
                    if (move.getStartPosition().equals(position) ||
                        move.getStartPosition().equals(new ChessPosition(posRow, posCol - 4))) {
                        return null;
                    }
                }
                ChessMove toReturn = new ChessMove(position, new ChessPosition(posRow, posCol - 2));
                toReturn.setCastlingQueenSide(true);
                return toReturn;
            } else {
                ChessMove toReturn = new ChessMove(position, new ChessPosition(posRow, posCol - 2));
                toReturn.setCastlingQueenSide(true);
                return toReturn;
            }
            // check if king side castle available
        } else if ((posRow == 1 || posRow == 8) && posCol == 5 &&
            (chessBoard.getPiece(new ChessPosition(posRow, posCol + 1)) == null) &&
            (chessBoard.getPiece(new ChessPosition(posRow, posCol + 2)) == null) &&
            !isInCheck(goodColor) &&
            !inDanger(new ChessPosition(posRow, posCol + 2), goodColor) &&
            !inDanger(new ChessPosition(posRow, posCol + 1), goodColor)) {
            // check if previous moves on record
            if (!previousMoveList.isEmpty()) {
                for (ChessMove move : previousMoveList) {
                    if (move.getStartPosition().equals(position) ||
                        move.getStartPosition().equals(new ChessPosition(posRow, posCol + 3))) {
                        return null;
                    }
                }
                ChessMove toReturn = new ChessMove(position, new ChessPosition(posRow, posCol + 2));
                toReturn.setCastlingQueenSide(true);
                return toReturn;
            } else {
                ChessMove toReturn = new ChessMove(position, new ChessPosition(posRow, posCol + 2));
                toReturn.setCastlingQueenSide(true);
                return toReturn;
            }
        }
        return null;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessBoard = board;
        previousMoveList = new ArrayList<ChessMove>();
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessBoard;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(chessBoard, chessGame.chessBoard) &&
                currentTurn == chessGame.currentTurn &&
                Objects.equals(previousMoveList, chessGame.previousMoveList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard, currentTurn, previousMoveList);
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "chessBoard=" + chessBoard +
                ", currentTurn=" + currentTurn +
                ", previousMoveList=" + previousMoveList +
                ", gameOver=" + gameOver +
                '}';
    }
}
