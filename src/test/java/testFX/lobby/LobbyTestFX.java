package testFX.lobby;

import javafx.scene.Node;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.HBox;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;
import testFX.screen.LobbyScreen;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class LobbyTestFX extends LobbyScreen {

    public void createNewGameAndJoin() {
        String gameName = String.valueOf(Instant.now().toEpochMilli());
        clickCreateGame();
        handleNewGamePopup(gameName);
        reloadGames();
        findCreatedGame(gameName);
        clickOn(joinButton);
    }

    @Test
    public void createNewGameWithBotAndPlay() throws TimeoutException {
        createNewGameAndJoin();
        waitUntilGameHasLoaded();
        playGame();
    }

    private void playGame() throws TimeoutException {
        while (!isWinnerPopupShown()) {
            waitForTurnAndSelectACard();
        }
    }

    private void waitUntilGameHasLoaded() throws TimeoutException {
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, () ->find("#userBox").isVisible());
    }

    private boolean isWinnerPopupShown() {
        final javafx.stage.Stage actualAlertDialog = getTopModalStage();
        if (actualAlertDialog.getScene().getRoot() instanceof DialogPane) {
            DialogPane root = (DialogPane) actualAlertDialog.getScene().getRoot();
            return root.getHeaderText().contains("The game has ended!");
        } else return false;
    }

    private void waitForTurnAndSelectACard() throws TimeoutException {
        WaitForAsyncUtils.waitFor(10, TimeUnit.SECONDS, this::isMyTurnOrPlayerHasWon);
        try {
            selectCard();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void selectCard() throws InterruptedException, TimeoutException {
        HBox cards = find("#userBox");
        Optional<Node> playableCard = cards.getChildren().stream()
                .filter(node -> node.getOpacity() == 1)
                .findFirst();
        if (playableCard.isPresent()) {
            Node node = playableCard.get();
            try {
                clickOn(node);
            } catch (Exception e) {
                waitForTurnAndSelectACard();
            }
            handleBlackCard();
        } else clickOn("#btn_drawCard");
    }

    private void handleBlackCard() throws InterruptedException {
        Thread.sleep(100L);
        if (isMyTurnOrPlayerHasWon()) {
            clickOn("#btn_red");
        }
    }

    private boolean isMyTurnOrPlayerHasWon() {
        return isMyTurn() || isWinnerPopupShown();
    }

    private boolean isMyTurn() {
        Node button = find("#btn_red");
        return button.getOpacity() == 1;
    }
}
