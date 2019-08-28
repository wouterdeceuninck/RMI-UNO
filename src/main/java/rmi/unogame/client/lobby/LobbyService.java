package rmi.unogame.client.lobby;

import com.google.common.collect.ImmutableList;
import rmi.unogame.applicationServer.SecureApplicationServerInterface;
import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.applicationServer.game.player.Player;
import rmi.unogame.client.lobby.model.ClientGameInfo;
import rmi.unogame.client.lobby.model.NewGameInfo;
import rmi.unogame.exceptions.ServerNotFoundException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class LobbyService {

    private JWTToken token;
    private List<ClientGameInfo> gameInfo = new ArrayList<>();
    private SecureApplicationServerInterface applicationServer;

    public LobbyService(SecureApplicationServerInterface applicationServer, JWTToken token) {
        this.applicationServer = applicationServer;
        this.token = token;
    }

    public List<String> getGames() {
        List<ClientGameInfo> activeGames = null;
        activeGames = getActiveGames(activeGames);
        gameInfo = ImmutableList.copyOf(activeGames);
        return mapToString(activeGames);
    }

    private List<ClientGameInfo> getActiveGames(List<ClientGameInfo> activeGames) {
        try {
            activeGames = applicationServer.getActiveGames(token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ServerNotFoundException("Server was not found!");
        }
        return activeGames;
    }

    private List<String> mapToString(List<ClientGameInfo> games) {
        return games.stream()
                .map(ClientGameInfo::toString)
                .collect(Collectors.toList());
    }

    public void joinGame(String gameId, Player player) {
        try {
            applicationServer.addPlayerToGame(gameId, player, token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ServerNotFoundException("Server was not found!");
        }
    }

    public Optional<ClientGameInfo> findSelectedGame(String id) {
        return gameInfo.stream().filter(info -> info.getId().equals(id)).findAny();
    }

    public void createGame(NewGameInfo newGameInfo) {
        try {
            applicationServer.createGame(newGameInfo, token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ServerNotFoundException("Server was not found!");
        }
    }

    public String getUsername() {
        return token.getUserName();
    }

    public JWTToken getToken() {
        return token;
    }

    public Map<String, Integer> getScoreboard() {
        try {
            return applicationServer.getScoreboard(token);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new ServerNotFoundException("Server was not found!");
        }
    }
}
