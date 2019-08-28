package rmi.unogame.dispatcher;

import org.junit.Assert;
import org.junit.Test;

public class DispatcherServiceTest {

    @Test
    public void createService() {
        DispatcherService dispatcherService = new DispatcherService();
        int applicationServerPortNumber = dispatcherService.getApplicationServerPortNumber();
        int databaseServerPortNumber = dispatcherService.getDatabaseServerPortNumber();

        Assert.assertTrue(1200 <= applicationServerPortNumber && applicationServerPortNumber <= 1204);
        Assert.assertTrue(1300 <= databaseServerPortNumber && databaseServerPortNumber <= 1304);
    }
}