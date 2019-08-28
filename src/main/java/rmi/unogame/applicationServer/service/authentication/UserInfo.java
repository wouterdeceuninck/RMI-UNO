package rmi.unogame.applicationServer.service.authentication;

import java.io.Serializable;

public class UserInfo implements Serializable {
    protected String username;
    protected String hashedPassword;
    protected String secretSalt;
    protected String token;

    public UserInfo(String username, String hashedPassword, String secretSalt, String token) {
        this.username = username;
        this.hashedPassword = hashedPassword;
        this.secretSalt = secretSalt;
        this.token = token;
    }

    public UserInfo() {
    }

    public UserInfo(UserInfo userInfo) {
        this.username = userInfo.getUsername();
        this.hashedPassword = userInfo.getHashedPassword();
        this.secretSalt = userInfo.getSecretSalt();
        this.token = userInfo.getToken();
    }

    public String getUsername() {
        return username;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public String getSecretSalt() {
        return secretSalt;
    }

    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setSecretSalt(String secretSalt) {
        this.secretSalt = secretSalt;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
