package rmi.unogame.client.lobby.model;

public class ClientGameInfo {
    private String id;
    private String gameName;
    private int gameTheme;
    private int connectedAmountOfPlayers;
    private int gameSize;

    public ClientGameInfo(String id, String gameName, int gameTheme, int connectedAmountOfPlayers, int gameSize) {
        this.id = id;
        this.gameName = gameName;
        this.gameTheme = gameTheme;
        this.connectedAmountOfPlayers = connectedAmountOfPlayers;
        this.gameSize = gameSize;
    }

    public String getId() {
        return id;
    }

    public String getGameName() {
        return gameName;
    }

    public int getGameTheme() {
        return gameTheme;
    }

    public int getConnectedAmountOfPlayers() {
        return connectedAmountOfPlayers;
    }

    public int getGameSize() {
        return gameSize;
    }

    @Override
    public String toString() {
        return id + "\t" + gameName + "\t" + connectedAmountOfPlayers + "/" + gameSize + "\t" + gameTheme;
    }

    public static class Builder {
        private String id = "";
        private String gameName = "";
        private int gameTheme;
        private int connectedAmountOfPlayers;
        private int gameSize;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setGameName(String gameName) {
            this.gameName = gameName;
            return this;
        }

        public Builder setGameTheme(int gameTheme) {
            this.gameTheme = gameTheme;
            return this;
        }

        public Builder setConnectedAmountOfPlayers(int connectedAmountOfPlayers) {
            this.connectedAmountOfPlayers = connectedAmountOfPlayers;
            return this;
        }

        public Builder setGameSize(int gameSize) {
            this.gameSize = gameSize;
            return this;
        }

        public ClientGameInfo build() {
            return new ClientGameInfo(id, gameName, gameTheme, connectedAmountOfPlayers, gameSize);
        }
    }
}
