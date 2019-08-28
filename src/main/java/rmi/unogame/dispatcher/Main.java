package rmi.unogame.dispatcher;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import rmi.unogame.config.PortNumberConfig;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;

import static rmi.unogame.dispatcher.ServerFactory.*;

@SpringBootApplication
public class Main extends Application {

    private ConfigurableApplicationContext springContext;

    public static void main(String... args) {
        Application.launch(args);
    }

    @Override
    public void init() {
        springContext = bootstrapSpringApplicationContext();
        createDispatcherServer(PortNumberConfig.DISPATCHER_PORT_NUMBER.getProperty());
    }


    @Override
    public void start(Stage stage) {
        displayInitialScene(stage);
    }

    private void displayInitialScene(Stage stage) {
        try {
            URL url = new File("src/main/java/rmi/unogame/client/auth/login/Login.fxml").toURI().toURL();
            FXMLLoader fxmlLoader = new FXMLLoader(url);
            Parent root1 = fxmlLoader.load();

            stage.setTitle("Login");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void stop() throws Exception {
        springContext.close();
    }

    private ConfigurableApplicationContext bootstrapSpringApplicationContext() {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(Main.class);
        String[] parameters = getParameters().getRaw().stream().toArray(String[]::new);
        builder.headless(false);
        return builder.run(parameters);
    }
}
