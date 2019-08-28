package game.cards;

import rmi.unogame.applicationServer.game.cards.Symbol;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class SymbolTest {

    @Test
    public void getAllColouredCards() {
        List<Symbol> symbols = Arrays.asList(Symbol.getAllColourCards());
        Assert.assertTrue(symbols.contains(Symbol.ZERO));
        Assert.assertTrue(symbols.contains(Symbol.ONE));
        Assert.assertTrue(symbols.contains(Symbol.TWO));
        Assert.assertTrue(symbols.contains(Symbol.THREE));
        Assert.assertTrue(symbols.contains(Symbol.FOUR));
        Assert.assertTrue(symbols.contains(Symbol.FIVE));
        Assert.assertTrue(symbols.contains(Symbol.SIX));
        Assert.assertTrue(symbols.contains(Symbol.SEVEN));
        Assert.assertTrue(symbols.contains(Symbol.EIGHT));
        Assert.assertTrue(symbols.contains(Symbol.NINE));

        Assert.assertTrue(symbols.contains(Symbol.SKIP));
        Assert.assertTrue(symbols.contains(Symbol.DRAW));
        Assert.assertTrue(symbols.contains(Symbol.REVERSE));

        Assert.assertFalse(symbols.contains(Symbol.WILDCARD));
        Assert.assertFalse(symbols.contains(Symbol.WILDDRAWCARD));
    }
}