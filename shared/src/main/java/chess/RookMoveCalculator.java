package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class RookMoveCalculator extends MoveCalculator {
    RookMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        // Direction = up
        Collection<ChessMove> L1 = this.checkOneDirection(1, 0);
        // Direction = left
        Collection<ChessMove> L2 = this.checkOneDirection(0, -1);
        // Direction = right
        Collection<ChessMove> L3 = this.checkOneDirection(0, 1);
        // Direction = down
        Collection<ChessMove> L4 = this.checkOneDirection(-1, 0);

        moves.addAll(L1);
        moves.addAll(L2);
        moves.addAll(L3);
        moves.addAll(L4);

        return moves;
    }
}
