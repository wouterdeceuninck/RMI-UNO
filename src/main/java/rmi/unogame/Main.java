package rmi.unogame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rmi.unogame.config.PortNumberConfig;
import rmi.unogame.dispatcher.ServerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    public static void main(String... args) {
        ServerFactory.createDispatcherServer(PortNumberConfig.DISPATCHER_PORT_NUMBER.getProperty());
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try {
            URL url = new File("src/main/java/rmi/unogame/client/auth/login/Login.fxml").toURI().toURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent root1 = fxmlLoader.load();

            primaryStage.setTitle("Login");
            primaryStage.setScene(new Scene(root1));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
