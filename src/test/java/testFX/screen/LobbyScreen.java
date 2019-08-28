package testFX.screen;

import javafx.scene.Parent;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import org.junit.Assert;
import org.junit.Before;
import org.testfx.api.FxAssert;
import org.testfx.framework.junit.ApplicationTest;
import org.testfx.matcher.base.ParentMatchers;
import rmi.unogame.main.LobbyMain;
import testFX.utils.BaseFXTest;

import java.util.Optional;

public class LobbyScreen extends BaseFXTest {

    public String reloadButton = "#btn_reload";
    public String joinButton = "#btn_join";
    public String createGameButton = "#btn_new";
    public String exitButton = "#btn_exit";
    public String listOfGames = "#pn_input";

    String numberOfPlayers = "#numberOfPlayers";
    String numberOfBots = "#numberOfBots";
    String themePicker = "#themePicker";
    String startGameButton = "#btn_start";
    String gameName = "#gameName";


    @Override
    @Before
    public void setupClass() throws Exception {
        ApplicationTest.launch(LobbyMain.class);
    }

    public void clickCreateGame() {
        clickOn(createGameButton);
    }

    public void reloadGames() {
        clickOn(reloadButton);
    }

    public void findCreatedGame(String gameName) {
        FxAssert.verifyThat(listOfGames, ParentMatchers.hasChildren(1));

        AnchorPane node = (AnchorPane) lookup(listOfGames).queryFirst();
        ListView<String> listView = (ListView<String>) node.getChildren().get(0);

        Optional<String> gameFromList = listView.getItems().stream()
                .filter(game -> game.contains(gameName))
                .findAny();

        Assert.assertTrue(gameFromList.isPresent());

        clickOn(listOfGames);
        type(KeyCode.DOWN);
    }

    public void handleNewGamePopup(String gameName) {
        Parent root = getTopModalStage().getScene().getRoot();

        clickOn(root.lookup(this.gameName));

        write(gameName);
        selectTheme(root);
        selectNumberOfPlayers(root);
        selectNumberOfBots(root);

        clickOn(root.lookup(startGameButton));
    }

    private void selectNumberOfBots(Parent root) {
        clickOn(root.lookup(numberOfBots));
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void selectTheme(Parent root) {
        clickOn(root.lookup(themePicker));
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }

    private void selectNumberOfPlayers(Parent root) {
        clickOn(root.lookup(numberOfPlayers));
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.DOWN);
        type(KeyCode.ENTER);
    }
}
