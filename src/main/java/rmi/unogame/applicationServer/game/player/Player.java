package rmi.unogame.applicationServer.game.player;

import rmi.unogame.applicationServer.game.cards.UnoCard;
import rmi.unogame.client.gameplay.PlayerInfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface Player extends Serializable {

    Optional<UnoCard> requestCard();

    void updateTopCard(UnoCard topCard);

    void addCards(List<UnoCard> cards);

    List<UnoCard> getCards();

    void notifyWinner(String winner);

    void updatePlayerCards(String playerName, int amount);

    void startGame(List<PlayerInfo> playerInfoList);

    String getUsername();

    void updateScore(Map<String, Integer> scoreboard);

    void startNewRound();
}
