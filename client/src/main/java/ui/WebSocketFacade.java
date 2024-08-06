package ui;

import ui.consolerepls.GameHandler;

import javax.websocket.*;

public class WebSocketFacade extends Endpoint implements MessageHandler.Whole<String> {
    private Session session;
    GameHandler gameHandler;


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    @Override
    public void onClose(Session session, CloseReason closeReason) {
    }

    @Override
    public void onError(Session session, Throwable thr) {
    }

    public void connect() {

    }

    public void makeMove() {

    }

    public void leaveGame() {

    }

    public void resignGame() {

    }

    private void sendMessage() {
        // create command message
        // send message to server
    }

    @Override
    public void onMessage(String s) {
        // deserialize message
        // call GameHandler to process message
    }
}
