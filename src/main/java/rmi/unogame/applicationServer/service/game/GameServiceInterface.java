package rmi.unogame.applicationServer.service.game;

import rmi.unogame.applicationServer.game.ApplicationPlayer;
import rmi.unogame.applicationServer.game.dtoObjects.UnoGameInfo;
import rmi.unogame.client.lobby.model.NewGameInfo;

import java.io.Serializable;
import java.util.List;

public interface GameServiceInterface extends Serializable {

    void createGame(NewGameInfo gameInfo);

    void addPlayerToGame(String id, ApplicationPlayer player);

    List<UnoGameInfo> getActiveGames();
}
