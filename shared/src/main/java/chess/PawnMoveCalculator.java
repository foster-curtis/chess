package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoveCalculator extends MoveCalculator {
    PawnMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<>();

        return moves;
    }
}
