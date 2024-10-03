package chess;

import java.util.*;

import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessPiece.PieceType.*;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[8][8];
    }

    public ChessBoard(ChessBoard board) {
        this.board = board.getBoard();
    }

    public void makeMove(ChessMove move) {
        ChessPiece piece = board[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1];
        board[move.getStartPosition().getRow() - 1][move.getStartPosition().getColumn() - 1] = null;
        board[move.getEndPosition().getRow() - 1][move.getEndPosition().getColumn() - 1] = piece;
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        this.board[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return this.board[position.getRow() - 1][position.getColumn() - 1];
    }

    public ChessPiece[][] getBoard() {
        return board;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < 8; row++) {
            sb.append("|");
            for (int col = 0; col < 8; col++) {
                assert board[row][col] != null;
                String symbol = board[row][col].getPieceType().toString();
                if (Objects.equals(symbol, "KNIGHT")) {
                    symbol = "n";
                } else {
                    symbol = String.valueOf(symbol.charAt(0));
                }
                if (board[row][col].getTeamColor() == WHITE) {
                    sb.append(symbol.toUpperCase());
                } else {
                    sb.append(symbol.toLowerCase());
                }
                sb.append('|');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessBoard that)) return false;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    /**
     * Sets the gameBoard to the default starting gameBoard
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        //Generate and add pawns
        for (int i = 0; i < 8; i++) {
            this.addPiece(new ChessPosition(2, i + 1), new ChessPiece(WHITE, ChessPiece.PieceType.PAWN));
            this.addPiece(new ChessPosition(7, i + 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        }
        //Generate back row pieces
        ChessPiece.PieceType[] typeList = {ROOK, KNIGHT, BISHOP, QUEEN, KING, BISHOP, KNIGHT, ROOK};
        for (int i = 0; i < 8; i++) {
            this.addPiece(new ChessPosition(1, i + 1), new ChessPiece(WHITE, typeList[i]));
        }
        for (int i = 7; i >= 0; i--) {
            this.addPiece(new ChessPosition(8, i + 1), new ChessPiece(ChessGame.TeamColor.BLACK, typeList[i]));
        }
    }
}
