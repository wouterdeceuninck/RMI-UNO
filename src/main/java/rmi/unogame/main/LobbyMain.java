package rmi.unogame.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;
import rmi.unogame.config.PortNumberConfig;
import rmi.unogame.applicationServer.SecureApplicationServerInterface;
import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.applicationServer.service.authentication.auth.TokenFactory;
import rmi.unogame.client.auth.dto.Credentials;
import rmi.unogame.client.lobby.LobbyController;
import rmi.unogame.client.lobby.LobbyService;

import java.io.File;
import java.net.URL;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.Instant;

import static rmi.unogame.dispatcher.ServerFactory.*;

public class LobbyMain extends Application {

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        URL url = new File("src/main/java/rmi/unogame/client/lobby/Lobby.fxml").toURI().toURL();
        FXMLLoader fxmlLoader = new FXMLLoader(url);

        fxmlLoader.setController(new LobbyController(getLobbyService()));
        Parent root = fxmlLoader.load();

        stage.setTitle("Lobby");
        stage.setScene(new Scene(root, 800, 450));
        stage.show();
    }

    private LobbyService getLobbyService() throws RemoteException, NotBoundException {
        SecureApplicationServerInterface serverInterface = lookupServer();
        Credentials credentials = new Credentials("testuser", "password");
        JWTToken token = TokenFactory.createToken(credentials, Instant.now().toEpochMilli(), BCrypt.gensalt());

        return new LobbyService(serverInterface, token);
    }

    private static SecureApplicationServerInterface lookupServer() throws RemoteException, NotBoundException {
        return  (SecureApplicationServerInterface) LocateRegistry
                .getRegistry("localhost", 1200)
                .lookup("UNOserver");
    }

    @Override
    public void init() {
        createDispatcherServer(PortNumberConfig.DISPATCHER_PORT_NUMBER.getProperty());
    }
}
