package rmi.unogame.database.users.dto;

import rmi.unogame.applicationServer.service.authentication.UserInfo;

public class DatabaseUserInfo extends UserInfo {
    private int ownerId;

    public DatabaseUserInfo(String username, String hashedPassword, String secretSalt, String tokenTailer, int ownerId) {
        super(username, hashedPassword, secretSalt, tokenTailer);
        this.ownerId = ownerId;
    }

    public DatabaseUserInfo() {
    }

    public DatabaseUserInfo(UserInfo userInfo, int ownerId) {
        super(userInfo);
        this.ownerId = ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void update(UserInfo userInfo) {
        this.username = userInfo.getUsername();
        this.hashedPassword = userInfo.getHashedPassword();
        this.secretSalt = userInfo.getSecretSalt();
        this.token = userInfo.getToken();
    }
}
