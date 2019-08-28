package rmi.unogame.client.auth.login;

import rmi.unogame.applicationServer.SecureApplicationServerInterface;
import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.client.auth.dto.Credentials;
import rmi.unogame.client.lobby.LobbyController;
import rmi.unogame.client.lobby.LobbyService;
import rmi.unogame.config.PortNumberConfig;
import rmi.unogame.dispatcher.DispatcherInterface;
import rmi.unogame.exceptions.InvalidInputException;
import rmi.unogame.exceptions.ServerNotFoundException;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class LoginService {
    private final int appServerPortNumber;
    private SecureApplicationServerInterface server = null;
    private JWTToken token;

    public LoginService() {
        DispatcherInterface dispatcherInterface = lookupDispatcherServer();
        appServerPortNumber = dispatcherInterface.requestApplicationServer();
        System.out.println(appServerPortNumber);
    }

    private DispatcherInterface lookupDispatcherServer() {
        try {
            return (DispatcherInterface) LocateRegistry
                    .getRegistry("localhost", PortNumberConfig.DISPATCHER_PORT_NUMBER.getProperty())
                    .lookup("Dispatcher");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Can not find dispatcher!");
        }
    }

    private SecureApplicationServerInterface getApplicationServer() {
        if (this.server == null) {
            try {
                this.server = lookupApplicationServer(appServerPortNumber);
            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
                throw new ServerNotFoundException("Server was not found!");
            }
        }
        return server;
    }

    private SecureApplicationServerInterface lookupApplicationServer(int portNumber) throws RemoteException, NotBoundException {
        return (SecureApplicationServerInterface) LocateRegistry
                .getRegistry("localhost", portNumber)
                .lookup("UNOserver");
    }

    public void loginUser(String username, String password) {
        try {
            token = getApplicationServer().loginUser(new Credentials(username, password));
        } catch (RemoteException e) {
            throw new ServerNotFoundException("Server was not found!");
        }
    }

    public void registerUser(String username, String password1, String password2) {
        validateInput(password1, password2);
        Credentials credentials = new Credentials(username, password1);
        try {
            token = getApplicationServer().registerUser(credentials);
        } catch (RemoteException e) {
            throw new ServerNotFoundException("Server was not found!");
        }
    }

    private void validateInput(String password1, String password2) {
        if (!password1.equals(password2)) throw new InvalidInputException("The passwords must match!");
    }

    public LobbyController createLobbyController() {
        LobbyService lobbyService = new LobbyService(server, token);
        return new LobbyController(lobbyService);
    }
}
