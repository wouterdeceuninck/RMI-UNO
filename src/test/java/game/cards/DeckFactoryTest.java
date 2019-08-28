package game.cards;

import rmi.unogame.applicationServer.game.cards.Colour;
import rmi.unogame.applicationServer.game.cards.DeckFactory;
import rmi.unogame.applicationServer.game.cards.Symbol;
import rmi.unogame.applicationServer.game.cards.UnoCard;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class DeckFactoryTest {

    @Test
    public void createNewUnoGame_checkIfAllCardsPresent() {
        List<UnoCard> deck = DeckFactory.buildDeck();

        Assert.assertEquals(112, deck.size());
        Assert.assertEquals(2, countAmountOfCard(deck, new UnoCard(Colour.BLUE, Symbol.DRAW)));
        Assert.assertEquals(4, countAmountOfCard(deck, new UnoCard(Colour.NONE, Symbol.WILDDRAWCARD)));
    }

    private int countAmountOfCard(List<UnoCard> deck, UnoCard unoCard) {
        return (int) deck.stream()
                .filter(unoCard::equals)
                .count();
    }
}