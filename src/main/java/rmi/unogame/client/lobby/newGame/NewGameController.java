package rmi.unogame.client.lobby.newGame;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import rmi.unogame.client.lobby.LobbyService;
import rmi.unogame.client.lobby.model.NewGameInfo;

import java.rmi.RemoteException;

public class NewGameController {

    private final LobbyService lobbyService;
    private ObservableList<Integer> themeList = FXCollections.observableArrayList(0, 1);
    private ObservableList<Integer> numberOfPlayerList = FXCollections.observableArrayList(1, 2, 3, 4);
    private ObservableList<Integer> numberOfBotsList = FXCollections.observableArrayList(0, 1, 2, 3);

    public NewGameController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    @FXML
    TextField gameName;

    @FXML
    ChoiceBox<Integer> themePicker = new ChoiceBox<>(themeList);
    @FXML
    ChoiceBox<Integer> numberOfBots = new ChoiceBox<>(numberOfPlayerList);
    @FXML
    ChoiceBox<Integer> numberOfPlayers = new ChoiceBox<>(numberOfBotsList);

    @FXML
    Button btn_start, btn_cancel;

    @FXML
    public void initialize() {
        themePicker.setItems(themeList);
        numberOfPlayers.setItems(numberOfPlayerList);
        numberOfBots.setItems(numberOfBotsList);
    }

    @FXML
    public void startGame() throws RemoteException {

        NewGameInfo gameInfo = createGameInfo();
        lobbyService.createGame(gameInfo);
        closeWindow();
    }

    @FXML
    public void cancel() {
        closeWindow();
    }

    private NewGameInfo createGameInfo() {
        return new NewGameInfo(gameName.getText(), numberOfPlayers.getValue(), themePicker.getValue(), numberOfBots.getValue());
    }

    public void closeWindow() {
        Stage stage = (Stage) btn_cancel.getScene().getWindow();
        stage.close();
    }

}
