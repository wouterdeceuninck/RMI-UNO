package rmi.unogame.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import rmi.unogame.applicationServer.service.authentication.UserInfo;
import rmi.unogame.database.mapper.UserInfoMapper;
import rmi.unogame.database.score.ScoreTable;
import rmi.unogame.database.users.UserDatabase;
import rmi.unogame.database.users.dto.DatabaseUserInfo;
import rmi.unogame.exceptions.UsernameAlreadyUsedException;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class DatabaseServer implements DatabaseServerInterface {

    private UserDatabase userDatabase;
    private ScoreTable scoreTable;
    private int portNumber;
    private Map<Integer, DatabaseServerInterface> databaseServerMap;
    private Logger LOGGER = LoggerFactory.getLogger(DatabaseServer.class);


    public DatabaseServer(UserDatabase userDatabase,ScoreTable scoreTable, int portNumber) {
        this.userDatabase = userDatabase;
        this.scoreTable = scoreTable;
        this.portNumber = portNumber;
        this.databaseServerMap = new HashMap<>();
    }

    @Override
    public UserInfo getUserRecord(String username) {
        return UserInfoMapper.toUserInfo(userDatabase.findByName(username));
    }

    @Override
    public void interConnectDatabaseServer(Map<Integer, DatabaseServerInterface> map) throws RemoteException {
        this.databaseServerMap = map;
    }

    @Override
    public boolean updateUserInfo(UserInfo userInfo) throws RemoteException {
        DatabaseUserInfo user = userDatabase.findByName(userInfo.getUsername());
        int ownerId = user.getOwnerId();

        if (ownerId == portNumber) {
            return updatePrimary(userInfo, user);
        } else {
            return sendRequestToPrimary(userInfo, ownerId);
        }
    }

    @Override
    public void forwardUserInfoUpdate(UserInfo userInfo) throws RemoteException {
        userDatabase.updateUserInfo(userInfo);
    }

    @Override
    public boolean createUser(UserInfo userInfo) {
        DatabaseUserInfo databaseUserInfo = new DatabaseUserInfo(userInfo, portNumber);
        if (userDatabase.usernameExists(userInfo.getUsername())) throw new UsernameAlreadyUsedException("Username already used!");
        return userDatabase.createUser(databaseUserInfo) && forwardBlockingCreateUser(databaseUserInfo);
    }

    private boolean forwardBlockingCreateUser(DatabaseUserInfo userInfo) {
        return getOtherDatabaseServers()
                .allMatch(database -> requestForReplicatedUserCreation(userInfo, database));
    }

    @Override
    public boolean forwardUserCreation(DatabaseUserInfo userInfo) {
        LOGGER.info("Database " + portNumber + " got a forward user request");
        if (userDatabase.usernameExists(userInfo.getUsername())) throw new UsernameAlreadyUsedException("Username is already used");
        userDatabase.createUser(userInfo);
        return true;
    }

    @Override
    public void updateScore(String username, Integer delta) {
        scoreTable.updateScore(username, delta);
        Runnable runnable = () -> getOtherDatabaseServers()
                .forEach(database -> singleScoreForward(username, delta, database));
        Executors.newSingleThreadExecutor().submit(runnable);
    }

    @Override
    public void forwardScoreUpdate(String username, Integer detla) throws RemoteException {
        scoreTable.updateScore(username, detla);
    }

    @Override
    public int getUserScore(String username) throws RemoteException {
        return scoreTable.getScoreOnUsername(username);
    }

    @Override
    public Map<String, Integer> getScoreboard() {
        return scoreTable.getFirstLines(20);
    }

    private boolean sendRequestToPrimary(UserInfo userInfo, int ownerId) throws RemoteException {
        LOGGER.info("Database " + portNumber + " Getting an update request on secondary database " + ownerId);
        return databaseServerMap.get(ownerId).updateUserInfo(userInfo);
    }

    private boolean updatePrimary(UserInfo userInfo, DatabaseUserInfo user) {
        LOGGER.info("Database " + portNumber + " Updating a user on the primary: " + user.getOwnerId());
        userDatabase.updateUserInfo(userInfo);
        forwardNotBlockingUpdate(userInfo);
        return userDatabase.updateUserInfo(userInfo);
    }

    private void forwardNotBlockingUpdate(UserInfo userInfo) {
        getOtherDatabaseServers()
                .forEach(database -> updateUser(userInfo, database));
    }

    private void updateUser(UserInfo userInfo, DatabaseServerInterface database) {
        try {
            database.forwardUserInfoUpdate(userInfo);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void singleScoreForward(String username, Integer delta, DatabaseServerInterface database) {
        try {
            database.forwardScoreUpdate(username, delta);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private Stream<DatabaseServerInterface> getOtherDatabaseServers() {
        return databaseServerMap.entrySet()
                .parallelStream()
                .filter(entry -> !entry.getKey().equals(portNumber))
                .map(Map.Entry::getValue);
    }

    private boolean requestForReplicatedUserCreation(DatabaseUserInfo userInfo, DatabaseServerInterface database) {
        try {
            return database.forwardUserCreation(userInfo);
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("Forwarding failed");
        }
    }
}
