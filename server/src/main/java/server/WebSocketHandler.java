package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.sqldao.AuthDAO;
import dataaccess.sqldao.GameDAO;
import model.GameData;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.*;
import websocket.messages.*;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

@WebSocket
public class WebSocketHandler {
    private static WebSocketSessions sessions = new WebSocketSessions();

    private final Map<Integer, Character> coordinateMap = Map.of(
            1, 'a',
            2, 'b',
            3, 'c',
            4, 'd',
            5, 'e',
            6, 'f',
            7, 'g',
            8, 'h'
    );

    @OnWebSocketError
    public void onError(Throwable throwable) {
        System.out.print(throwable.getMessage());
    }

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

    private void verifyCommand(UserGameCommand command, ChessMove move) throws Exception {
        if (command.getAuthToken() != null &&
            command.getGameID() != null &&
            command.getGameID() != 0) {
            new AuthDAO().getAuth(command.getAuthToken());
            if (command.getCommandType() == UserGameCommand.CommandType.MAKE_MOVE &&
                move == null) {
                throw new Exception("Error: invalid request");
            }
        } else {
            throw new Exception("Error: invalid request");
        }
    }

    private void connect(ConnectCommand message, Session session) throws Exception {
        Gson g = new Gson();
        try {
            verifyCommand(message, null);
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

    // fixme and others below
    private void makeMove(MakeMoveCommand message, Session session) throws Exception {
        Gson g = new Gson();
        try {
            verifyCommand(message, message.getMove());
            if (new GameDAO().getGame(message.getGameID()).whiteUsername() == null ||
                new GameDAO().getGame(message.getGameID()).whiteUsername() == null) {
                throw new Exception("Error: waiting for opponent to join game");
            }
            checkIfObserver(message);
            if (new GameDAO().getGame(message.getGameID()).game().isGameOver()) {
                throw new Exception("Error: Game is over, can't make move");
            }
            // check valid move
            if (!isValidMove(message)) {
                throw new Exception("Error: invalid move");
            }
            checkYourTurn(message);
            checkIfYourPiece(message);
            // update game in database
            ChessGame game = new GameDAO().getGame(message.getGameID()).game();
            game.makeMove(message.getMove());
            new GameDAO().updateGame(message.getGameID(), game);
            // send load game to all clients in game w/ updated game
            LoadGameMessage loadGame = new LoadGameMessage(g.toJson(game));
            sessions.broadcastMessage(message.getGameID(), g.toJson(loadGame), null);
            // notify all other clients that move made not mover
            String move =
                    "" + coordinateMap.get(message.getMove().getEndPosition().getColumn()) + message.getMove().getEndPosition().getRow();
            NotificationMessage notification =
                    new NotificationMessage(new AuthDAO().getAuth(message.getAuthToken()).username() + " moved to " + move);
            sessions.broadcastMessage(message.getGameID(), g.toJson(notification), session);
            // if check, checkmate, or stalemate notify all clients
            notifyGameState(new GameDAO().getGame(message.getGameID()), message, session);
        } catch(Exception e) {
            ErrorMessage error = new ErrorMessage(e.getMessage());
            sessions.sendMessage(g.toJson(error), session);
        }
    }

    private void checkIfObserver(UserGameCommand command) throws Exception {
        String username = new AuthDAO().getAuth(command.getAuthToken()).username();
        GameData gameData = new GameDAO().getGame(command.getGameID());
        if (!Objects.equals(username, gameData.whiteUsername()) && !Objects.equals(username, gameData.blackUsername())) {
            throw new Exception("Error: observer cannot affect game");
        }
    }

    private void checkIfYourPiece(MakeMoveCommand command) throws Exception {
        String username = new AuthDAO().getAuth(command.getAuthToken()).username();
        GameData gameData = new GameDAO().getGame(command.getGameID());
        ChessPosition pos = command.getMove().getStartPosition();
        ChessGame.TeamColor color = gameData.game().getBoard().getPiece(pos).getTeamColor();
        if (username.equals(gameData.whiteUsername()) && color == ChessGame.TeamColor.BLACK) {
            throw new Exception("Error: you can only move your colored pieces");
        } else if (username.equals(gameData.blackUsername()) && color == ChessGame.TeamColor.WHITE) {
            throw new Exception("Error: you can only move your colored pieces");
        }
    }

    private void checkYourTurn(UserGameCommand command) throws Exception {
        String username = new AuthDAO().getAuth(command.getAuthToken()).username();
        GameData gameData = new GameDAO().getGame(command.getGameID());
        if ((gameData.game().getTeamTurn() == ChessGame.TeamColor.WHITE &&
            gameData.blackUsername().equals(username)) ||
            (gameData.game().getTeamTurn() == ChessGame.TeamColor.BLACK &&
            gameData.whiteUsername().equals(username))) {
            throw new Exception("Error: not your turn");
        }
    }

    private void notifyGameState(GameData gameData, MakeMoveCommand message, Session session) throws Exception {
        Gson g = new Gson();
        ChessGame game = gameData.game();
        String username = game.getTeamTurn() == ChessGame.TeamColor.WHITE ? gameData.whiteUsername() : gameData.blackUsername();
        String statement = null;
        if (gameData.game().isInCheck(game.getTeamTurn())) {
            statement = username + " is in check!";
        } else if (gameData.game().isInCheckmate(game.getTeamTurn())) {
            statement = username + " is in checkmate!";
            game.setGameOver(true);
            new GameDAO().updateGame(message.getGameID(), game);
        } else if (gameData.game().isInStalemate(game.getTeamTurn())) {
            statement = username + " is in stalemate!";
            game.setGameOver(true);
            new GameDAO().updateGame(message.getGameID(), game);
        }
        if (statement != null) {
            NotificationMessage notification = new NotificationMessage(statement);
            sessions.broadcastMessage(message.getGameID(), g.toJson(notification), null);
        }
    }

    private boolean isValidMove(MakeMoveCommand message) throws Exception {
        ChessGame game = new GameDAO().getGame(message.getGameID()).game();
        Collection<ChessMove> validMoves = game.validMoves(message.getMove().getStartPosition());
        if (validMoves == null) {
            throw new Exception("Error: no piece selected");
        }
        return validMoves.contains(message.getMove());
    }

    private void leaveGame(LeaveCommand message, Session session) throws Exception {
        Gson g = new Gson();
        try {
            verifyCommand(message, null);
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
            verifyCommand(message, null);
            checkIfObserver(message);
            // check if game already over
            if (new GameDAO().getGame(message.getGameID()).game().isGameOver()) {
                throw new Exception("Error: Game is over, can't make move");
            }
            // mark game as over, update database
            ChessGame game = new GameDAO().getGame(message.getGameID()).game();
            game.setGameOver(true);
            new GameDAO().updateGame(message.getGameID(), game);
            // notify all clients that he resigned
            NotificationMessage notification =
                    new NotificationMessage(new AuthDAO().getAuth(message.getAuthToken()).username() + " has resigned");
            sessions.broadcastMessage(message.getGameID(), g.toJson(notification), null);
        } catch(Exception e) {
            ErrorMessage error = new ErrorMessage(e.getMessage());
            sessions.sendMessage(g.toJson(error), session);
        }
    }
}
