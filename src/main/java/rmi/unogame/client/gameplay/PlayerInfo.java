package rmi.unogame.client.gameplay;

public class PlayerInfo {

    private final String username;
    private int amountOfCards;
    private int id;

    public PlayerInfo(int id, String username, int amountOfCards) {
        this.id = id;
        this.username = username;
        this.amountOfCards = amountOfCards;
    }

    public String getUsername() {
        return username;
    }

    public int getAmountOfCards() {
        return amountOfCards;
    }

    public void setAmountOfCards(int amountOfCards) {
        this.amountOfCards = amountOfCards;
    }

    public int getId() {
        return this.id;
    }

    public PlayerInfo setId(int id) {
        this.id = id;
        return this;
    }
}
