package rmi.unogame.client.auth.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rmi.unogame.client.lobby.model.ClientGameInfo;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class LoginControllerTest extends Application {

    public static void main (String [] args) throws IOException {
        launch(args);
    }

    private static ClientGameInfo createGameInfo(int index) {
        return new ClientGameInfo(String.valueOf(index), "gameName" + index, 1, 2, 4);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = new File("src/main/java/rmi/unogame/client/login/login/Login.fxml").toURI().toURL();
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        Parent root1 = fxmlLoader.load();

        stage.setTitle("GameObject Lobby");
        stage.setScene(new Scene(root1));
        stage.show();

    }

}