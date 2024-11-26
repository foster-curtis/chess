package ui;

import model.AuthData;
import ui.websocketmanager.WebSocketFacade;
import websocket.commands.UserGameCommand;

public class ClientInGame implements Client {
    private final AuthData currentUserAuth;
    private final WebSocketFacade ws;
    private State state = State.INGAME;

    public ClientInGame(int port, AuthData currUserAuth, int gameID) {
        this.ws = new WebSocketFacade(port);
        this.currentUserAuth = currUserAuth;

        UserGameCommand command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, currentUserAuth.authToken(), gameID);
        try {
            ws.send(command);
        } catch (Exception e) {
            System.out.println("You haven't handled this error yet");
        }
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public String eval(String input) {
        switch (input) {
            case ("2") -> redrawBoard();
            case ("3") -> leave();
            case ("4") -> makeMove();
            case ("5") -> resign();
            case ("6") -> highlightMoves();
            default -> help();
        }
        return "";
    }

    private void redrawBoard() {
        //TODO
    }

    private void leave() {
        this.state = State.LOGGEDIN;

        //TODO
    }

    private void makeMove() {
        //TODO
    }

    private void resign() {
        //TODO
    }

    private void highlightMoves() {
        //TODO
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
        return 0;
    }
}
