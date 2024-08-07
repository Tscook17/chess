package ui;

import com.google.gson.Gson;
import servicepackets.request.RegisterRequest;
import servicepackets.result.RegisterResult;
import ui.consolerepls.GameHandler;
import websocket.commands.ConnectCommand;
import websocket.commands.UserGameCommand;

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

    public void makeMove() {

    }

    public void leaveGame() {

    }

    public void resignGame() {

    }

    private <T extends UserGameCommand> void sendMessage(T command) throws Exception {
        // send message to server
        this.session.getBasicRemote().sendText(new Gson().toJson(command));
    }

    @Override
    public void onMessage(String s) {
        // deserialize message
        // call GameHandler to process message
    }
}
