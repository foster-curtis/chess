package server.websocketServer;

import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class GameConnectionManager {

    private final ConcurrentHashMap<Integer, ArrayList<Session>> sessionMap = new ConcurrentHashMap<>();

    public void addToGame(Integer gameID, Session session) {
        var players = sessionMap.get(gameID);
        if (players != null) {
            players.add(session);
        } else {
            sessionMap.put(gameID, new ArrayList<>());
            sessionMap.get(gameID).add(session);
        }
    }

    public void removeFromGame(Integer gameID, Session session) {
        sessionMap.get(gameID).remove(session);
    }

    public void removeSession(Session session) {
        for (var sessionSet : sessionMap.entrySet()) {
            // Removes a session from a game's list of player if it equals the session passed to the function
            sessionSet.getValue().removeIf(playerSession -> playerSession == session);
        }
    }

    public ArrayList<Session> getSessionsForGame(Integer gameID) {
        return sessionMap.get(gameID);
    }

}
