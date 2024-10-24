package chess;

import chess.movecalculators.*;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessPiece.PieceType type;
    private final ChessGame.TeamColor pieceColor;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (this.getPieceType()) {
            case BISHOP -> new BishopMoveCalculator(myPosition, this.getTeamColor(), board).calculateMoves();
            case ROOK -> new RookMoveCalculator(myPosition, this.getTeamColor(), board).calculateMoves();
            case QUEEN -> new QueenMoveCalculator(myPosition, this.getTeamColor(), board).calculateMoves();
            case KNIGHT -> new KnightMoveCalculator(myPosition, this.getTeamColor(), board).calculateMoves();
            case KING -> new KingMoveCalculator(myPosition, this.getTeamColor(), board).calculateMoves();
            case PAWN -> new PawnMoveCalculator(myPosition, this.getTeamColor(), board).calculateMoves();
        };
    }

    @Override
    public String toString() {
        return "{" + pieceColor + "-" + type + "}";
    }
}
