package rmi.unogame.database;

import rmi.unogame.applicationServer.service.authentication.UserInfo;
import rmi.unogame.database.users.dto.DatabaseUserInfo;
import rmi.unogame.remoteUtils.RemoteInterface;

import java.rmi.RemoteException;
import java.util.Map;

public interface DatabaseServerInterface extends RemoteInterface {

    UserInfo getUserRecord(String username) throws RemoteException;

    void interConnectDatabaseServer(Map<Integer, DatabaseServerInterface> map) throws RemoteException;

    boolean updateUserInfo(UserInfo userInfo) throws RemoteException;

    void forwardUserInfoUpdate(UserInfo userInfo) throws RemoteException;

    boolean createUser(UserInfo userInfo) throws RemoteException;

    boolean forwardUserCreation(DatabaseUserInfo userInfo) throws RemoteException;

    void updateScore(String username, Integer delta) throws RemoteException;

    void forwardScoreUpdate(String username, Integer detla) throws RemoteException;

    int getUserScore(String username)throws RemoteException;

    Map<String, Integer> getScoreboard();
}
