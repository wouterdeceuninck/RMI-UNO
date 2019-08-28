package rmi.comparison;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rmi.unogame.applicationServer.service.authentication.UserInfo;
import rmi.unogame.database.DatabaseServerInterface;
import rmi.unogame.dispatcher.ServerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.HashMap;
import java.util.Map;

public class GraphicsConsistency extends Application {

    private DatabaseServerInterface database1;
    private DatabaseServerInterface database2;
    private DatabaseServerInterface database3;
    private DatabaseServerInterface database4;
    private Map<Integer, DatabaseServerInterface> map;

    public static void main(String... args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        setup();

        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();

        xAxis.setLabel("Amount of updates");
        yAxis.setLabel("Time (in millis)");

        final LineChart<Number, Number> lineChart = new LineChart(xAxis, yAxis);
        lineChart.setTitle("Time needed to perform database request");
        lineChart.setCreateSymbols(false);

        XYChart.Series series = new XYChart.Series();
        series.setName("Local write, eventual consistent");

        for (int i = 0; i < 100; i++) {
            long start = System.currentTimeMillis();
            subroutineCreate(start);
            long l = System.currentTimeMillis() - start;
            series.getData().add(new XYChart.Data(i, l));
        }

        lineChart.getData().addAll(series);
        VBox vbox = new VBox();
        vbox.getChildren().add(lineChart);

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(vbox);

        Scene scene = new Scene(borderPane, 1000, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void subroutineUpdateScore() throws RemoteException {
        database1.updateScore("username", 3);
        database2.updateScore("username", -3);
        database3.updateScore("username", -3);
        database4.updateScore("username", 3);
    }

    private void subroutineUpdate(long start) throws RemoteException {
        database1.updateUserInfo(createUpdatedUserInfo("username", "salt"));
        database2.updateUserInfo(createUpdatedUserInfo("username", "salty"));
        database3.updateUserInfo(createUpdatedUserInfo("username", "saltyyy"));
        database4.updateUserInfo(createUpdatedUserInfo("username", "pepper"));
    }

    private void subroutineCreate(long start) throws RemoteException {
        database1.createUser(createUserInfo("username1" + start));
        database2.createUser(createUserInfo("username2" + start));
        database3.createUser(createUserInfo("username3" + start));
        database4.createUser(createUserInfo("username4" + start));
    }

    private UserInfo createUserInfo(String s) {
        return new UserInfo(s, "sdfqd", "qdsfqsdf", "sqfqsd");
    }

    private UserInfo createUpdatedUserInfo(String username1, String salt) {
        return new UserInfo(username1, "fsqdfqs", salt, "qsdfqsd");
    }

    private void setup() {
        ServerFactory.createDatabaseServer(1300);
        ServerFactory.createDatabaseServer(1301);
        ServerFactory.createDatabaseServer(1302);
        ServerFactory.createDatabaseServer(1303);

        database1 = lookupDatabaseServerAuthenticator(1300);
        database2 = lookupDatabaseServerAuthenticator(1301);
        database3 = lookupDatabaseServerAuthenticator(1302);
        database4 = lookupDatabaseServerAuthenticator(1303);

        map = new HashMap<>();
        map.put(1300, database1);
        map.put(1301, database2);
        map.put(1302, database3);
        map.put(1303, database4);

        interConnect(map);
    }

    private DatabaseServerInterface lookupDatabaseServerAuthenticator(int portNumber) {
        try {
            return (DatabaseServerInterface)
                    LocateRegistry.getRegistry("localhost", portNumber)
                            .lookup("Authentication");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Not possible to find created databases");
        }
    }


    private void interConnect(Map<Integer, DatabaseServerInterface> map) {
        map.values().forEach(database -> {
            try {
                database.interConnectDatabaseServer(map);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
