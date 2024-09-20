package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends MoveCalculator {
    PawnMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    private boolean checkPromotion() {
        return true;
    }

    private Collection<ChessMove> checkOneSide(ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        if (inBoardRange(position.getRow(), position.getColumn()) && (this.getBoard().getPiece(position) != null)) {
            if (this.getBoard().getPiece(position).getTeamColor() != this.getPieceColor()) {
                //checkPromotion()
                moves.add(new ChessMove(this.getStart(), position, null));
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
        Collection<ChessMove> L1 = checkOneSide(left);
        Collection<ChessMove> L2 = checkOneSide(right);
        moves.addAll(L1);
        moves.addAll(L2);
        return moves;
    }

    private Collection<ChessMove> checkMoves(int startRow, int direction) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPosition end = new ChessPosition(this.getStart().getRow(), this.getStart().getColumn());
        end.offset(direction, 0);
        if (this.getBoard().getPiece(end) != null) {
            return moves;
        } else {
            //checkPromotion
            moves.add(new ChessMove(this.getStart(), end, null));
        }
        if (this.getStart().getRow() == startRow) {
            end.offset(direction, 0);
            if (this.getBoard().getPiece(end) == null) {
                moves.add(new ChessMove(this.getStart(), end, null));
            }
        }
        return moves;
    }

    @Override
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
