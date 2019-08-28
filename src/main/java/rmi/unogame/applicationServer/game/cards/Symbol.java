package rmi.unogame.applicationServer.game.cards;

import java.util.Arrays;

public enum Symbol {
    ZERO(10),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    SKIP(20),
    DRAW(20),
    REVERSE(20),
    WILDCARD(50),
    WILDDRAWCARD(50);

    public final int score;

    Symbol(int score) {
        this.score = score;
    }

    public static Symbol[] getAllColourCards() {
        return Arrays.stream(Symbol.values())
            .filter(symbol -> !symbol.equals(Symbol.WILDCARD) && !symbol.equals(Symbol.WILDDRAWCARD))
            .toArray(Symbol[]::new);
    }

    @Override
    public String toString() {
        switch (this) {
            case ONE: case TWO: case THREE: case FOUR: case FIVE: case SIX: case SEVEN: case EIGHT: case NINE:
                return String.valueOf(score);
            case ZERO:
                return String.valueOf(0);
            default:
                return super.toString();
        }
    }
}
