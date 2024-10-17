package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class RookMoveCalculator extends MoveCalculator {
    public RookMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
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
