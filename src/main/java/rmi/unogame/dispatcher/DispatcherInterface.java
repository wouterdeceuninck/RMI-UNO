package rmi.unogame.dispatcher;

import rmi.unogame.remoteUtils.RemoteInterface;

public interface DispatcherInterface extends RemoteInterface {

    int requestApplicationServer();

    int getDBPortnumber();
}
