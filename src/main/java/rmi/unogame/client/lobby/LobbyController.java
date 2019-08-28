package rmi.unogame.client.lobby;

import com.google.common.base.Strings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import rmi.unogame.client.gameplay.GameplayService;
import rmi.unogame.client.gameplay.player.RemotePlayer;
import rmi.unogame.client.lobby.model.ClientGameInfo;
import rmi.unogame.client.lobby.newGame.NewGameController;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class LobbyController {

    @FXML
    public AnchorPane pn_input;

    @FXML
    public Button btn_exit;

    @FXML
    public Button btn_scoreboard;

    private LobbyService lobbyService;

    private ObservableList gameData = FXCollections.observableArrayList();
    private ListView gamesList;
    private String selectedGameId;

    public LobbyController(LobbyService lobbyService) {
        this.lobbyService = lobbyService;
    }

    public void initialize() {
        gamesList = new ListView();
        gamesList.getSelectionModel().selectedItemProperty().addListener(
                (ChangeListener<String>) (observable, oldValue, newValue) -> setSelectedGame(newValue));
        setList();
        //lbl_username.setText(clientUserInfo.getUsername());
        this.gamesList = new ListView(gameData);
    }

    private void setSelectedGame(String newValue) {
        String gameId = Arrays.asList(Strings.nullToEmpty(newValue).split("\t")).get(0);
        this.selectedGameId = Strings.nullToEmpty(gameId);
    }

    public void setList() {
        reload();
        pn_input.getChildren().add(gamesList);
        gamesList.prefWidthProperty().bind(pn_input.widthProperty());
        gamesList.prefHeightProperty().bind(pn_input.heightProperty());
    }

    @FXML
    public void reload() {
        gameData.clear();
        gameData.addAll(lobbyService.getGames());
        gamesList.setEditable(false);
        gamesList.setItems(gameData);
    }

    @FXML
    public void joinGame() throws InterruptedException {
        System.out.println("join game");
        Optional<ClientGameInfo> selectedGame = lobbyService.findSelectedGame(selectedGameId);

        if (selectedGame.isPresent()) {
            GameplayService service = new GameplayService(selectedGame.get(), lobbyService.getToken());

            Runnable joinGame = () -> lobbyService.joinGame(selectedGameId, new RemotePlayer(service, lobbyService.getUsername()));

            ExecutorService executorService = Executors.newFixedThreadPool(2);
            startGameplay(service);
            executorService.submit(joinGame);
            exit();
        }
    }

    private void startGameplay(GameplayService service) {
        try {
            loadFXMLFile(service);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    private void loadFXMLFile(GameplayService service) throws java.io.IOException {
        URL url = new File("src/main/java/rmi/unogame/client/gameplay/Game.fxml").toURI().toURL();

        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setController(service);
        Parent root = fxmlLoader.load();

        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setTitle("Game");
        stage.setScene(new Scene(root));
        stage.setMaximized(true);
        stage.show();
    }

    @FXML
    public void createNewGame() {
        startPopupNewGame();
    }

    @FXML
    public void openScoreboard() {
        openUIScoreboard(lobbyService.getScoreboard());
    }

    public void startPopupNewGame(){
        try {
            URL resource = new File("src/main/java/rmi/unogame/client/lobby/newGame/PopupNewGame.fxml").toURI().toURL();

            FXMLLoader fxmlLoader = new FXMLLoader(resource);
            fxmlLoader.setController(new NewGameController(lobbyService));
            Parent root1 = fxmlLoader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Create new GameObject");
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void exit() {
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }

    private void openUIScoreboard(Map<String, Integer> scoreboard) {
        ObservableList<String> observableList = FXCollections.observableArrayList();
        observableList.addAll(mapToString(scoreboard));
        try {
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Scoreboard");
            stage.setScene(new Scene(new ListView<>(observableList), 800, 600));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<String> mapToString(Map<String, Integer> scoreboard) {
        List<String> collect = scoreboard.entrySet().stream()
                .sorted(Comparator.comparingInt(Map.Entry::getValue))
                .peek(System.out::println)
                .map(entry -> entry.getValue() + "\t\t\t" + entry.getKey())
                .collect(Collectors.toList());
        Collections.reverse(collect);
        return collect;
    }
}
