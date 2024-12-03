package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand {
    private final ChessMove move;
    private final String stringMove;

    public MakeMoveCommand(String authToken, Integer gameID, ChessMove move, String stringMove) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = move;
        this.stringMove = stringMove;
    }

    public ChessMove getMove() {
        return move;
    }

    public String getStringMove() {
        return stringMove;
    }
}
