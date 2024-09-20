package chess;

import java.util.Collection;
import java.util.List;

public class PawnMoveCalculator extends MoveCalculator {
    PawnMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = List.of();

        return moves;
    }
}
