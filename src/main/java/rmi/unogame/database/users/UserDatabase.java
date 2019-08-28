package rmi.unogame.database.users;

import rmi.unogame.applicationServer.service.authentication.UserInfo;
import rmi.unogame.database.users.dto.DatabaseUserInfo;

import java.io.Serializable;
import java.util.Optional;

public class UserDatabase implements Serializable {

    private final UserTable table;

    public UserDatabase(UserTable table) {
        this.table = table;
    }

    public DatabaseUserInfo findByName(String username) {
        Optional<DatabaseUserInfo> selectedUser = table.findByName(username);
        if (selectedUser.isPresent()) return selectedUser.get();
        else throw new RuntimeException("User was not found!");
    }

    public boolean updateUserInfo(UserInfo userInfo) {
        table.persistUser(userInfo);
        return false;
    }

    public boolean createUser(DatabaseUserInfo userInfo) {
        table.createUser(userInfo);
        return true;
    }

    public boolean usernameExists(String username) {
        Optional<DatabaseUserInfo> byName = table.findByName(username);
        return byName.isPresent();
    }

}
