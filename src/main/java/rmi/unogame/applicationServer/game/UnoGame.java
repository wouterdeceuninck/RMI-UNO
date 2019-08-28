package rmi.unogame.applicationServer.game;

import rmi.unogame.applicationServer.game.dtoObjects.UnoGameInfo;
import rmi.unogame.applicationServer.game.player.inMemPlayers.SlowLocalInMemPlayer;
import rmi.unogame.applicationServer.game.score.EndScoreCalculator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnoGame {

    private final UnoGameplayLogic unoGameplayLogic;
    private List<ApplicationPlayer> playersList;
    private UnoGameInfo unoGameInfo;

    public UnoGame(UnoGameInfo unoGameInfo) {
        playersList = new ArrayList<>();
        unoGameplayLogic = new UnoGameplayLogic();
        this.unoGameInfo = unoGameInfo;
        addBots(unoGameInfo.getAmountOfBots());
    }

    private void addBots(int amountOfBots) {
        for (int i = 1; i < amountOfBots + 1; i++) {
            SlowLocalInMemPlayer slowLocalInMemPlayer = new SlowLocalInMemPlayer("bot " + i);
            addPlayer(new ApplicationPlayer(slowLocalInMemPlayer, "bot " + i));
        }
    }

    public void addPlayer(ApplicationPlayer player) {
        playersList.add(player);
    }

    public void startGame() {
        unoGameplayLogic.start(playersList);
    }

    public UnoGameInfo getUnoGameInfo() {
        this.unoGameInfo.setJoinedPlayers(playersList.size());
        return unoGameInfo;
    }

    public int getJoinedPlayers() {
        return playersList.size();
    }

    public List<ApplicationPlayer> getPlayersList() {
        return playersList;
    }

    public Map<String, Integer> getEndScore() {
        return EndScoreCalculator.calculateScore(playersList);
    }
}
