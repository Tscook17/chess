package server;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;

import static service.WebSocketService.*;

public class WebSocketHandler {
    private WebSocketSessions sessions;

    @OnWebSocketConnect
    public void onConnection(Session session) {

    }

    @OnWebSocketClose
    public void onClose(Session session) {

    }

    @OnWebSocketError
    public void onError(Throwable throwable) {

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String str) {
        // determine message type
        String message = "";
        // call one of the following methods to process the message
        connect(message); // call service and/or send messages to clients
        makeMove(message); // service.method()
        leaveGame(message); // sendMessage()
        resignGame(message); // broadcastMessage()
    }

    private void sendMessage(String message, Session session) {

    }

    private void broadcastMessage(int gameID, String message, Session exceptThisSession) {

    }
}
