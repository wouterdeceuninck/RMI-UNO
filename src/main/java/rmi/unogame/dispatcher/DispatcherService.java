package rmi.unogame.dispatcher;

import rmi.unogame.applicationServer.SecureApplicationServerInterface;
import rmi.unogame.config.PortNumberConfig;
import rmi.unogame.database.DatabaseServerInterface;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class DispatcherService implements Serializable {

    Map<Integer, DatabaseServerInterface> databases;
    Map<Integer, SecureApplicationServerInterface> appServers;

    public DispatcherService() {
        databases = new HashMap<>();
        appServers = new HashMap<>();

        startDatabaseServers();
        interconnectDatabases();
        startApplicationServers();
        interconnectApplicationServers();
    }

    private void interconnectApplicationServers() {
        appServers.values()
                .forEach(database -> {
                    try {
                        database.interConnectApplicationServer(appServers);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
    }

    private void interconnectDatabases() {
        databases.values()
                .forEach(database -> {
                    try {
                        database.interConnectDatabaseServer(databases);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
    }

    public int getApplicationServerPortNumber() {
        return PortNumberConfig.SERVER_PORT_NUMBER.getProperty();
    }

    public int getDatabaseServerPortNumber() {
        return getRandom(databases.keySet());
    }

    private void startDatabaseServers() {
        for (int i = 0; i < 4; i++) {
            int portNumber = PortNumberConfig.DATABASE_PORT_NUMBER.getProperty() + i;
            ServerFactory.createDatabaseServer(portNumber);
            databases.put(portNumber, lookupDatabaseServerAuthenticator(portNumber));
        }
    }

    private void startApplicationServers() {
        for (int i = 0; i < 4; i++) {
            int serverPortnumber = PortNumberConfig.SERVER_PORT_NUMBER.getProperty() + i;
            int databasePortnumber = PortNumberConfig.DATABASE_PORT_NUMBER.getProperty() + i;

            ServerFactory.createApplicationServer(serverPortnumber, databasePortnumber);
            appServers.put(serverPortnumber, lookupApplicationServer(serverPortnumber));
        }
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

    private SecureApplicationServerInterface lookupApplicationServer(int serverPortNumber) {
        try {
            return (SecureApplicationServerInterface)
                    LocateRegistry.getRegistry("localhost", serverPortNumber)
                            .lookup("UNOserver");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Not possible to find created databases");
        }
    }

    private int getRandom(Set<Integer> portNumbers) {
        ArrayList<Integer> list = new ArrayList<>(portNumbers);
        Collections.shuffle(list);
        return list.get(0);
    }
}
