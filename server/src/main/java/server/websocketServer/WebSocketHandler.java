package server.websocketServer;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameAuthPackage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import service.WebSocketService;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

import static websocket.messages.ServerMessage.ServerMessageType.*;

@WebSocket
public class WebSocketHandler {
    /**
     * A list of connection managers. A new one should be created
     * for each game. The connection manager should take in a gameID
     * when it is created, and new connections with each user will
     * be initiated through the connection manager.
     */
    private final GameConnectionManager connectionManager;
    private final WebSocketService service;

    public WebSocketHandler(AuthDAO auth, GameDAO game, UserDAO user) {
        this.service = new WebSocketService(game, user, auth);
        this.connectionManager = new GameConnectionManager();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> makeMove(command, session);
            case LEAVE -> leave(command, session);
            case RESIGN -> resign(command, session);
        }
    }

    // SERIALIZE YOUR MESSAGES BEFORE YOU PASS THEM TO THE SEND METHOD!!

    private void connect(UserGameCommand command, Session session) throws DataAccessException, IOException {
        GameAuthPackage pack = service.connect(command);
        connectionManager.addToGame(command.getGameID(), session);

        // Send a load game message to the client
        var message = new LoadGameMessage(LOAD_GAME, pack.gameData());
        sendMessage(new Gson().toJson(message), session);

        // Broadcast a message to all other clients that the user joined the game
        var broadcast = new NotificationMessage(NOTIFICATION, pack.authData().username() + " has joined the game.");
        broadcast(command.getGameID(), new Gson().toJson(broadcast), session);
    }

    private void makeMove(UserGameCommand command, Session session) {

    }

    private void leave(UserGameCommand command, Session session) {

    }

    private void resign(UserGameCommand command, Session session) {

    }

    private void sendMessage(String message, Session session) throws IOException {
        session.getRemote().sendString(message);
    }

    private void broadcast(Integer gameID, String message, Session excludedSession) throws IOException {
        var sessions = connectionManager.getSessionsForGame(gameID);
        for (Session session : sessions) {
            if (session != excludedSession) {
                session.getRemote().sendString(message);
            }
        }
    }
}
