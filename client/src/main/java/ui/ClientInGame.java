package ui;

import chess.ChessGame;
import model.AuthData;
import ui.websocketmanager.WebSocketFacade;
import websocket.commands.UserGameCommand;

import java.io.IOException;

public class ClientInGame implements Client {
    private final AuthData currentUserAuth;
    private final WebSocketFacade ws;
    private Integer gameID;
    private State state = State.INGAME;
    private ChessGame chessGame;
    private ChessGame.TeamColor player_color;

    public ClientInGame(int port, AuthData currUserAuth, int gameID, ServerMessageObserver serverMessageObserver) {
        this.ws = new WebSocketFacade(port, serverMessageObserver, this);
        this.currentUserAuth = currUserAuth;
        this.gameID = gameID;

        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, currentUserAuth.authToken(), gameID);
        try {
            ws.send(command);
        } catch (IOException e) {
            System.out.println("You haven't handled this IO error yet");
        }
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String eval(String input) {
        return switch (input) {
            case ("2") -> redrawBoard();
            case ("3") -> leave();
            case ("4") -> makeMove();
            case ("5") -> resign();
            case ("6") -> highlightMoves();
            default -> help();
        };
    }

    private String redrawBoard() {
        return new BoardUI(chessGame.getBoard(), player_color).displayBoard();
    }

    private String leave() {
        this.state = State.LOGGEDIN;

        try {
            ws.send(new UserGameCommand(UserGameCommand.CommandType.LEAVE, currentUserAuth.authToken(), this.gameID));
        } catch (IOException e) {
            System.out.println("You haven't handled this IO error yet");
        }
        return "";
    }

    private String makeMove() {
        //TODO
        return "";
    }

    private String resign() {
        //TODO
        return "";
    }

    private String highlightMoves() {
        //TODO
        return "";
    }

    @Override
    public String help() {
        return """
                1. Help -> View list of available commands
                2. Redraw Chess Board -> Pretty self explanatory
                3. Leave -> Exit the game (You can come back and join later!)
                4. Make Move -> Move a piece on the board
                5. Resign -> Resign from the game. (You'll lose)
                6. Highlight Legal Moves -> Highlight all the legal moves of any piece
                """;
    }

    @Override
    public AuthData getCurrentUserAuth() {
        return this.currentUserAuth;
    }

    @Override
    public Integer getGameID() {
        return this.gameID;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    public void setPlayer_color(ChessGame.TeamColor player_color) {
        this.player_color = player_color;
    }
}
