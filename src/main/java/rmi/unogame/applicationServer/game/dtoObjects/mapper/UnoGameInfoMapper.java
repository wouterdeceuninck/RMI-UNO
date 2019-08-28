package rmi.unogame.applicationServer.game.dtoObjects.mapper;

import rmi.unogame.applicationServer.game.dtoObjects.UnoGameInfo;
import rmi.unogame.client.lobby.model.NewGameInfo;

import java.util.UUID;

public class UnoGameInfoMapper {
    public static UnoGameInfo fromNewGameInfo(NewGameInfo gameInfo) {
        return new UnoGameInfo.UnoGameInfoBuilder()
                .setId(UUID.randomUUID().toString())
                .setGameName(gameInfo.getGameName())
                .setMaxAmountOfPlayers(gameInfo.getAmountOfPlayers())
                .setTheme(gameInfo.getTheme())
                .setAmountOfBots(gameInfo.getNumberOfBots())
                .build();
    }
}
