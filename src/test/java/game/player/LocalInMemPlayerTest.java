package game.player;

import rmi.unogame.applicationServer.game.cards.Colour;
import rmi.unogame.applicationServer.game.cards.Symbol;
import rmi.unogame.applicationServer.game.cards.UnoCard;
import rmi.unogame.applicationServer.game.player.inMemPlayers.LocalInMemPlayer;
import rmi.unogame.applicationServer.game.player.Player;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LocalInMemPlayerTest {

    @Test
    public void createPlayer() {
        Player player = new LocalInMemPlayer("bot");
    }

    @Test
    public void setAndGetCardsFromPlayer() {
        Player player = new LocalInMemPlayer("bot");
        List<UnoCard> unoCards = Arrays.asList(new UnoCard(Colour.BLUE, Symbol.EIGHT), new UnoCard(Colour.RED, Symbol.ONE), new UnoCard(Colour.NONE, Symbol.WILDCARD));
        player.addCards(unoCards);
        List<UnoCard> cards = player.getCards();

        Assert.assertEquals(unoCards.size(), cards.size());
        unoCards.forEach(unoCard -> Assert.assertTrue(cards.contains(unoCard)));
    }

    @Test
    public void requestCardFromPlayer_expectNormalCard() {
        Player player = new LocalInMemPlayer("bot");
        UnoCard blueEight = new UnoCard(Colour.BLUE, Symbol.EIGHT);
        List<UnoCard> unoCards = Collections.singletonList(blueEight);
        player.addCards(unoCards);

        player.updateTopCard(new UnoCard(Colour.GREEN, Symbol.EIGHT));
        Optional<UnoCard> unoCard = player.requestCard();

        Assert.assertEquals(unoCard.get(), blueEight);
    }

    @Test
    public void requestCardDFromPlayer_expectNoCard() {
        Player player = new LocalInMemPlayer("bot");
        UnoCard blueEight = new UnoCard(Colour.BLUE, Symbol.EIGHT);
        List<UnoCard> unoCards = Collections.singletonList(blueEight);
        player.addCards(unoCards);

        player.updateTopCard(new UnoCard(Colour.GREEN, Symbol.SEVEN));
        Optional<UnoCard> unoCard = player.requestCard();

        Assert.assertFalse(unoCard.isPresent());
    }

    @Test
    public void requestCardFromPlayer_expectWildCard() {
        Player player = new LocalInMemPlayer("bot");
        UnoCard blueEight = new UnoCard(Colour.NONE, Symbol.WILDCARD);
        List<UnoCard> unoCards = Collections.singletonList(blueEight);
        player.addCards(unoCards);

        player.updateTopCard(new UnoCard(Colour.GREEN, Symbol.SEVEN));
        Optional<UnoCard> unoCard = player.requestCard();

        Assert.assertTrue(unoCard.isPresent());
        Assert.assertEquals(unoCard.get(), new UnoCard(Colour.BLUE, Symbol.WILDCARD));
    }
}