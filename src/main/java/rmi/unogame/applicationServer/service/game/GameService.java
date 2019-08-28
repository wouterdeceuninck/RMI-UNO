package rmi.unogame.applicationServer.service.game;

import rmi.unogame.applicationServer.game.ApplicationPlayer;
import rmi.unogame.applicationServer.game.UnoGame;
import rmi.unogame.applicationServer.game.dtoObjects.UnoGameInfo;
import rmi.unogame.applicationServer.game.dtoObjects.mapper.UnoGameInfoMapper;
import rmi.unogame.applicationServer.game.player.Player;
import rmi.unogame.client.lobby.model.NewGameInfo;
import rmi.unogame.database.DatabaseServerInterface;
import rmi.unogame.exceptions.UnoGameNotFoundException;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class GameService implements GameServiceInterface {

    private final List<UnoGame> activeGames;
    private DatabaseServerInterface databaseServer;

    public GameService(DatabaseServerInterface databaseServer) {
        this.activeGames = new ArrayList<>();
        this.databaseServer = databaseServer;
    }

    @Override
    public void createGame(NewGameInfo gameInfo) {
        activeGames.add(new UnoGame(UnoGameInfoMapper.fromNewGameInfo(gameInfo)));
    }

    @Override
    public void addPlayerToGame(String id, ApplicationPlayer player) {
        Optional<UnoGame> optSelectedGame = findGameOnId(id);

        if (optSelectedGame.isPresent()) {
            addPlayerAndStartWhenFull(player, optSelectedGame.get());
        } else {
            throw new UnoGameNotFoundException("Selected game not found! " + id);
        }
    }

    @Override
    public List<UnoGameInfo> getActiveGames() {
        return activeGames.stream()
                .map(UnoGame::getUnoGameInfo)
                .peek(unoGameInfo -> System.out.println(unoGameInfo.getId()))
                .collect(Collectors.toList());
    }

    private Optional<UnoGame> findGameOnId(String id) {
        return activeGames.stream()
                .filter(unoGame -> unoGame.getUnoGameInfo().getId().equals(id))
                .findFirst();
    }

    private void addPlayerAndStartWhenFull(ApplicationPlayer player, UnoGame selectedGame) {
        selectedGame.addPlayer(player);
        if (gameIsFull(selectedGame)) {
            startGame(selectedGame);
        }
    }

    private void startGame(UnoGame selectedGame) {
        for (int i = 0; i < selectedGame.getJoinedPlayers(); i++) {
            selectedGame.startGame();
        }
        updateScore(selectedGame);
    }

    private void updateScore(UnoGame selectedGame) {
        selectedGame.getEndScore().entrySet()
                .parallelStream()
                .forEach(this::persistScore);

    }

    private void persistScore(Map.Entry<String, Integer> entry) {
        try {
            databaseServer.updateScore(entry.getKey(), entry.getValue());
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("Lost connection to the database server");
        }
    }

    private boolean gameIsFull(UnoGame unoGame) {
        return unoGame.getJoinedPlayers() == unoGame.getUnoGameInfo().getMaxAmountOfPlayers();
    }
}
