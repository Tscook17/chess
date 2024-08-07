package ui.consolerepls;

import chess.ChessGame;
import websocket.messages.LoadGameMessage;

public interface GameHandler {
    void updateGame(LoadGameMessage message);
    void printMessage(String message, boolean redText);
}
