package rmi.unogame.applicationServer;

import rmi.unogame.applicationServer.game.ApplicationPlayer;
import rmi.unogame.applicationServer.game.dtoObjects.UnoGameInfo;
import rmi.unogame.applicationServer.game.player.Player;
import rmi.unogame.applicationServer.service.authentication.AuthenticationService;
import rmi.unogame.applicationServer.service.authentication.AuthorizationService;
import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.applicationServer.service.game.GameService;
import rmi.unogame.client.auth.dto.Credentials;
import rmi.unogame.client.lobby.model.ClientGameInfo;
import rmi.unogame.client.lobby.model.NewGameInfo;
import rmi.unogame.database.DatabaseServerInterface;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SecureApplicationService implements SecureApplicationServerInterface {

    private final AuthorizationService authorizationService;
    private final AuthenticationService authenticationService;
    private final GameService gameService;

    private final DatabaseServerInterface databaseServer;
    private Map<Integer, SecureApplicationServerInterface> appServers;

    public SecureApplicationService(int databasePortNumber) {
        this.databaseServer = SecureApplicationService.this.lookupDatabase(databasePortNumber);
        this.gameService = new GameService(SecureApplicationService.this.databaseServer);
        this.authenticationService = new AuthenticationService(SecureApplicationService.this.databaseServer);
        this.authorizationService = new AuthorizationService();
    }

    @Override
    public void createGame(NewGameInfo gameInfo, JWTToken token) {
        verifyToken(token);
        createGame(gameInfo);
    }

    @Override
    public void addPlayerToGame(String id, Player player, JWTToken token) {
        verifyToken(token);
        addPlayerToGame(id, new ApplicationPlayer(player, token.getUserName()));
    }

    @Override
    public List<ClientGameInfo> getActiveGames(JWTToken token) {
        verifyToken(token);
        return getActiveGames();
    }

    public JWTToken registerUser(Credentials credentials) {
        return authenticationService.registerUser(credentials);
    }

    public JWTToken loginUser(Credentials credentials) {
        return authenticationService.loginUser(credentials);
    }

    @Override
    public Map<String, Integer> getScoreboard(JWTToken token) {
        verifyToken(token);
        return getScoreboard();
    }

    @Override
    public void interConnectApplicationServer(Map<Integer, SecureApplicationServerInterface> appServers) {
        addMap(appServers);
    }

    @Override
    public void revokeToken(JWTToken token) {
        authorizationService.revokeToken(token);
    }

    private void verifyToken(JWTToken token) {
        authorizationService.verify(token);
    }

    private DatabaseServerInterface lookupDatabase(int databasePortNumber) {
        try {
            return (DatabaseServerInterface)
                    LocateRegistry.getRegistry("localhost", databasePortNumber)
                    .lookup("Authentication");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Database not found!");
        }
    }

    public void createGame(NewGameInfo gameInfo) {
        gameService.createGame(gameInfo);
    }

    public void addPlayerToGame(String gameId, ApplicationPlayer player) {
        gameService.addPlayerToGame(gameId, player);
    }

    public List<ClientGameInfo> getActiveGames() {
        return gameService.getActiveGames().stream()
                .map(this::mapToClientGameInfo)
                .collect(Collectors.toList());
    }

    private ClientGameInfo mapToClientGameInfo(UnoGameInfo unoGameInfo) {
        return new ClientGameInfo(unoGameInfo.getId()
                , unoGameInfo.getGameName()
                , unoGameInfo.getTheme()
                , unoGameInfo.getJoinedPlayers()
                , unoGameInfo.getMaxAmountOfPlayers());
    }

    protected Map<String, Integer> getScoreboard() {
        return databaseServer.getScoreboard();
    }

    protected void addMap(Map<Integer, SecureApplicationServerInterface> appServers) {
        this.appServers = appServers;
        this.authenticationService.setApplicationServer(appServers);
    }
}
