package rmi.unogame.applicationServer.game.cards;

import java.util.Objects;

public class UnoCard {
    private Colour colour;
    private final Symbol symbol;

    public UnoCard(Colour colour, Symbol symbol) {
        this.colour = colour;
        this.symbol = symbol;
    }

    public int getScore() {
        return symbol.score;
    }

    public boolean canPlayOn(UnoCard unoCard) {
        return this.colour == unoCard.colour || this.symbol == unoCard.symbol
                || this.symbol == Symbol.WILDDRAWCARD || this.symbol == Symbol.WILDCARD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UnoCard unoCard = (UnoCard) o;
        return colour == unoCard.colour &&
                symbol == unoCard.symbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(colour, symbol);
    }

    @Override
    public String toString() {
        return "UnoCard{" +
                "colour=" + colour +
                ", symbol=" + symbol +
                '}';
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    public String getCardName() {
        if (this.symbol == Symbol.WILDCARD || this.symbol == Symbol.WILDDRAWCARD) return this.symbol + "_.png";
        return this.symbol + "_" + this.colour + ".png";
    }
}
