package server;

import com.google.gson.Gson;
import dataaccess.sqldao.AuthDAO;
import dataaccess.sqldao.GameDAO;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.*;

@WebSocket
public class WebSocketHandler {
    private static WebSocketSessions sessions = new WebSocketSessions();

//    @OnWebSocketConnect
//    public void onConnection(Session session) {
//
//    }
//
//    @OnWebSocketClose
//    public void onClose(Session session) {
//
//    }
//
//    @OnWebSocketError
//    public void onError(Throwable throwable) {
//
//    }

    @OnWebSocketMessage
    public void onMessage(Session session, String str) throws Exception {
        // determine message type
        Gson g = new Gson();
        UserGameCommand command = g.fromJson(str, UserGameCommand.class);

        // call one of the following methods to process the message
        switch (command.getCommandType()) {
            case CONNECT -> connect(g.fromJson(str, ConnectCommand.class), session);
            case MAKE_MOVE -> makeMove(g.fromJson(str, MakeMoveCommand.class));
            case LEAVE -> leaveGame(g.fromJson(str, LeaveCommand.class));
            case RESIGN -> resignGame(g.fromJson(str, ResignCommand.class));
        }
    }

    private void connect(ConnectCommand message, Session session) throws Exception {
        Gson g = new Gson();
        try {
            // add connection
            sessions.addSessionToGame(message.getGameID(), session);
            // send load game message to client
            NotificationMessage notification = new NotificationMessage(checkPlayingGame(message));
            LoadGameMessage loadGame = new LoadGameMessage(g.toJson(new GameDAO().getGame(message.getGameID())));
            sessions.sendMessage(g.toJson(loadGame), session);
            // notification to all other people in game
            sessions.broadcastMessage(message.getGameID(), g.toJson(notification), session);
        } catch(Exception e) {
            ErrorMessage error = new ErrorMessage(e.getMessage());
            sessions.sendMessage(g.toJson(error), session);
        }
    }

    private String checkPlayingGame(ConnectCommand message) throws Exception {
        String username = new AuthDAO().getAuth(message.getAuthToken()).username();
        if (username.equals(new GameDAO().getGame(message.getGameID()).whiteUsername())) {
            return username + " has joined the game as White";
        } else if (username.equals(new GameDAO().getGame(message.getGameID()).blackUsername())) {
            return username + " has joined the game as Black";
        } else {
            return username + " has joined the game as an observer";
        }
    }

    private void makeMove(MakeMoveCommand message) {

    }

    private void leaveGame(LeaveCommand message) {

    }

    private void resignGame(ResignCommand message) {

    }
}
