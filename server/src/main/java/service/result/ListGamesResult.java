package service.result;

import model.GameData;
import service.request.ListGamesRequest;

public class ListGamesResult {
    private GameData[] games;

    public ListGamesResult() {}

    public ListGamesResult(GameData[] games) {
        setGames(games);
    }

    public GameData[] getGames() {
        return games;
    }

    public void setGames(GameData[] games) {
        this.games = games;
    }
}
