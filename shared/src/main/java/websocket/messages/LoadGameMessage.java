package websocket.messages;

public class LoadGameMessage extends ServerMessage {
    private String game;

    public LoadGameMessage(String game) {
        super(ServerMessageType.LOAD_GAME);
        setGame(game);
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }
}
