package chess;

import java.util.Collection;
import java.util.List;

public class KnightMoveCalculator extends MoveCalculator {
    KnightMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = List.of();

        return moves;
    }
}
