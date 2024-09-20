package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends MoveCalculator {
    PawnMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    public boolean checkPromotion() {
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

    public Collection<ChessMove> checkAttack(int direction) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPosition right = this.getStart();
        right.offset(direction, 1);
        ChessPosition left = this.getStart();
        left.offset(direction, -1);
        Collection<ChessMove> L1 = checkOneSide(left);
        Collection<ChessMove> L2 = checkOneSide(right);
        moves.addAll(L1);
        moves.addAll(L2);
        return moves;
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
//        int limit = 1;
//        if (((this.getPieceColor() == ChessGame.TeamColor.BLACK) && (this.getStart().getRow() == 7)) || ((this.getPieceColor() == ChessGame.TeamColor.WHITE) && (this.getStart().getRow() == 2))) {
//            limit = 2;
//        }
        // checkAttack takes 1 for WHITE and -1 for BLACK
        if (this.getPieceColor() == ChessGame.TeamColor.WHITE) {
            moves.addAll(this.checkAttack(1));
        }
        if (this.getPieceColor() == ChessGame.TeamColor.BLACK) {
            moves.addAll(this.checkAttack(-1));
        }

        return moves;
    }
}
