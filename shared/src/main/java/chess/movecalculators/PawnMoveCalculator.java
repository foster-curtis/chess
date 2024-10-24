package chess.movecalculators;

import chess.*;

import java.util.ArrayList;
import java.util.Collection;

import static chess.ChessPiece.PieceType.KING;

public class PawnMoveCalculator extends MoveCalculator {
    public PawnMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    private boolean checkPromotion(ChessPosition position) {
        return switch (this.getPieceColor()) {
            case WHITE -> position.getRow() == 8;
            case BLACK -> position.getRow() == 1;
        };
    }

    private Collection<ChessMove> checkOneSide(ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (inBoardRange(position.getRow(), position.getColumn()) && (this.getBoard().getPiece(position) != null)) {
            if (this.getBoard().getPiece(position).getTeamColor() != this.getPieceColor()) {
                if (this.getBoard().getPiece(position).getPieceType() == KING) {
                    moves.add(new ChessMove(this.getStart(), position, null, true));
                } else if (checkPromotion(position)) {
                    moves.add(new ChessMove(this.getStart(), position, ChessPiece.PieceType.ROOK));
                    moves.add(new ChessMove(this.getStart(), position, ChessPiece.PieceType.QUEEN));
                    moves.add(new ChessMove(this.getStart(), position, ChessPiece.PieceType.KNIGHT));
                    moves.add(new ChessMove(this.getStart(), position, ChessPiece.PieceType.BISHOP));
                } else {
                    moves.add(new ChessMove(this.getStart(), position, null));
                }
            }
        }
        return moves;
    }

    private Collection<ChessMove> checkAttack(int direction) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPosition right = new ChessPosition(this.getStart().getRow(), this.getStart().getColumn());
        right.offset(direction, 1);
        ChessPosition left = new ChessPosition(this.getStart().getRow(), this.getStart().getColumn());
        left.offset(direction, -1);
        moves.addAll(checkOneSide(left));
        moves.addAll(checkOneSide(right));
        return moves;
    }

    private Collection<ChessMove> checkMoves(int startRow, int direction) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPosition end = new ChessPosition(this.getStart().getRow(), this.getStart().getColumn());
        end.offset(direction, 0);
        if (this.getBoard().getPiece(end) != null) {
            return moves;
        } else {
            var endPosition = new ChessPosition(end.getRow(), end.getColumn());
            if (checkPromotion(endPosition)) {
                moves.add(new ChessMove(this.getStart(), endPosition, ChessPiece.PieceType.ROOK));
                moves.add(new ChessMove(this.getStart(), endPosition, ChessPiece.PieceType.QUEEN));
                moves.add(new ChessMove(this.getStart(), endPosition, ChessPiece.PieceType.KNIGHT));
                moves.add(new ChessMove(this.getStart(), endPosition, ChessPiece.PieceType.BISHOP));
            } else {
                moves.add(new ChessMove(this.getStart(), endPosition, null));
            }
        }
        if (this.getStart().getRow() == startRow) {
            end.offset(direction, 0);
            if (this.getBoard().getPiece(end) == null) {
                var endPosition = new ChessPosition(end.getRow(), end.getColumn());
                moves.add(new ChessMove(this.getStart(), endPosition, null));
            }
        }
        return moves;
    }

    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        if (this.getPieceColor() == ChessGame.TeamColor.WHITE) {
            moves.addAll(this.checkAttack(1));
            moves.addAll(this.checkMoves(2, 1));
        }
        if (this.getPieceColor() == ChessGame.TeamColor.BLACK) {
            moves.addAll(this.checkAttack(-1));
            moves.addAll(this.checkMoves(7, -1));
        }

        return moves;
    }
}
