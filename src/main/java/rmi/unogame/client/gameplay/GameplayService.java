package rmi.unogame.client.gameplay;

import com.google.common.collect.ImmutableList;
import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.applicationServer.game.cards.UnoCard;
import rmi.unogame.client.lobby.model.ClientGameInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GameplayService extends GameplayController {
    private List<UnoCard> cards;
    private final ClientGameInfo gameInfo;
    private final JWTToken token;
    private UnoCard topCard = null;

    public GameplayService(ClientGameInfo info, JWTToken token) {
        super(info, token.getUserName());
        this.token = token;
        this.gameInfo = info;
        this.cards = new ArrayList<>();
    }

    public Optional<UnoCard> requestCard() {
        return super.requestCard();
    }

    public void updateTopCard(UnoCard topCard) {
        this.topCard = topCard;
        super.updateTopCard(topCard);
    }

    public void addCards(List<UnoCard> cards) {
        this.cards.addAll(cards);
        super.setCards(this.cards);
    }

    public List<UnoCard> getCards() {
        return ImmutableList.copyOf(this.cards);
    }

    public void updatePlayerInfo(String playerName, int amount) {
        super.updatePlayerAmountOfCards(playerName, amount);
    }

    public void clearCards() {
        cards = new ArrayList<>();
        super.setCards(this.cards);
    }
}
