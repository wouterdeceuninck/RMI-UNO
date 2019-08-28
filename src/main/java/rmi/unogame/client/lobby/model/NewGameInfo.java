package rmi.unogame.client.lobby.model;

import java.io.Serializable;

public class NewGameInfo implements Serializable {
    private final String gameName;
    private final int amountOfPlayers;
    private final int theme;
    private final int numberOfBots;

    public NewGameInfo(String gameName, int amountOfPlayers, int theme, int numberOfBots) {
        this.gameName = gameName;
        this.amountOfPlayers = amountOfPlayers;
        this.theme = theme;
        this.numberOfBots = numberOfBots;
    }

    public String getGameName() {
        return gameName;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public int getTheme() {
        return theme;
    }

    public int getNumberOfBots() {
        return numberOfBots;
    }
}
