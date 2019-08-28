package rmi.unogame.applicationServer.game.cards;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DeckFactory {

        public static List<UnoCard> buildDeck() {
            List<UnoCard> deck = new LinkedList<>();
            List<UnoCard> colouredCards = Arrays.stream(Colour.getNormalColours())
                    .map(DeckFactory::getCardsForColour)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            List<UnoCard> wildCards = IntStream.range(0, 4)
                    .mapToObj(integer -> createWildCards())
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());

            deck.addAll(colouredCards);
            deck.addAll(wildCards);
            Collections.shuffle(deck);
            return deck;
        }
        private static List<UnoCard> createWildCards() {
            return Arrays.asList(new UnoCard(Colour.NONE, Symbol.WILDCARD), new UnoCard(Colour.NONE, Symbol.WILDDRAWCARD));
        }

        private static List<UnoCard> getCardsForColour(Colour colour) {
            return Arrays.stream(Symbol.getAllColourCards())
                    .map(symbol -> createTwoCards(colour, symbol))
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }

        private static List<UnoCard> createTwoCards(Colour colour, Symbol symbol) {
            return Arrays.asList(new UnoCard(colour, symbol), new UnoCard(colour, symbol));
        }
    }