package testFX.login;

import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.junit.Ignore;
import org.junit.Test;
import org.testfx.util.WaitForAsyncUtils;
import testFX.screen.LoginScreen;

import java.time.Instant;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.testfx.util.WaitForAsyncUtils.waitFor;

@Ignore
public class AuthenticationTestFX extends LoginScreen {

    @Test
    public void writeRegisterCredentials_expectAlreadyUsed() throws TimeoutException {
        registerUser("username", "password", "password");
        alertDialogHasHeaderAndContent("Error!", "The username is already used!");
    }

    @Test
    public void writeRegisterCredentials_expectPopUp() {
        registerUser("username", "password", "differentPassword");
        alertDialogHasHeaderAndContent("Error!", "The passwords must match!");
    }

    @Ignore
    @Test
    public void writeRegisterCredentials() throws TimeoutException {
        registerUser(String.valueOf(Instant.now().toEpochMilli()), "password", "password");
        expectLobbyScreen();
    }

    private void expectLobbyScreen() throws TimeoutException {
        Stage topModalStage = getTopModalStage();
        waitFor(10, TimeUnit.SECONDS, () -> getTopModalStage() != null && getTopModalStage().getTitle() != null);
        assertEquals("Lobby", topModalStage.getTitle());
    }

    public void alertDialogHasHeaderAndContent(final String expectedHeader, final String expectedContent) {
        final javafx.stage.Stage actualAlertDialog = getTopModalStage();
        assertNotNull(actualAlertDialog);

        final DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
        assertEquals(expectedHeader, dialogPane.getHeaderText());
        assertEquals(expectedContent, dialogPane.getContentText());
    }
}
