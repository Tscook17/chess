package ui;

import chess.ChessMove;
import com.google.gson.Gson;
import servicepackets.request.RegisterRequest;
import servicepackets.result.RegisterResult;
import ui.consolerepls.GameHandler;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.HttpURLConnection;
import java.net.URI;

import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {
    private Session session;
    GameHandler gameHandler;

    public WebSocketFacade(String url, GameHandler handler) throws Exception {
        gameHandler = handler;
        url = url.replace("http", "ws");
        URI uri = new URI(url + "/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);
        this.session.addMessageHandler(this);
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {}

    public void connect(String authToken, int gameID) throws Exception {
        ConnectCommand command = new ConnectCommand(authToken, gameID);
        sendMessage(command);
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws Exception {
        MakeMoveCommand command = new MakeMoveCommand(authToken, gameID, move);
        sendMessage(command);
    }

    public void leaveGame(String authToken, int gameID) throws Exception {
        LeaveCommand command = new LeaveCommand(authToken, gameID);
        sendMessage(command);
    }

    public void resignGame(String authToken, int gameID) throws Exception {
        ResignCommand command = new ResignCommand(authToken, gameID);
        sendMessage(command);
    }

    private <T extends UserGameCommand> void sendMessage(T command) throws Exception {
        // send message to server
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    @Override
    public void onMessage(String s) {
        // deserialize message
        Gson g = new Gson();
        ServerMessage message = g.fromJson(s, ServerMessage.class);
        // call GameHandler to process message
        switch (message.getServerMessageType()) {
            case LOAD_GAME -> gameHandler.updateGame(g.fromJson(s, LoadGameMessage.class));
            case ERROR -> gameHandler.printMessage(g.fromJson(s, ErrorMessage.class).getErrorMessage(), true);
            case NOTIFICATION -> gameHandler.printMessage(g.fromJson(s, NotificationMessage.class).getMessage(), false);
        }
    }
}
