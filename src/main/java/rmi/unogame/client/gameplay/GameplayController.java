package rmi.unogame.client.gameplay;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import rmi.unogame.applicationServer.game.cards.Colour;
import rmi.unogame.applicationServer.game.cards.Symbol;
import rmi.unogame.applicationServer.game.cards.UnoCard;
import rmi.unogame.client.gameplay.opponent.OpponentUI;
import rmi.unogame.client.gameplay.util.ImageViewFactory;
import rmi.unogame.client.lobby.model.ClientGameInfo;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.awaitility.Awaitility.await;

public class GameplayController extends OpponentUI {

    @FXML
    private TextField me;

    @FXML
    private Button btn_red;
    @FXML
    private Button btn_blue;
    @FXML
    private Button btn_yellow;
    @FXML
    private Button btn_green;

    @FXML
    private ImageView btn_drawCard;
    @FXML
    private ImageView image_lastcard;

    @FXML
    private Label title;
    @FXML
    private HBox userBox;
    @FXML
    private TextArea text_scoreboard;

    private Colour selectedColor;
    private Image backImage;
    private final String path = "src/main/resources/pictures/";

    public int theme;
    private List<UnoCard> cardsList;
    private UnoCard selectedCard;
    private UnoCard topCard;
    private boolean drawCardSelected;
    private List<PlayerInfo> players;
    private String username;
    private final ClientGameInfo gameInfo;

    public GameplayController(ClientGameInfo gameInfo, String userName) {
        super(gameInfo.getGameTheme(), "src/main/resources/pictures/");
        this.username = userName;
        this.theme = gameInfo.getGameTheme();
        this.gameInfo = gameInfo;

        image_lastcard = new ImageView();
        text_scoreboard = new TextArea();

        cardsList = new ArrayList<>();
        players = new ArrayList<>();
        try {
            backImage = new Image(new FileInputStream(path + theme + "/UNO-Back.png"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        btn_drawCard.setImage(backImage);
        btn_drawCard.setFocusTraversable(true);
        btn_drawCard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            drawCardSelected = true;
            event.consume();
        });
        deactivateButtons();
        title.setText(gameInfo.getGameName());

        text_scoreboard.setEditable(false);
    }

    public Optional<UnoCard> requestCard() {
        Platform.runLater(() -> setUserBoxCards(true));
        Platform.runLater(this::activateButtons);

        selectedCard = null;
        drawCardSelected = false;

        try {
            await().atMost(5, TimeUnit.SECONDS).until(cardIsSelected());
        } catch (Exception e) {
            endTurn();
            return Optional.empty();
        }

        if (selectedCard == null) {
            endTurn();
            return Optional.empty();
        }

        if (selectedCard.getSymbol() == Symbol.WILDDRAWCARD) handleBlackCards();
        if (selectedCard.getSymbol() == Symbol.WILDCARD) handleBlackCards();

        endTurn();
        return Optional.of(selectedCard);
    }

    private void endTurn() {
        Platform.runLater(() -> setUserBoxCards(false));
        Platform.runLater(this::deactivateButtons);
    }

    private void handleBlackCards() {
        selectedColor = null;
        await().atMost(5, TimeUnit.SECONDS).until(() -> selectedColor != null);

        if (selectedColor != null) selectedCard.setColour(selectedColor);
        else selectedColor = Colour.RED;
    }

    private Callable<Boolean> cardIsSelected() {
        return () -> selectedCard != null || drawCardSelected;
    }

    private void deactivateButtons() {
        btn_red.setOpacity(0.5);
        btn_blue.setOpacity(0.5);
        btn_yellow.setOpacity(0.5);
        btn_green.setOpacity(0.5);
    }

    private void activateButtons() {
        btn_red.setOpacity(1);
        btn_blue.setOpacity(1);
        btn_yellow.setOpacity(1);
        btn_green.setOpacity(1);
    }

    public void updateTopCard(UnoCard topCard) {
        this.topCard = topCard;
        try {
            image_lastcard.setImage(new Image(new FileInputStream(path + theme + "/" + topCard.getCardName())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Platform.runLater(() -> setUserBoxCards(false));
    }

    public void setCards (List<UnoCard> cards) {
        this.cardsList = cards;
        Platform.runLater(() -> setUserBoxCards(false));
    }

    private void setUserBoxCards(boolean isMyTurn) {
        userBox.getChildren().clear();
        cardsList.forEach(card -> setASingleCardInBox(card, isMyTurn));
    }

    private void setASingleCardInBox(UnoCard card, boolean myTurn) {
        try {
            ImageView imageView = createImageView(card);
            if (myTurn) setOpacity(imageView, card.canPlayOn(topCard));
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, this::setSelectedCard);
            userBox.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ImageView createImageView(UnoCard card) throws FileNotFoundException {
        return ImageViewFactory.buildForCardName(path + theme + "/" + card.getCardName());
    }

    private void setSelectedCard(MouseEvent event) {
        for (int i = 0; i < userBox.getChildren().size(); i++) {
            if (userBox.getChildren().get(i) == event.getTarget() && cardsList.get(i).canPlayOn(topCard)) {
                selectedCard = cardsList.remove(i);
                break;
            }
        }
        event.consume();
    }

    private void setOpacity(ImageView imageView, boolean isPlayable) {
        if (isPlayable) {
            imageView.setOpacity(1);
        } else imageView.setOpacity(0.5);
    }

    public void notifyWinner(String winner) {
        Platform.runLater(() -> popupNewInfoRoundWon(winner));
    }

    private void popupNewInfoRoundWon(String winner) {
        Alert errorAlert = new Alert(Alert.AlertType.INFORMATION);
        errorAlert.setHeaderText("The game has ended!");
        errorAlert.setContentText(winner + " has won the game!");
        errorAlert.showAndWait();
    }

    public void updatePlayerAmountOfCards(String playerName, int amount) {
        players.stream()
                .filter(playerInfo -> playerInfo.getUsername().equals(playerName))
                .findAny()
                .ifPresent(playerInfo -> playerInfo.setAmountOfCards(amount));
        setOpponentsCards();
    }

    public void setPlayerInfo(List<PlayerInfo> playerInfoList) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        players = playerInfoList.stream()
                .filter(this::isNotMe)
                .map(playerInfo -> playerInfo.setId(atomicInteger.getAndIncrement()))
                .collect(Collectors.toList());

        setOpponentsCards();
        setOpponentsNames();
    }

    private void setOpponentsNames() {
        players.forEach(playerInfo -> super.setOpponentsNames(playerInfo));
    }

    private void setOpponentsCards() {
        Platform.runLater(this::doUIChanges);
    }

    private void doUIChanges() {
        players.stream()
                .filter(this::isNotMe)
                .forEach(this::setOpponentCards);
    }

    private boolean isNotMe(PlayerInfo playerInfo) {
        return !playerInfo.getUsername().equals(username);
    }

    @FXML
    public void clickedRed() {
        this.selectedColor = Colour.RED;
    }

    @FXML
    public void clickedBlue() {
        this.selectedColor = Colour.BLUE;
    }

    @FXML
    public void clickedYellow() {
        this.selectedColor = Colour.YELLOW;
    }

    @FXML
    public void clickedGreen() {
        this.selectedColor = Colour.GREEN;
    }

    public void updateScore(Map<String, Integer> scoreboard) {
        text_scoreboard.clear();

        scoreboard.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "\t\t" + entry.getValue() + "\n")
                .forEach(line -> text_scoreboard.appendText(line));
    }

}
