package rmi.unogame.applicationServer.game.player.inMemPlayers;

import rmi.unogame.applicationServer.game.cards.Colour;
import rmi.unogame.applicationServer.game.cards.UnoCard;
import rmi.unogame.applicationServer.game.player.Player;
import rmi.unogame.client.gameplay.PlayerInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class LocalInMemPlayer implements Player {
    private UnoCard topCard;
    private List<UnoCard> playerCards;
    private String username;

    public LocalInMemPlayer(String username) {
        this.username = username;
        playerCards = new ArrayList<>();
    }

    @Override
    public Optional<UnoCard> requestCard() {
        Optional<UnoCard> selectedCard = playerCards.stream()
                .filter(unoCard -> unoCard.canPlayOn(topCard))
                .findAny();
        selectedCard.ifPresent(unoCard -> {
            playerCards.remove(unoCard);
            setColourWhenWildCard(selectedCard.get());
        });
        return selectedCard;
    }

    private void setColourWhenWildCard(UnoCard unoCard) {
        if (unoCard.getColour().equals(Colour.NONE)) {
            setColourOfWildCard(unoCard);
        }
    }

    private void setColourOfWildCard(UnoCard unoCard) {
        unoCard.setColour(Colour.BLUE);
    }

    @Override
    public void updateTopCard(UnoCard topCard) {
        this.topCard = topCard;
    }

    @Override
    public void addCards(List<UnoCard> cards) {
        this.playerCards.addAll(cards);
    }

    @Override
    public List<UnoCard> getCards() {
        return this.playerCards;
    }

    @Override
    public void notifyWinner(String winner) {
        System.out.println(winner);
    }

    @Override
    public void updatePlayerCards(String playerName, int amount) {

    }

    @Override
    public void startGame(List<PlayerInfo> playerInfoList) {

    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void updateScore(Map<String, Integer> scoreboard) {

    }

    @Override
    public void startNewRound() {
        playerCards = new ArrayList<>();
    }
}
