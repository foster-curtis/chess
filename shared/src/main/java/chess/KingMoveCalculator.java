package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator extends MoveCalculator {
    KingMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<>();

        Collection<ChessMove> L1 = this.checkOneDirection(1, 1, 1);
        // Direction = up and left
        Collection<ChessMove> L2 = this.checkOneDirection(1, -1, 1);
        // Direction = down and right
        Collection<ChessMove> L3 = this.checkOneDirection(-1, 1, 1);
        // Direction = down and left
        Collection<ChessMove> L4 = this.checkOneDirection(-1, -1, 1);
        // Direction = up
        Collection<ChessMove> L5 = this.checkOneDirection(1, 0, 1);
        // Direction = left
        Collection<ChessMove> L6 = this.checkOneDirection(0, -1, 1);
        // Direction = right
        Collection<ChessMove> L7 = this.checkOneDirection(0, 1, 1);
        // Direction = down
        Collection<ChessMove> L8 = this.checkOneDirection(-1, 0, 1);

        moves.addAll(L1);
        moves.addAll(L2);
        moves.addAll(L3);
        moves.addAll(L4);
        moves.addAll(L5);
        moves.addAll(L6);
        moves.addAll(L7);
        moves.addAll(L8);

        return moves;
    }
}
