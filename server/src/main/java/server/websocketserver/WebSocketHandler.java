package server.websocketserver;

import chess.ChessGame;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.GameData;
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
import java.util.Objects;

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
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        MakeMoveCommand mmCommand = new Gson().fromJson(message, MakeMoveCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> makeMove(mmCommand, session);
            case LEAVE -> leave(command, session);
            case RESIGN -> resign(command, session);
        }
    }

    private void connect(UserGameCommand command, Session session) throws IOException {
        try {
            GameUsernamePackage pack = service.connect(command);
            if (pack.gameData() == null) {
                sendErrorMessage("Invalid game ID.", session);
            } else {
                connectionManager.addToGame(command.getGameID(), session);
                gameActive.putIfAbsent(command.getGameID(), true);

                var color = getColor(pack.gameData(), pack.username());

                var message = new LoadGameMessage(pack.gameData(), color, null);
                sendMessage(new Gson().toJson(message), session);

                String notification = pack.username() + " joined the game as " + color.toString().toLowerCase() + ".";
                sendNotificationBroadcast(notification, command.getGameID(), session);
            }
        } catch (DataAccessException e) {
            sendErrorMessage(e.getMessage(), session);
        }
    }

    private void makeMove(MakeMoveCommand command, Session session) throws IOException {
        try {
            if (!gameActive.get(command.getGameID())) {
                sendErrorMessage("The game is over, no more moves can be made.", session);
            } else {

                MakeMoveResponse res = service.makeMove(command);

                var color = getColor(res.gameData(), res.username());

                broadcast(command.getGameID(), new Gson().toJson(new LoadGameMessage(res.gameData(), color, command.getMove())), null);
                sendNotificationBroadcast(res.username() + " made a move: " + command.getStringMove(), command.getGameID(), session);

                if (res.inCheckmate()) {
                    String message = res.opponentUsername() + " is in Checkmate! " + res.username() + " wins!";
                    sendNotificationBroadcast(message, command.getGameID(), null);
                    gameActive.put(command.getGameID(), false);
                } else if (res.inStalemate()) {
                    sendNotificationBroadcast(res.opponentUsername() + " is in Stalemate! There is no winner.", command.getGameID(), null);
                    gameActive.put(command.getGameID(), false);
                } else if (res.inCheck()) {
                    sendNotificationBroadcast(res.opponentUsername() + " is in Check!", command.getGameID(), null);
                }
            }
        } catch (DataAccessException | InvalidMoveException e) {
            sendErrorMessage(e.getMessage(), session);
        }
    }

    private void leave(UserGameCommand command, Session session) throws IOException {
        Integer gameID = command.getGameID();
        connectionManager.removeFromGame(gameID, session);

        // If there are no more players in the game and a game has ended, delete the game
        boolean gameExpired = connectionManager.getSessionsForGame(gameID).isEmpty() && !gameActive.get(gameID);

        try {
            var username = service.leave(command, gameExpired);

            sendNotificationBroadcast(username + " has left the game.", command.getGameID(), session);
        } catch (DataAccessException e) {
            sendErrorMessage(e.getMessage(), session);
        }
    }

    private void resign(UserGameCommand command, Session session) throws IOException {
        try {
            var username = service.resign(command);
            if (!gameActive.get(command.getGameID())) {
                sendErrorMessage("This game is over, you cannot perform this action.", session);
            } else {
                gameActive.put(command.getGameID(), false);
                sendNotificationBroadcast(username + " has resigned from the game.", command.getGameID(), session);
                var message = new NotificationMessage("You have resigned from the game.");
                sendMessage(new Gson().toJson(message), session);
            }
        } catch (DataAccessException e) {
            sendErrorMessage(e.getMessage(), session);
        }
    }

    private void sendNotificationBroadcast(String message, Integer gameID, Session session) throws IOException {
        var broadcast = new NotificationMessage(message);
        broadcast(gameID, new Gson().toJson(broadcast), session);
    }

    private void sendErrorMessage(String message, Session session) throws IOException {
        sendMessage(new Gson().toJson(new ErrorMessage(message)), session);
    }

    private ChessGame.TeamColor getColor(GameData game, String username) {
        ChessGame.TeamColor color = null;
        if (Objects.equals(game.whiteUsername(), username)) {
            color = ChessGame.TeamColor.WHITE;
        } else if (Objects.equals(game.blackUsername(), username)) {
            color = ChessGame.TeamColor.BLACK;
        }
        return color;
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

    public void clear() {
        gameActive.clear();
    }
}
