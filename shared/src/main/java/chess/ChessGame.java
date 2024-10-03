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
    ChessBoard board;
    TeamColor currentTeamTurn;
    //ChessPiece threat = null;

    public ChessGame() {
        ChessBoard newBoard = new ChessBoard();
        newBoard.resetBoard();
        this.board = newBoard;
        this.currentTeamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;
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
        if (board.getPiece(startPosition) == null) {
            return null;
        } else {
            ArrayList<ChessMove> valid = new ArrayList<>();
            Collection<ChessMove> possibleMoves = board.getPiece(startPosition).pieceMoves(board, startPosition);
            for (var move : possibleMoves) {
                board.makeMove(move);
                if (!isInCheck(this.getTeamTurn())) {
                    valid.add(move);
                }
                ChessMove undo = new ChessMove(move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece());
                undo.undoMove();
                board.makeMove(undo);
            }
            return valid;
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (this.currentTeamTurn != getTeamTurn()) {
            throw new InvalidMoveException("Invalid Move: Out of turn.");
        }
        Collection<ChessMove> valid = validMoves(move.getStartPosition());
        boolean isValid = false;
        for (var m : valid) {
            if (m == move) {
                isValid = true;
                board.makeMove(move);
            }
        }
        if (!isValid) {
            throw new InvalidMoveException("Invalid Move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     * <p>
     * We need to check if a team is in check before they attempt to make a move.
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = board.getPiece(new ChessPosition(row, col)).pieceMoves(board, new ChessPosition(row, col));
                    for (var move : moves) {
                        if (move.threatensKing) {
                            return true;
                        }
                    }
                }
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
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> possibleMoves = piece.pieceMoves(board, new ChessPosition(row, col));
                    for (var move : possibleMoves) {
                        board.makeMove(move);
                        if (!isInCheck(this.getTeamTurn())) {
                            return false;
                        }
                        ChessMove undo = new ChessMove(move.getStartPosition(), move.getEndPosition(), move.getPromotionPiece());
                        undo.undoMove();
                        board.makeMove(undo);
                    }
                }
            }
        }
        return true;
        //This should be similar to valid moves I think. Get the pieceMoves values from all
        //the pieces, and check each one, except this time you're checking to see if it would
        //NOT result in check. If there is at least one move that can do so, return false.
        //otherwise, return true.
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPiece piece = board.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> moves = validMoves(new ChessPosition(row, col));
                    if (!moves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        //Copies the board parameter to a new instance of ChessBoard
        this.board = new ChessBoard(board);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }
}
