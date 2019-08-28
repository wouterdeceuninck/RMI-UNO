package game.cards;

import rmi.unogame.applicationServer.game.cards.Colour;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ColourTest {

    @Test
    public void getNormalColours() {
        List<Colour> colours = Arrays.asList(Colour.getNormalColours());

        Assert.assertTrue(colours.contains(Colour.BLUE));
        Assert.assertTrue(colours.contains(Colour.GREEN));
        Assert.assertTrue(colours.contains(Colour.YELLOW));
        Assert.assertTrue(colours.contains(Colour.RED));

        Assert.assertFalse(colours.contains(Colour.NONE));
    }
}