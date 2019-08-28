package rmi.unogame.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rmi.unogame.client.gameplay.GameplayService;
import rmi.unogame.client.lobby.model.ClientGameInfo;

import java.io.File;
import java.net.URL;

public class GameplayMain extends Application {

    public static void main(String ...args) {
        Application.launch();
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = new File("src/main/java/rmi/unogame/client/gameplay/Game.fxml").toURI().toURL();
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        GameplayService service = new GameplayService(new ClientGameInfo("id", "name", 1, 1, 2), null);
        fxmlLoader.setController(service);
        Parent root = fxmlLoader.load();

        stage.setTitle("Game");
        stage.setScene(new Scene(root, 800, 450));
        stage.show();

    }
}
