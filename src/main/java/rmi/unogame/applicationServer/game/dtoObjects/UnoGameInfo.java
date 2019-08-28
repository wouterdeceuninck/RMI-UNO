package rmi.unogame.applicationServer.game.dtoObjects;

public class UnoGameInfo {
    private final String id;
    private final String gameName;
    private final int maxAmountOfPlayers;
    private int joinedPlayers;
    private final int theme;
    private final int amountOfBots;

    public UnoGameInfo(String id, String gameName, int maxAmountOfPlayers, int joinedPlayers, int theme, int amountOfBots) {
        this.id = id;
        this.gameName = gameName;
        this.maxAmountOfPlayers = maxAmountOfPlayers;
        this.joinedPlayers = joinedPlayers;
        this.theme = theme;
        this.amountOfBots = amountOfBots;
    }

    public int getAmountOfBots() {
        return amountOfBots;
    }

    public String getId() {
        return id;
    }

    public String getGameName() {
        return gameName;
    }

    public int getJoinedPlayers() {
        return joinedPlayers;
    }

    public void setJoinedPlayers(int amountOfPlayers) {
        this.joinedPlayers = amountOfPlayers;
    }

    public int getMaxAmountOfPlayers() {
        return maxAmountOfPlayers;
    }

    public int getTheme() {
        return this.theme;
    }

    public static class UnoGameInfoBuilder {
        private int amountOfBots;
        private String id = "";
        private String gameName = "";
        private int maxAmountOfPlayers = 0;
        private int joinedPlayers = 0;
        private int theme;

        public UnoGameInfoBuilder setId(String id) {
            this.id = id;
            return this;
        }

        public UnoGameInfoBuilder setAmountOfBots(int amountOfBots) {
            this.amountOfBots = amountOfBots;
            return this;
        }

        public UnoGameInfoBuilder setGameName(String gameName) {
            this.gameName = gameName;
            return this;
        }

        public UnoGameInfoBuilder setMaxAmountOfPlayers(int maxAmountOfPlayers) {
            this.maxAmountOfPlayers = maxAmountOfPlayers;
            return this;
        }

        public UnoGameInfoBuilder setJoinedPlayers(int amountOfPlayers) {
            this.joinedPlayers = amountOfPlayers;
            return this;
        }

        public UnoGameInfoBuilder setTheme(int theme) {
            this.theme = theme;
            return this;
        }

        public UnoGameInfo build() {
            return new UnoGameInfo(id, gameName, maxAmountOfPlayers, joinedPlayers, theme, amountOfBots);
        }
    }
}
