package server.websocketServer;

import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameUsernamePackage;
import model.MakeMoveResponse;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import org.eclipse.jetty.websocket.api.Session;
import service.WebSocketService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

@WebSocket
public class WebSocketHandler {
    private final GameConnectionManager connectionManager;
    private final WebSocketService service;
    private final HashMap<Integer, Boolean> gameActive = new HashMap<>();

    public WebSocketHandler(AuthDAO auth, GameDAO game, UserDAO user) {
        this.service = new WebSocketService(game, user, auth);
        this.connectionManager = new GameConnectionManager();
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws DataAccessException, IOException, InvalidMoveException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        MakeMoveCommand mmCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> makeMove(mmCommand, session);
            case LEAVE -> leave(command, session);
            case RESIGN -> resign(command, session);
        }
    }

    private void connect(UserGameCommand command, Session session) throws DataAccessException, IOException {
        GameUsernamePackage pack = service.connect(command);

        if (pack.gameData() == null) {
            sendErrorMessage("Invalid game ID.", session);
        } else {
            connectionManager.addToGame(command.getGameID(), session);
            gameActive.putIfAbsent(command.getGameID(), true);

            var message = new LoadGameMessage(pack.gameData());
            sendMessage(new Gson().toJson(message), session);

            SendNotificationBroadcast(pack.username() + " has joined the game.", command.getGameID(), session);
        }
    }

    private void makeMove(MakeMoveCommand command, Session session) throws DataAccessException, InvalidMoveException, IOException {
        if (!gameActive.get(command.getGameID())) {
            sendErrorMessage("The game is over, no more moves can be made.", session);
        } else {

            MakeMoveResponse res = service.makeMove(command);

            broadcast(command.getGameID(), new Gson().toJson(new LoadGameMessage(res.gameData())), null);
            SendNotificationBroadcast(res.username() + " has made a move.", command.getGameID(), session);

            if (res.inCheckmate()) {
                SendNotificationBroadcast(res.username() + " is in Checkmate!", command.getGameID(), null);
                gameActive.put(command.getGameID(), false);
            } else if (res.inStalemate()) {
                SendNotificationBroadcast(res.username() + " is in Stalemate!", command.getGameID(), null);
                gameActive.put(command.getGameID(), false);
            } else if (res.inCheck()) {
                SendNotificationBroadcast(res.username() + " is in Check!", command.getGameID(), null);
            }
        }
    }

    private void leave(UserGameCommand command, Session session) throws DataAccessException, IOException {
        Integer gameID = command.getGameID();
        connectionManager.removeFromGame(gameID, session);

        // If there are no more players in the game and a game has ended, delete the game
        boolean gameExpired = connectionManager.getSessionsForGame(gameID).isEmpty() && !gameActive.get(gameID);

        var username = service.leave(command, gameExpired);

        SendNotificationBroadcast(username + " has left the game.", command.getGameID(), session);
    }

    private void resign(UserGameCommand command, Session session) throws DataAccessException, IOException {
        var username = service.resign(command);
        if (!gameActive.get(command.getGameID())) {
            sendErrorMessage("This game is over, you cannot perform this action.", session);
        } else {
            gameActive.put(command.getGameID(), false);
            SendNotificationBroadcast(username + "has resigned from the game.", command.getGameID(), session);
            var message = new NotificationMessage("You have resigned from the game.");
            sendMessage(new Gson().toJson(message), session);
        }
    }

    private void SendNotificationBroadcast(String message, Integer gameID, Session session) throws IOException {
        var broadcast = new NotificationMessage(message);
        broadcast(gameID, new Gson().toJson(broadcast), session);
    }

    private void sendErrorMessage(String message, Session session) throws IOException {
        sendMessage(new Gson().toJson(new ErrorMessage(message)), session);
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
