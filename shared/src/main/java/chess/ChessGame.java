package chess;

import java.util.ArrayList;
import java.util.Collection;


/**
 * For a class that can manage a chess game, making moves on a gameBoard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard gameBoard;
    TeamColor currentTeamTurn;
    //ChessPiece threat = null;

    public ChessGame() {
        ChessBoard newBoard = new ChessBoard();
        newBoard.resetBoard();
        this.gameBoard = newBoard;
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
        ArrayList<ChessMove> valid = new ArrayList<>();
        if (gameBoard.getPiece(startPosition) == null) {
            return null;
        }
        Collection<ChessMove> possibleMoves = gameBoard.getPiece(startPosition).pieceMoves(gameBoard, startPosition);
        for (var move : possibleMoves) {
            ChessPiece capturedPiece = null;
            if (gameBoard.getPiece(move.getEndPosition()) != null) {
                capturedPiece = gameBoard.getPiece(move.getEndPosition());
            }
            ChessGame.TeamColor color = gameBoard.getPiece(startPosition).getTeamColor();
            gameBoard.makeMove(move);
            if (!isInCheck(color)) {
                valid.add(move);
            }
            gameBoard.undoMove(move, capturedPiece);
        }
        return valid;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (gameBoard.getPiece(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Invalid Move: No piece at start position.");
        }
        if (this.currentTeamTurn != gameBoard.getPiece(move.getStartPosition()).getTeamColor()) {
            throw new InvalidMoveException("Invalid Move: Out of turn.");
        }
        Collection<ChessMove> valid = validMoves(move.getStartPosition());
        boolean isValid = false;
        for (var m : valid) {
            if (m.equals(move)) {
                isValid = true;
                gameBoard.makeMove(move);
                this.currentTeamTurn = switch (this.getTeamTurn()) {
                    case WHITE -> TeamColor.BLACK;
                    case BLACK -> TeamColor.WHITE;
                };
                break;
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
                ChessPiece piece = gameBoard.getPiece(new ChessPosition(row, col));
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> moves = gameBoard.getPiece(new ChessPosition(row, col)).pieceMoves(gameBoard, new ChessPosition(row, col));
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
        if (isInCheck(teamColor)) {
            for (int row = 1; row <= 8; row++) {
                for (int col = 1; col <= 8; col++) {
                    ChessPiece piece = gameBoard.getPiece(new ChessPosition(row, col));
                    if (piece == null) {
                        continue;
                    }
                    if (piece.getTeamColor() == teamColor) {
                        Collection<ChessMove> possibleMoves = piece.pieceMoves(gameBoard, new ChessPosition(row, col));
                        for (var move : possibleMoves) {
                            ChessPiece capturedPiece = null;
                            if (gameBoard.getPiece(move.getEndPosition()) != null) {
                                capturedPiece = gameBoard.getPiece(move.getEndPosition());
                            }
                            gameBoard.makeMove(move);
                            if (!isInCheck(teamColor)) {
                                return false;
                            }
                            gameBoard.undoMove(move, capturedPiece);
                        }
                    }
                }
            }
            return true;
        } else {
            return false;
        }
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
                ChessPiece piece = gameBoard.getPiece(new ChessPosition(row, col));
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
        return !isInCheckmate(teamColor);
    }

    /**
     * Sets this game's chessboard with a given gameBoard
     *
     * @param gameBoard the new gameBoard to use
     */
    public void setBoard(ChessBoard gameBoard) {
        //Copies the gameBoard parameter to a new instance of ChessBoard
        this.gameBoard = new ChessBoard(gameBoard);
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.gameBoard;
    }
}
