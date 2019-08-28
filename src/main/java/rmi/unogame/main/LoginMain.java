package rmi.unogame.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rmi.unogame.config.PortNumberConfig;
import rmi.unogame.dispatcher.Main;
import rmi.unogame.applicationServer.SecureApplicationServerInterface;
import rmi.unogame.dispatcher.ServerFactory;

import java.io.File;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import static rmi.unogame.dispatcher.ServerFactory.*;

public class LoginMain extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = new File("src/main/java/rmi/unogame/client/auth/login/Login.fxml").toURI().toURL();
        Parent root = FXMLLoader.load(url);
        stage.setTitle("UNO Login");
        stage.setScene(new Scene(root, 800, 450));
        stage.show();
    }

    @Override
    public void init() {
    }
}
