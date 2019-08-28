package rmi.unogame.applicationServer;

import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.applicationServer.game.player.Player;
import rmi.unogame.client.auth.dto.Credentials;
import rmi.unogame.client.lobby.model.ClientGameInfo;
import rmi.unogame.client.lobby.model.NewGameInfo;
import rmi.unogame.remoteUtils.RemoteInterface;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface SecureApplicationServerInterface extends RemoteInterface {

    void createGame(NewGameInfo gameInfo, JWTToken token)throws RemoteException;

    void addPlayerToGame(String id, Player player, JWTToken token)throws RemoteException;

    List<ClientGameInfo> getActiveGames(JWTToken token)throws RemoteException;

    JWTToken registerUser(Credentials credentials)throws RemoteException;

    JWTToken loginUser(Credentials credentials)throws RemoteException;

    Map<String, Integer> getScoreboard(JWTToken token)throws RemoteException;

    void interConnectApplicationServer(Map<Integer, SecureApplicationServerInterface> appServers)throws RemoteException;

    void revokeToken(JWTToken token)throws RemoteException;
}
