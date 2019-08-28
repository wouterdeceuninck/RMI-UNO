package rmi.unogame.applicationServer.game;

import com.google.common.collect.ImmutableList;
import rmi.unogame.applicationServer.game.cards.Colour;
import rmi.unogame.applicationServer.game.cards.DeckFactory;
import rmi.unogame.applicationServer.game.cards.Symbol;
import rmi.unogame.applicationServer.game.cards.UnoCard;
import rmi.unogame.applicationServer.game.player.Player;
import rmi.unogame.client.gameplay.PlayerInfo;
import rmi.unogame.exceptions.InvalidCardPlayedExcetpion;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class UnoGameplayLogic {
    private List<UnoCard> deck;
    private List<UnoCard> pile;
    private List<ApplicationPlayer> playersList;

    private int playDirection;
    private int currentPlayer;
    private boolean skipNext;

    UnoGameplayLogic() {
        this.deck = DeckFactory.buildDeck();
        this.pile = new ArrayList<>();
        playDirection = 1;
        currentPlayer = 0;
    }

    void start(List<ApplicationPlayer> playersList) {
        this.playersList = playersList;
        this.playersList.forEach(ApplicationPlayer::clearHand);
        setupInitialStateOfGame();
        providePlayersInfo(playersList);
        playGame();
    }

    private void providePlayersInfo(List<ApplicationPlayer> playersList) {
        List<PlayerInfo> playerInfo = getPlayerInfo();
        playersList.forEach(player -> player.startGame(playerInfo));
    }

    private List<PlayerInfo> getPlayerInfo() {
        AtomicInteger atomicInt = new AtomicInteger(1);
        return playersList.stream()
                .map(player -> new PlayerInfo(atomicInt.getAndIncrement(), player.getUsername(), player.getCards().size()))
                .collect(Collectors.toList());
    }

    private void setupInitialStateOfGame() {
        placeFirstCard();
        updateTopCard();
        giveAllPlayersStartingCards();
    }

    private void placeFirstCard() {
        UnoCard remove = deck.remove(0);
        if (remove.getColour() == Colour.NONE) remove.setColour(Colour.BLUE);
        pile.add(remove);
    }

    private void updateTopCard() {
        playersList.forEach(player -> player.updateTopCard(getTopCard()));
    }

    private void giveAllPlayersStartingCards() {
        playersList.forEach(player -> player.addCards(getAmountOfCardsFromDeck(7)));
    }

    private List<UnoCard> getAmountOfCardsFromDeck(int amountOfCards) {
        createNewDeckWhenEmpty(amountOfCards);
        List<UnoCard> selectedCards = ImmutableList.copyOf(deck.subList(0, amountOfCards));
        deck.removeAll(selectedCards);
        return selectedCards;
    }

    private void createNewDeckWhenEmpty(int amountOfCards) {
        if (deck.size() < amountOfCards) {
            deck.addAll(DeckFactory.buildDeck());
        }
    }

    private void playGame() {
        boolean gameHasEnded = playTurnAndUpdateTopCard(getCurrentPlayer());
        while (!gameHasEnded) {
            updateIndex();
            gameHasEnded = playTurnAndUpdateTopCard(getCurrentPlayer());
        }
        notifyPlayers(getCurrentPlayer().getUsername());
        updateScore();
    }

    private void updateScore() {
        Map<String, Integer> scoreboard = playersList.stream()
                .peek(ApplicationPlayer::updatePlayerScore)
                .collect(Collectors.toMap(ApplicationPlayer::getUsername, ApplicationPlayer::getScore));
        playersList.forEach(player -> player.updateScore(scoreboard));
    }

    private boolean playTurnAndUpdateTopCard(ApplicationPlayer currentPlayer) {
        this.skipNext = false;
        boolean isGameFinished = playTurn(currentPlayer);
        updateTopCard();
        updateCurrentPlayersAmountOfCards(currentPlayer.getUsername(), currentPlayer.getCards().size());
        return isGameFinished;
    }

    private boolean playTurn(ApplicationPlayer player) {
        Optional<UnoCard> requestedCard = player.requestCard();

        if (requestedCard.isPresent()) {
            placeCardOnPile(requestedCard.get());
            return player.getCards().size() == 0;
        } else {
            player.addCards(getAmountOfCardsFromDeck(1));
            updateCurrentPlayersAmountOfCards(player.getUsername(), player.getCards().size());
        }
        return false;
    }

    private void notifyPlayers(String winner) {
        playersList.forEach(player -> player.notifyWinner(winner));
    }

    private ApplicationPlayer getCurrentPlayer() {
        return this.playersList.get(this.currentPlayer);
    }

    private void updateCurrentPlayersAmountOfCards(String username, int amountOfCards) {
        playersList.forEach(player -> player.updatePlayerCards(username, amountOfCards));
    }

    private void updateIndex() {
        this.currentPlayer= updateAccordingToDirection(this.currentPlayer, this.playDirection);
        if (skipNext) {
            skipNext = false;
            updateIndex();
        }
    }

    private int updateAccordingToDirection(int currentPlayer, int playDirection) {
        currentPlayer += playDirection;
        if (currentPlayer >= playersList.size()) currentPlayer -= playersList.size();
        if (currentPlayer < 0) currentPlayer += playersList.size();
        return currentPlayer;
    }

    private void placeCardOnPile(UnoCard card) {
        if (card.canPlayOn(getTopCard())){
            pile.add(0, card);
            handlePlayOfSpecialCard(card);
        } else throw new InvalidCardPlayedExcetpion("The card played was invalid: " + card.toString()
                + " on top of " + getTopCard().toString());
    }

    private void handlePlayOfSpecialCard(UnoCard card) {
        switch (card.getSymbol()) {
            case WILDDRAWCARD:
                drawAmountAndSkip(4);
                break;
            case SKIP:
                skipNextPlayer();
                break;
            case REVERSE:
                reversePlayDirection();
                break;
            case DRAW:
                drawAmountAndSkip(2);
                break;
            default:
                break;
        }
    }

    private void reversePlayDirection() {
        this.playDirection = playDirection * -1;
    }

    private void skipNextPlayer() {
        this.skipNext = true;
    }

    private void drawAmountAndSkip(int amountOfCards) {
        skipNextPlayer();
        ApplicationPlayer nextPlayer = getNextPlayer();
        nextPlayer.addCards(getAmountOfCardsFromDeck(amountOfCards));
        updateCurrentPlayersAmountOfCards(nextPlayer.getUsername(), nextPlayer.getCards().size());
    }

    private ApplicationPlayer getNextPlayer() {
        return playersList.get(updateAccordingToDirection(this.currentPlayer, this.playDirection));
    }

    private UnoCard getTopCard() {
        return pile.get(0);
    }

}
