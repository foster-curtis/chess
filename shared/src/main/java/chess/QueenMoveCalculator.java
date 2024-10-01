package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoveCalculator extends MoveCalculator {
    QueenMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        // Direction = up and right
        moves.addAll(this.checkOneDirection(1, 1, 10));
        // Direction = up and left
        moves.addAll(this.checkOneDirection(1, -1, 10));
        // Direction = down and right
        moves.addAll(this.checkOneDirection(-1, 1, 10));
        // Direction = down and left
        moves.addAll(this.checkOneDirection(-1, -1, 10));
        // Direction = up
        moves.addAll(this.checkOneDirection(1, 0, 10));
        // Direction = left
        moves.addAll(this.checkOneDirection(0, -1, 10));
        // Direction = right
        moves.addAll(this.checkOneDirection(0, 1, 10));
        // Direction = down
        moves.addAll(this.checkOneDirection(-1, 0, 10));

        return moves;
    }
}
