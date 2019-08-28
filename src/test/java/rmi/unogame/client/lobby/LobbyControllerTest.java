package rmi.unogame.client.lobby;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.junit.Test;
import org.mockito.Mockito;
import rmi.unogame.applicationServer.SecureApplicationServerInterface;
import rmi.unogame.applicationServer.SecureApplicationService;
import rmi.unogame.client.lobby.model.ClientGameInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class LobbyControllerTest extends Application {

    public static void main (String [] args) throws IOException {
        launch(args);
    }

    private static ClientGameInfo createGameInfo(int index) {
        return new ClientGameInfo(String.valueOf(index), "gameName" + index, 1, 2, 4);
    }

    @Override
    public void start(Stage stage) throws Exception {
        SecureApplicationServerInterface applicationServerInterface = Mockito.mock(SecureApplicationService.class);

        when(applicationServerInterface.getActiveGames(any())).thenReturn(Arrays.asList(createGameInfo(1), createGameInfo(2)));

        LobbyController controller = new LobbyController(new LobbyService(applicationServerInterface, null));

        URL url = new File("src/main/java/rmi/unogame/client/lobby/Lobby.fxml").toURI().toURL();
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        fxmlLoader.setController(controller);
        Parent root1 = fxmlLoader.load();

        stage.setTitle("GameObject Lobby");
        stage.setScene(new Scene(root1));
        stage.show();

    }
}