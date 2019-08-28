package rmi.unogame.applicationServer.game.cards;

import java.util.Arrays;

public enum Colour {
    RED,
    GREEN,
    YELLOW,
    BLUE,
    NONE;

    public static Colour[] getNormalColours() {
        return Arrays.stream(Colour.values())
                .filter(colour -> !colour.equals(Colour.NONE))
                .toArray(Colour[]::new);
    }

    @Override
    public String toString() {
        if (this == NONE) return "";
        return super.toString();
    }

}
