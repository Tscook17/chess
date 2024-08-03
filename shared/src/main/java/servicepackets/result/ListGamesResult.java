package servicepackets.result;

import model.GameData;

public class ListGamesResult extends ResultBase {
    private GameData[] games;

    public ListGamesResult() {}

    public ListGamesResult(String message, int errorCode) {
        super(message, errorCode);
    }

    public ListGamesResult(GameData[] games) {
        setGames(games);
    }

    @Override
    void callError() {
        setGames(null);
    }

    public GameData[] getGames() {
        return games;
    }

    public void setGames(GameData[] games) {
        this.games = games;
    }
}
