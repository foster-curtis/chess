package ui.websocketmanager;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import ui.BoardUI;
import ui.ClientInGame;
import ui.ServerMessageObserver;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static ui.EscapeSequences.*;

public class WebSocketFacade extends Endpoint {

    Session session;
    private final ServerMessageObserver serverMessageObserver;
    private final ClientInGame client;

    public WebSocketFacade(int port, ServerMessageObserver serverMessageObserver, ClientInGame client) {
        try {
            this.serverMessageObserver = serverMessageObserver;
            this.client = client;

            URI socketURI = new URI("ws://localhost:" + port + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {

                @Override
                public void onMessage(String msg) {
                    ServerMessage message = new Gson().fromJson(msg, ServerMessage.class);
                    var type = message.getServerMessageType();

                    switch (type) {
                        case ServerMessage.ServerMessageType.LOAD_GAME -> {
                            LoadGameMessage loadGameMessage = new Gson().fromJson(msg, LoadGameMessage.class);
                            loadGame(loadGameMessage);
                        }
                        case ServerMessage.ServerMessageType.NOTIFICATION -> {
                            NotificationMessage notificationMessage = new Gson().fromJson(msg, NotificationMessage.class);
                            notification(notificationMessage);
                        }
                        case ServerMessage.ServerMessageType.ERROR -> {
                            ErrorMessage errorMessage = new Gson().fromJson(msg, ErrorMessage.class);
                            error(errorMessage);
                        }
                    }
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new ResponseException(ex.getMessage(), 500);
        }
    }

    private void loadGame(LoadGameMessage s) {
        ChessGame game = s.getGame().game();

        //Store the game info in the client for when we call redrawBoard
        client.setChessGame(game);

        serverMessageObserver.notify("\n" + new BoardUI(game.getBoard(), client.getPlayerColor()).displayBoard(null, null));
    }

    private void notification(NotificationMessage s) {
        System.out.println(SET_TEXT_COLOR_GREEN + s.getMessage() + SET_TEXT_COLOR_WHITE);
    }

    private void error(ErrorMessage s) {
        System.out.println(SET_TEXT_COLOR_RED + s.getErrorMessage() + SET_TEXT_COLOR_WHITE);
    }

    public void send(UserGameCommand command) throws IOException {
        var msg = new Gson().toJson(command);
        this.session.getBasicRemote().sendText(msg);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
