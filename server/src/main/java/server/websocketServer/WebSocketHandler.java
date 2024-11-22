package server.websocketServer;

import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameUsernamePackage;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import service.WebSocketService;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
    private final HashMap<Integer, Boolean> gameActive = new HashMap<>();

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
        GameUsernamePackage pack = service.connect(command);
        connectionManager.addToGame(command.getGameID(), session);
        gameActive.putIfAbsent(command.getGameID(), true);

        // Send a load game message to the client
        var message = new LoadGameMessage(pack.gameData());
        sendMessage(new Gson().toJson(message), session);

        // Broadcast a message to all other clients that the user joined the game
        var broadcast = new NotificationMessage(pack.username() + " has joined the game.");
        broadcast(command.getGameID(), new Gson().toJson(broadcast), session);
    }

    private void makeMove(UserGameCommand command, Session session) {

    }

    private void leave(UserGameCommand command, Session session) throws DataAccessException, IOException {
        Integer gameID = command.getGameID();
        connectionManager.removeFromGame(gameID, session);

        // If there are no more players in the game and a game has ended, delete the game
        boolean gameExpired = connectionManager.getSessionsForGame(gameID).isEmpty() && !gameActive.get(gameID);

        var username = service.leave(command, gameExpired);

        var broadcast = new NotificationMessage(username + " has left the game.");
        broadcast(gameID, new Gson().toJson(broadcast), session);
    }

    private void resign(UserGameCommand command, Session session) throws DataAccessException, IOException {
        var username = service.resign(command);
        if (!gameActive.get(command.getGameID())) {
            var message = new ErrorMessage("This game is over, you cannot perform this action.");
            sendMessage(new Gson().toJson(message), session);
        } else {
            gameActive.put(command.getGameID(), false);
            var broadcast = new NotificationMessage(username + "has resigned from the game.");
            broadcast(command.getGameID(), new Gson().toJson(broadcast), session);
            var message = new NotificationMessage("You have resigned from the game.");
            sendMessage(new Gson().toJson(message), session);
        }
    }

    private void sendMessage(String message, Session session) throws IOException {
        session.getRemote().sendString(message);
    }

    private void broadcast(Integer gameID, String message, Session excludedSession) throws IOException {
        var removeList = new ArrayList<Session>();
        var sessions = connectionManager.getSessionsForGame(gameID);
        for (Session session : sessions) {
            if (session.isOpen()) {
                if (session != excludedSession) {
                    sendMessage(message, session);
                }
            } else {
                removeList.add(session);
            }
        }
        for (var r : removeList) {
            connectionManager.removeSession(r);
        }
    }
}
