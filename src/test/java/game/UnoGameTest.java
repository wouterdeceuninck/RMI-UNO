package game;

import rmi.unogame.applicationServer.game.ApplicationPlayer;
import rmi.unogame.applicationServer.game.dtoObjects.UnoGameInfo;
import rmi.unogame.applicationServer.game.UnoGame;
import rmi.unogame.applicationServer.game.player.inMemPlayers.LocalInMemPlayer;
import rmi.unogame.applicationServer.game.player.Player;
import org.junit.Test;

public class UnoGameTest {

    @Test
    public void addPlayersToTheGame() {
        UnoGameInfo gameInfo = new UnoGameInfo("id", "testGameName", 4, 0, 1, 0);
        UnoGame unoGame = new UnoGame(gameInfo);
        Player player1 = new LocalInMemPlayer("bot");
        Player player2 = new LocalInMemPlayer("bot");
        Player player3 = new LocalInMemPlayer("bot");
        Player player4 = new LocalInMemPlayer("bot");

        unoGame.addPlayer(new ApplicationPlayer(player1, "bot"));
        unoGame.addPlayer(new ApplicationPlayer(player2, "bot"));
        unoGame.addPlayer(new ApplicationPlayer(player3, "bot"));
        unoGame.addPlayer(new ApplicationPlayer(player4, "bot"));

        unoGame.startGame();
    }
}
