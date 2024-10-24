package chess.movecalculators;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMoveCalculator extends MoveCalculator {
    public BishopMoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        super(start, pieceColor, board);
    }

    @Override
    public Collection<ChessMove> calculateMoves() {
        Collection<ChessMove> moves = new ArrayList<>();
        // Direction = up and right
        moves.addAll(this.checkOneDirection(1, 1, 10));
        // Direction = up and left
        moves.addAll(this.checkOneDirection(1, -1, 10));
        // Direction = down and right
        moves.addAll(this.checkOneDirection(-1, 1, 10));
        // Direction = down and left
        moves.addAll(this.checkOneDirection(-1, -1, 10));

        return moves;
    }
}
