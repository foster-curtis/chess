package websocket;

import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;

@WebSocket
public class WebSocketHandler {
    /**
     * A list of connection managers. A new one should be created
     * for each game. The connection manager should take in a gameID
     * when it is created, and new connections with each user will
     * be initiated through the connection manager.
     */
    private ArrayList<GameConnectionManager> connectionManagers;

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        session.getRemote().sendString(message);
    }
}
