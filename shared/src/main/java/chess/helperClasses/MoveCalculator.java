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
        // calculate move set based on piece type
        switch (pieceType) {
            case KING -> moveCollection = calculateKing();
            case QUEEN -> moveCollection = calculateQueen();
            case BISHOP -> moveCollection = calculateBishop();
            case KNIGHT -> moveCollection = calculateKnight();
            case ROOK -> moveCollection = calculateRook();
            case PAWN -> moveCollection = calculatePawn();
            default -> moveCollection = new LinkedList<ChessMove>();
        }
        return moveCollection;
    }

    private Collection<ChessMove> calculateKing() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        // check up
        if (isOnBoard(positionRow + 1,positionCol) && pieceAtLocation(positionRow + 1,positionCol) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + 1,positionCol)));
        }
        // check down
        if (isOnBoard(positionRow - 1,positionCol) && pieceAtLocation(positionRow - 1,positionCol) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - 1,positionCol)));
        }
        // check right
        if (isOnBoard(positionRow,positionCol + 1) && pieceAtLocation(positionRow,positionCol + 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow,positionCol + 1)));
        }
        // check left
        if (isOnBoard(positionRow,positionCol - 1) && pieceAtLocation(positionRow,positionCol - 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow,positionCol - 1)));
        }
        // check up right diagonal
        if (isOnBoard(positionRow + 1,positionCol + 1) && pieceAtLocation(positionRow + 1,positionCol + 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + 1,positionCol + 1)));
        }
        // check down left diagonal
        if (isOnBoard(positionRow - 1,positionCol - 1) && pieceAtLocation(positionRow - 1,positionCol - 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - 1,positionCol - 1)));
        }
        // check up left diagonal
        if (isOnBoard(positionRow + 1,positionCol - 1) && pieceAtLocation(positionRow + 1,positionCol - 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + 1,positionCol - 1)));
        }
        // check down right diagonal
        if (isOnBoard(positionRow - 1,positionCol + 1) && pieceAtLocation(positionRow - 1,positionCol + 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - 1,positionCol + 1)));
        }

        return moveCollection;
    }

    private Collection<ChessMove> calculateQueen() {
        Collection<ChessMove> moveCollection = calculateBishop();
        moveCollection.addAll(calculateRook());
        return moveCollection;
    }

    private Collection<ChessMove> calculateBishop() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        // check up right diagonal
        for (int i = 1;;i++) {
            if (!isOnBoard(positionRow + i,positionCol + i) || pieceAtLocation(positionRow + i,positionCol + i) == teamColor) {
                break;
            } else if (pieceAtLocation(positionRow + i,positionCol + i) == enemyColor) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + i,positionCol + i)));
                break;
            } else {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + i,positionCol + i)));
            }
        }
        // check up left diagonal
        for (int i = 1;;i++) {
            if (!isOnBoard(positionRow + i,positionCol - i) || pieceAtLocation(positionRow + i,positionCol - i) == teamColor) {
                break;
            } else if (pieceAtLocation(positionRow + i,positionCol - i) == enemyColor) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + i,positionCol - i)));
                break;
            } else {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + i,positionCol - i)));
            }
        }
        // check down right diagonal
        for (int i = 1;;i++) {
            if (!isOnBoard(positionRow - i,positionCol + i) || pieceAtLocation(positionRow - i,positionCol + i) == teamColor) {
                break;
            } else if (pieceAtLocation(positionRow - i,positionCol + i) == enemyColor) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - i,positionCol + i)));
                break;
            } else {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - i,positionCol + i)));
            }
        }
        // check down left diagonal
        for (int i = 1;;i++) {
            if (!isOnBoard(positionRow - i,positionCol - i) || pieceAtLocation(positionRow - i,positionCol - i) == teamColor) {
                break;
            } else if (pieceAtLocation(positionRow - i,positionCol - i) == enemyColor) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - i,positionCol - i)));
                break;
            } else {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - i,positionCol - i)));
            }
        }

        return moveCollection;
    }

    private Collection<ChessMove> calculateKnight() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        // check up left
        if (isOnBoard(positionRow + 2,positionCol - 1) && pieceAtLocation(positionRow + 2,positionCol - 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + 2,positionCol - 1)));
        }
        // check up right
        if (isOnBoard(positionRow + 2,positionCol + 1) && pieceAtLocation(positionRow + 2,positionCol + 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + 2,positionCol + 1)));
        }
        // check down left
        if (isOnBoard(positionRow - 2,positionCol - 1) && pieceAtLocation(positionRow - 2,positionCol - 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - 2,positionCol - 1)));
        }
        // check down right
        if (isOnBoard(positionRow - 2,positionCol + 1) && pieceAtLocation(positionRow - 2,positionCol + 1) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - 2,positionCol + 1)));
        }
        // check right up
        if (isOnBoard(positionRow + 1,positionCol + 2) && pieceAtLocation(positionRow + 1,positionCol + 2) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + 1,positionCol + 2)));
        }
        // check right down
        if (isOnBoard(positionRow - 1,positionCol + 2) && pieceAtLocation(positionRow - 1,positionCol + 2) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - 1,positionCol + 2)));
        }
        // check left up
        if (isOnBoard(positionRow + 1,positionCol - 2) && pieceAtLocation(positionRow + 1,positionCol - 2) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + 1,positionCol - 2)));
        }
        // check right down
        if (isOnBoard(positionRow - 1,positionCol - 2) && pieceAtLocation(positionRow - 1,positionCol - 2) != teamColor) {
            moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - 1,positionCol - 2)));
        }
        return moveCollection;
    }

    private Collection<ChessMove> calculateRook() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        // check right
        for (int i = 1;;i++) {
            if (!isOnBoard(positionRow,positionCol + i) || pieceAtLocation(positionRow,positionCol + i) == teamColor) {
                break;
            } else if (pieceAtLocation(positionRow,positionCol + i) == enemyColor) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow,positionCol + i)));
                break;
            } else {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow,positionCol + i)));
            }
        }
        // check left
        for (int i = 1;;i++) {
            if (!isOnBoard(positionRow,positionCol - i) || pieceAtLocation(positionRow,positionCol - i) == teamColor) {
                break;
            } else if (pieceAtLocation(positionRow,positionCol - i) == enemyColor) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow,positionCol - i)));
                break;
            } else {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow,positionCol - i)));
            }
        }
        // check up
        for (int i = 1;;i++) {
            if (!isOnBoard(positionRow + i,positionCol) || pieceAtLocation(positionRow + i,positionCol) == teamColor) {
                break;
            } else if (pieceAtLocation(positionRow + i,positionCol) == enemyColor) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + i,positionCol)));
                break;
            } else {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + i,positionCol)));
            }
        }
        // check down
        for (int i = 1;;i++) {
            if (!isOnBoard(positionRow - i,positionCol) || pieceAtLocation(positionRow - i,positionCol) == teamColor) {
                break;
            } else if (pieceAtLocation(positionRow - i,positionCol) == enemyColor) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - i,positionCol)));
                break;
            } else {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - i,positionCol)));
            }
        }
        return moveCollection;
    }

    private Collection<ChessMove> calculatePawn() {
        Collection<ChessMove> moveCollection = new LinkedList<ChessMove>();
        // if white
        if (teamColor == Options.WHITE) {
            // check above
            if (pieceAtLocation(positionRow + 1, positionCol) == Options.EMPTY) {
                if ((positionRow + 1) == 8) {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol), ChessPiece.PieceType.QUEEN));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol), ChessPiece.PieceType.BISHOP));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol), ChessPiece.PieceType.KNIGHT));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol), ChessPiece.PieceType.ROOK));
                } else {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol)));
                }
            }
            // check right kill
            if (isOnBoard(positionRow + 1, positionCol + 1) && pieceAtLocation(positionRow + 1, positionCol + 1) == Options.BLACK) {
                if ((positionRow + 1) == 8) {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol + 1), ChessPiece.PieceType.QUEEN));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol + 1), ChessPiece.PieceType.BISHOP));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol + 1), ChessPiece.PieceType.KNIGHT));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol + 1), ChessPiece.PieceType.ROOK));
                } else {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol + 1)));
                }
            }
            // check left kill
            if (isOnBoard(positionRow + 1, positionCol - 1) && pieceAtLocation(positionRow + 1, positionCol - 1) == Options.BLACK) {
                if ((positionRow + 1) == 8) {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol - 1), ChessPiece.PieceType.QUEEN));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol - 1), ChessPiece.PieceType.BISHOP));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol - 1), ChessPiece.PieceType.KNIGHT));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol - 1), ChessPiece.PieceType.ROOK));
                } else {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow + 1, positionCol - 1)));
                }
            }
            // check if at start
            if (positionRow == 2 && pieceAtLocation(positionRow + 1, positionCol) == Options.EMPTY && pieceAtLocation(positionRow + 2, positionCol) == Options.EMPTY) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow + 2,positionCol)));
            }
            // if black
        } else {
            // check below
            if (pieceAtLocation(positionRow - 1, positionCol) == Options.EMPTY) {
                if ((positionRow - 1) == 1) {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol), ChessPiece.PieceType.BISHOP));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol), ChessPiece.PieceType.KNIGHT));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol), ChessPiece.PieceType.QUEEN));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol), ChessPiece.PieceType.ROOK));
                } else {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol)));
                }
            }
            // check right kill
            if (isOnBoard(positionRow - 1, positionCol + 1) && pieceAtLocation(positionRow - 1, positionCol + 1) == Options.WHITE) {
                if ((positionRow - 1) == 1) {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol + 1), ChessPiece.PieceType.BISHOP));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol + 1), ChessPiece.PieceType.KNIGHT));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol + 1), ChessPiece.PieceType.QUEEN));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol + 1), ChessPiece.PieceType.ROOK));
                } else {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol + 1)));
                }
            }
            // check left kill
            if (isOnBoard(positionRow - 1, positionCol - 1) && pieceAtLocation(positionRow - 1, positionCol - 1) == Options.WHITE) {
                if ((positionRow - 1) == 1) {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol - 1), ChessPiece.PieceType.BISHOP));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol - 1), ChessPiece.PieceType.KNIGHT));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol - 1), ChessPiece.PieceType.QUEEN));
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol - 1), ChessPiece.PieceType.ROOK));
                } else {
                    moveCollection.add(new ChessMove(myPosition, new ChessPosition(positionRow - 1, positionCol - 1)));
                }
            }
            // check if at start
            if (positionRow == 7 && pieceAtLocation(positionRow - 1, positionCol) == Options.EMPTY && pieceAtLocation(positionRow - 2, positionCol) == Options.EMPTY) {
                moveCollection.add(new ChessMove(myPosition,new ChessPosition(positionRow - 2,positionCol)));
            }
        }
        return moveCollection;
    }

    // check if the proposed position is on the board
    private boolean isOnBoard(int row, int col) {
        if (row > 8 || row < 1) {
            return false;
        } else return !(col > 8 || col < 1);
    }

    // returns the contents of the proposed position on the board
    private Options pieceAtLocation(int row, int col) {
        ChessPosition location = new ChessPosition(row, col);
        if (board.getPiece(location) == null) {
            return Options.EMPTY;
        } else if (board.getPiece(location).getTeamColor() == ChessGame.TeamColor.WHITE) {
            return Options.WHITE;
        } else {
            return Options.BLACK;
        }
    }
}
