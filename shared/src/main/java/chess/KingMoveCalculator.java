package chess;

import java.util.Collection;
import java.util.List;

public class KingMoveCalculator extends MoveCalculator {
    KingMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = List.of();

        return moves;
    }
}
