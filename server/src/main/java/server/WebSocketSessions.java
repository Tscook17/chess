package server;

import org.eclipse.jetty.websocket.api.*;

import java.util.HashMap;
import java.util.Set;

public class WebSocketSessions {
    private HashMap<Integer, Set<Session>> sessionMap = new HashMap<>();

    public void addSessionToGame(int gameID, Session session) {

    }

    public void removeSessionFromGame(int gameID, Session session) {

    }

    public void removeSession(Session session) {

    }

    public Set<Session> getSessionsForGame(int gameID) {
        return sessionMap.get(gameID);
    }
}
