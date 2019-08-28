package rmi.unogame.database;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import rmi.unogame.applicationServer.service.authentication.UserInfo;
import rmi.unogame.dispatcher.ServerFactory;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class DatabaseServerTest {

    private DatabaseServerInterface database4;
    private DatabaseServerInterface database3;
    private DatabaseServerInterface database1;
    private DatabaseServerInterface database2;
    private Map<Integer, DatabaseServerInterface> map;

    @Before
    public void setUp() throws Exception {
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

    @Test
    public void userRequests() throws RemoteException {
        long epochMilli = Instant.now().toEpochMilli();

        database1.createUser(new UserInfo(String.valueOf(epochMilli), "hashedpw", "salt", "tailer"));

        UserInfo userRecord4 = database4.getUserRecord(String.valueOf(epochMilli));
        Assert.assertEquals(String.valueOf(epochMilli), userRecord4.getUsername());


        UserInfo info = new UserInfo(String.valueOf(epochMilli), "hashedpw", "salt_2.0", "tailer");

        database2.updateUserInfo(info);
        Assert.assertEquals("salt_2.0", database4.getUserRecord(String.valueOf(epochMilli)).getSecretSalt());

    }

    @Test
    public void scoreRequests() throws RemoteException, InterruptedException {
        long currentTimeMillis = System.currentTimeMillis();
        database1.updateScore("testuser" + currentTimeMillis, 3);
        database2.updateScore("testuser" + currentTimeMillis, 3);
        database3.updateScore("testuser" + currentTimeMillis, 3);
        database4.updateScore("testuser" + currentTimeMillis, 3);
        System.out.println(System.currentTimeMillis() - currentTimeMillis);

        //because of eventuality of updates, we have to wait for them to finish
        Thread.sleep(100L);
        Assert.assertEquals(12, database1.getUserScore("testuser" + currentTimeMillis));
    }

    @Test
    public void updateUser() throws RemoteException {
        long currentTimeMillis = System.currentTimeMillis();
        database1.createUser(new UserInfo("username" + currentTimeMillis, "pass", "salt", "tailerToken"));
        long currentTimeMillis2 = System.currentTimeMillis();
        database1.updateUserInfo(new UserInfo("username" + currentTimeMillis, "password", "salt", "tailerToken"));
        database2.updateUserInfo(new UserInfo("username" + currentTimeMillis, "passwordss", "saltss", "tailerToken"));
        database3.updateUserInfo(new UserInfo("username" + currentTimeMillis, "password", "salt", "tailerTokensss"));
        database4.updateUserInfo(new UserInfo("username" + currentTimeMillis, "password", "expected", "expected"));
        System.out.println(System.currentTimeMillis() - currentTimeMillis2);

        Assert.assertEquals("password", database1.getUserRecord("username" + currentTimeMillis).getHashedPassword());
        Assert.assertEquals("expected", database1.getUserRecord("username" + currentTimeMillis).getSecretSalt());
        Assert.assertEquals("expected", database1.getUserRecord("username" + currentTimeMillis).getToken());
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

}