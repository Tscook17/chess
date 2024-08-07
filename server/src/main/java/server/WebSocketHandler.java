package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.sqldao.AuthDAO;
import dataaccess.sqldao.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.*;

import java.util.Collection;

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
            case MAKE_MOVE -> makeMove(g.fromJson(str, MakeMoveCommand.class), session);
            case LEAVE -> leaveGame(g.fromJson(str, LeaveCommand.class), session);
            case RESIGN -> resignGame(g.fromJson(str, ResignCommand.class), session);
        }
    }

    private void connect(ConnectCommand message, Session session) throws Exception {
        Gson g = new Gson();
        try {
            // add connection
            sessions.addSessionToGame(message.getGameID(), session);
            // send load game message to client
            NotificationMessage notification = new NotificationMessage(joinGameString(message));
            LoadGameMessage loadGame = new LoadGameMessage(g.toJson(new GameDAO().getGame(message.getGameID())));
            sessions.sendMessage(g.toJson(loadGame), session);
            // notification to all other people in game
            sessions.broadcastMessage(message.getGameID(), g.toJson(notification), session);
        } catch(Exception e) {
            ErrorMessage error = new ErrorMessage(e.getMessage());
            sessions.sendMessage(g.toJson(error), session);
        }
    }

    private String joinGameString(ConnectCommand message) throws Exception {
        String username = new AuthDAO().getAuth(message.getAuthToken()).username();
        if (username.equals(new GameDAO().getGame(message.getGameID()).whiteUsername())) {
            return username + " has joined the game as White";
        } else if (username.equals(new GameDAO().getGame(message.getGameID()).blackUsername())) {
            return username + " has joined the game as Black";
        } else {
            return username + " has joined the game as an observer";
        }
    }

    // todo fixme
    private void makeMove(MakeMoveCommand message, Session session) throws Exception {
        Gson g = new Gson();
        try {
            // check valid move
            if (!isValidMove(message)) {
                throw new Exception("Error: invalid move");
            }
            if (new GameDAO().getGame(message.getGameID()).game().isGameOver()) {
                throw new Exception("Game is over, can't make move");
            }
            // update game in database
            ChessGame game = new GameDAO().getGame(message.getGameID()).game();
            game.makeMove(message.getMove());
            new GameDAO().updateGame(message.getGameID(), game);
            // send load game to all clients in game w/ updated game
            LoadGameMessage loadGame = new LoadGameMessage(g.toJson(new GameDAO().getGame(message.getGameID())));
            sessions.broadcastMessage(message.getGameID(), g.toJson(loadGame), null);
            // notify all other clients that move made not mover
            NotificationMessage notification =
                    new NotificationMessage(new AuthDAO().getAuth(message.getAuthToken()).username() + " has moved");
            sessions.broadcastMessage(message.getGameID(), g.toJson(notification), session);
            // if check, checkmate, or stalemate notify all clients
            notifyGameState(game, message, session);
        } catch(Exception e) {
            ErrorMessage error = new ErrorMessage(e.getMessage());
            sessions.sendMessage(g.toJson(error), session);
        }
    }

    private void notifyGameState(ChessGame game, MakeMoveCommand message, Session session) throws Exception {
        Gson g = new Gson();
        String statement = null;
        if (game.isInCheck(game.getTeamTurn())) {
            statement = "In check!";
        } else if (game.isInCheckmate(game.getTeamTurn())) {
            statement = "In checkmate!";
        } else if (game.isInStalemate(game.getTeamTurn())) {
            statement = "In stalemate!";
        }
        if (statement != null) {
            NotificationMessage notification = new NotificationMessage(statement);
            sessions.broadcastMessage(message.getGameID(), g.toJson(notification), session);
        }
    }

    private boolean isValidMove(MakeMoveCommand message) throws Exception {
        ChessGame game = new GameDAO().getGame(message.getGameID()).game();
        Collection<ChessMove> validMoves = game.validMoves(message.getMove().getStartPosition());
        return validMoves.contains(message.getMove());
    }

    private void leaveGame(LeaveCommand message, Session session) throws Exception {
        Gson g = new Gson();
        try {
            // if player leaving, remove player from game, update in database
            String username = new AuthDAO().getAuth(message.getAuthToken()).username();
            GameData gameData = new GameDAO().getGame(message.getGameID());
            if (username.equals(gameData.whiteUsername())) {
                new GameDAO().unjoinGame(message.getGameID(), "WHITE");
                sessions.removeSessionFromGame(message.getGameID(), session);
            } else if (username.equals(gameData.blackUsername())) {
                new GameDAO().unjoinGame(message.getGameID(), "BLACK");
                sessions.removeSessionFromGame(message.getGameID(), session);
            }
            // notify all other clients that players left
            NotificationMessage notification =
                    new NotificationMessage(new AuthDAO().getAuth(message.getAuthToken()).username() + " has left the game");
            sessions.broadcastMessage(message.getGameID(), g.toJson(notification), session);
        } catch(Exception e) {
            ErrorMessage error = new ErrorMessage(e.getMessage());
            sessions.sendMessage(g.toJson(error), session);
        }
    }

    private void resignGame(ResignCommand message, Session session) throws Exception {
        Gson g = new Gson();
        try {
            // mark game as over, update database
            ChessGame game = new GameDAO().getGame(message.getGameID()).game();
            game.setGameOver(true);
            new GameDAO().updateGame(message.getGameID(), game);
            // notify all clients that he resigned
            NotificationMessage notification =
                    new NotificationMessage(new AuthDAO().getAuth(message.getAuthToken()).username() + " has resigned");
            sessions.broadcastMessage(message.getGameID(), g.toJson(notification), session);
        } catch(Exception e) {
            ErrorMessage error = new ErrorMessage(e.getMessage());
            sessions.sendMessage(g.toJson(error), session);
        }
    }
}
