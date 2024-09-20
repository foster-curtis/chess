package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BishopMoveCalculator extends MoveCalculator {
    BishopMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        // Direction = up and right
        Collection<ChessMove> L1 = this.checkOneDirection(1, 1, 10);
        // Direction = up and left
        Collection<ChessMove> L2 = this.checkOneDirection(1, -1, 10);
        // Direction = down and right
        Collection<ChessMove> L3 = this.checkOneDirection(-1, 1, 10);
        // Direction = down and left
        Collection<ChessMove> L4 = this.checkOneDirection(-1, -1, 10);

        moves.addAll(L1);
        moves.addAll(L2);
        moves.addAll(L3);
        moves.addAll(L4);

        return moves;
    }
}
