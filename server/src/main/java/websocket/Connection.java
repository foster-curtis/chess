package websocket;

import javax.websocket.Session;
import java.io.IOException;

public class Connection {
    private String username;
    private Session session;

    public Connection(String username, Session session) {
        this.username = username;
        this.session = session;
    }

    public void sendMessage(String msg) throws IOException {
        session.getBasicRemote().sendText(msg);
    }
}
