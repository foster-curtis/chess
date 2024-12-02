package websocket.messages;

import chess.ChessGame;
import chess.ChessMove;
import model.GameData;

public class LoadGameMessage extends ServerMessage {
    private final GameData game;
    private final ChessGame.TeamColor color;
    private final ChessMove move;

    public LoadGameMessage(GameData game, ChessGame.TeamColor color, ChessMove move) {
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
        this.color = color;
        this.move = move;
    }

    public GameData getGame() {
        return game;
    }

    public ChessGame.TeamColor getColor() {
        return color;
    }

    public ChessMove getMove() {
        return move;
    }
}
