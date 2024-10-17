package chess.moveCalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class KingMoveCalculator extends MoveCalculator {
    public KingMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<>();

        moves.addAll(this.checkOneDirection(1, 1, 1));
        // Direction = up and left
        moves.addAll(this.checkOneDirection(1, -1, 1));
        // Direction = down and right
        moves.addAll(this.checkOneDirection(-1, 1, 1));
        // Direction = down and left
        moves.addAll(this.checkOneDirection(-1, -1, 1));
        // Direction = up
        moves.addAll(this.checkOneDirection(1, 0, 1));
        // Direction = left
        moves.addAll(this.checkOneDirection(0, -1, 1));
        // Direction = right
        moves.addAll(this.checkOneDirection(0, 1, 1));
        // Direction = down
        moves.addAll(this.checkOneDirection(-1, 0, 1));

        return moves;
    }
}
