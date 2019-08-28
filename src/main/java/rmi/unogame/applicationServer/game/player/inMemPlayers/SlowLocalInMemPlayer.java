package rmi.unogame.applicationServer.game.player.inMemPlayers;

import rmi.unogame.applicationServer.game.cards.UnoCard;

import java.util.Optional;

public class SlowLocalInMemPlayer extends  LocalInMemPlayer {

    public SlowLocalInMemPlayer(String username) {
        super(username);
    }

    @Override
    public Optional<UnoCard> requestCard() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return super.requestCard();
    }

}
