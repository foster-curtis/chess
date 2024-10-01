package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMoveCalculator extends MoveCalculator {
    KnightMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        // Direction = right and up
        moves.addAll(this.checkOneDirection(1, 2, 1));
        // Direction = left and up
        moves.addAll(this.checkOneDirection(1, -2, 1));
        // Direction = right and down
        moves.addAll(this.checkOneDirection(-1, 2, 1));
        // Direction = left and down
        moves.addAll(this.checkOneDirection(-1, -2, 1));
        // Direction = up and right
        moves.addAll(this.checkOneDirection(2, 1, 1));
        // Direction = up and left
        moves.addAll(this.checkOneDirection(2, -1, 1));
        // Direction = down and right
        moves.addAll(this.checkOneDirection(-2, 1, 1));
        // Direction = down and left
        moves.addAll(this.checkOneDirection(-2, -1, 1));

        return moves;
    }
}
