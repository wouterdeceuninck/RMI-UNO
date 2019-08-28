package rmi.unogame.applicationServer.game;

import rmi.unogame.applicationServer.game.cards.UnoCard;
import rmi.unogame.applicationServer.game.player.Player;
import rmi.unogame.client.gameplay.PlayerInfo;
import rmi.unogame.exceptions.InvalidCardPlayedExcetpion;

import java.util.*;

public class ApplicationPlayer {
    private final Player player;
    private List<UnoCard> playerCards;
    private String username;
    private int score;

    public ApplicationPlayer(Player player, String userName) {
        this.player = player;
        playerCards = new ArrayList<>();
        username = userName;
    }

    public void startGame(List<PlayerInfo> playerInfo) {
        player.startGame(playerInfo);
    }

    public String getUsername() {
        return username;
    }

    public Collection<UnoCard> getCards() {
        return playerCards;
    }

    public void updateTopCard(UnoCard topCard) {
        player.updateTopCard(topCard);
    }

    public void addCards(List<UnoCard> cards) {
        playerCards.addAll(cards);
        player.addCards(cards);
    }

    public void notifyWinner(String winner) {
        player.notifyWinner(winner);
    }

    public void updatePlayerCards(String username, int amountOfCards) {
        player.updatePlayerCards(username, amountOfCards);
    }

    public Optional<UnoCard> requestCard() {
        Optional<UnoCard> unoCard = player.requestCard();
        if (unoCard.isPresent()) {
            boolean isPresent = playerCards.remove(unoCard.get());
            if (!isPresent) throw new InvalidCardPlayedExcetpion("Not in the players hand");
        }
        return unoCard;
    }

    public void updatePlayerScore() {
        int sum = playerCards.stream()
                .mapToInt(UnoCard::getScore)
                .sum();
        score += sum;
    }

    public int getScore() {
        return score;
    }

    public void clearHand() {
        playerCards = new ArrayList<>();
        player.startNewRound();
    }

    public void updateScore(Map<String, Integer> scoreboard) {
        player.updateScore(scoreboard);
    }
}
