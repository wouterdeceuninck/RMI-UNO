package rmi.unogame.dispatcher;

public class DispatcherController implements DispatcherInterface {

    private final DispatcherService dispatcherService;

    public DispatcherController(DispatcherService dispatcherService) {
        this.dispatcherService = dispatcherService;
    }

    @Override
    public int requestApplicationServer() {
        return dispatcherService.getApplicationServerPortNumber();
    }

    @Override
    public int getDBPortnumber() {
        return dispatcherService.getDatabaseServerPortNumber();
    }
}
