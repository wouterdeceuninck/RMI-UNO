package rmi.unogame.client.gameplay.player;

import rmi.unogame.applicationServer.game.cards.UnoCard;
import rmi.unogame.applicationServer.game.player.Player;
import rmi.unogame.client.gameplay.GameplayService;
import rmi.unogame.client.gameplay.PlayerInfo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RemotePlayer implements Player {
    private final GameplayService gameplayService;
    private final String username;

    public RemotePlayer(GameplayService gameplayService, String username) {
        this.username = username;
        this.gameplayService = gameplayService;
    }

    @Override
    public Optional<UnoCard> requestCard() {
        return gameplayService.requestCard();
    }

    @Override
    public void updateTopCard(UnoCard topCard) {
        gameplayService.updateTopCard(topCard);
    }

    @Override
    public void addCards(List<UnoCard> cards) {
        gameplayService.addCards(cards);
    }

    @Override
    public List<UnoCard> getCards() {
        return gameplayService.getCards();
    }

    @Override
    public void notifyWinner(String winner) {
        gameplayService.notifyWinner(winner);
    }

    @Override
    public void updatePlayerCards(String playerName, int amount) {
        gameplayService.updatePlayerInfo(playerName, amount);
    }

    @Override
    public void startGame(List<PlayerInfo> playerInfoList) {
        gameplayService.setPlayerInfo(playerInfoList);
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void updateScore(Map<String, Integer> scoreboard) {
        gameplayService.updateScore(scoreboard);
    }

    @Override
    public void startNewRound() {
        gameplayService.clearCards();
    }
}
