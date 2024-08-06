package server;

import org.eclipse.jetty.websocket.api.*;

import java.util.HashMap;
import java.util.Set;

public class WebSocketSessions {
    private HashMap<Integer, Set<Session>> sessionMap = new HashMap<>();

    public void addSessionToGame(int gameID, Session session) {
        Set<Session> sessions = sessionMap.get(gameID);
        sessions.add(session);
        sessionMap.put(gameID, sessions);
    }

    public void removeSessionFromGame(int gameID, Session session) {
        Set<Session> sessions = sessionMap.get(gameID);
        sessions.remove(session);
    }

    public void removeSession(Session session) {
        for (HashMap.Entry<Integer, Set<Session>> pair : sessionMap.entrySet()) {
            pair.getValue().remove(session);
        }
    }

    public Set<Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }
}
