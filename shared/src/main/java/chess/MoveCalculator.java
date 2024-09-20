package chess;

import java.util.ArrayList;
import java.util.Collection;

public abstract class MoveCalculator {
    private final ChessPosition start;
    private final ChessGame.TeamColor pieceColor;
    private final ChessBoard board;

    MoveCalculator(ChessPosition start, ChessGame.TeamColor pieceColor, ChessBoard board) {
        this.start = start;
        this.pieceColor = pieceColor;
        this.board = board;

    }

    public abstract Collection<ChessMove> calculateMoves();

    public Collection<ChessMove> checkOneDirection(int x, int y, int limit) {
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPosition end = new ChessPosition(this.getStart().getRow(), this.getStart().getColumn());
        end.offset(x, y);
        int numMoves = 0;
        while (inBoardRange(end.getRow(), end.getColumn()) && numMoves < limit) {
            ChessPiece piece = this.getBoard().getPiece(end);
            if (piece != null) {
                if (this.getPieceColor() != piece.getTeamColor()) {
                    moves.add(new ChessMove(this.getStart(), end, null));
                }
                break;
            }
            var endPosition = new ChessPosition(end.getRow(), end.getColumn());
            moves.add(new ChessMove(this.getStart(), endPosition, null));
            end.offset(x, y);
            numMoves++;
        }
        return moves;
    }

    public boolean inBoardRange(int r, int c) {
        return (r >= 1) && (r <= 8) && (c >= 1) && (c <= 8);
    }

    public ChessBoard getBoard() {
        return board;
    }

    public ChessGame.TeamColor getPieceColor() {
        return pieceColor;
    }

    public ChessPosition getStart() {
        return start;
    }
}
