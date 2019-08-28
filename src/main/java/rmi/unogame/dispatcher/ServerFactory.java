package rmi.unogame.dispatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import rmi.unogame.applicationServer.SecureApplicationServerInterface;
import rmi.unogame.applicationServer.SecureApplicationService;
import rmi.unogame.database.DatabaseServer;
import rmi.unogame.database.DatabaseServerInterface;
import rmi.unogame.database.score.ScoreTable;
import rmi.unogame.database.users.UserDatabase;
import rmi.unogame.database.users.UserTable;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerFactory {
    public static void createApplicationServer(int serverPortnumber, int databasePortnumber) {
        try {
            SecureApplicationServerInterface server = new SecureApplicationService(databasePortnumber);
            Registry registry = LocateRegistry.createRegistry(serverPortnumber);
            registry.rebind("UNOserver", server);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void createDatabaseServer(int databasePortnumber) {
        try {
            UserDatabase userDatabase = createUserDatabase(databasePortnumber);
            ScoreTable scoreTable = createScoreDatabase(databasePortnumber);
            DatabaseServerInterface databaseServerInterface = new DatabaseServer(userDatabase, scoreTable, databasePortnumber);

            Registry registry = LocateRegistry.createRegistry(databasePortnumber);
            registry.rebind("Authentication", databaseServerInterface);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static ScoreTable createScoreDatabase(int databasePortnumber) {
        return new ScoreTable(new ObjectMapper(), databasePortnumber);
    }

    private static UserDatabase createUserDatabase(int databasePortnumber) {
        UserTable userTable = new UserTable(new ObjectMapper(), databasePortnumber);
        return new UserDatabase(userTable);
    }

    public static void createDispatcherServer(int dispatcherPortNumber) {
        try {
            DispatcherInterface dispatcher = new DispatcherController(new DispatcherService());
            Registry registry = LocateRegistry.createRegistry(dispatcherPortNumber);
            registry.rebind("Dispatcher", dispatcher);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


}
