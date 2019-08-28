package rmi.unogame.database.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import rmi.unogame.applicationServer.service.authentication.UserInfo;
import rmi.unogame.database.users.dto.DatabaseUserInfo;

import java.time.Instant;

public class UserTableTest {

    @Test
    public void createTable() {
        UserTable userTable = new UserTable(new ObjectMapper(), 1300);
    }

    @Test
    public void writeToTable_createNewUsers() {
        UserTable userTable = new UserTable(new ObjectMapper(), 1300);
        long epochMilli = Instant.now().toEpochMilli();
        userTable.createUser(new DatabaseUserInfo("username" + epochMilli, "hashedPassword", "secretSalt", "token", 1300));
        userTable.createUser(new DatabaseUserInfo("username2"+ epochMilli, "hashedPassword2", "secretSalt2", "tokenTailer2", 1300));
    }

    @Test
    public void writeToTableAndUpdate() {
        UserTable userTable = new UserTable(new ObjectMapper(), 1300);
        long epochMilli = Instant.now().toEpochMilli();
        userTable.createUser(new DatabaseUserInfo("username" + epochMilli, "hashedPassword", "secretSalt", "token", 1300));
        String updatedToken = "updatedToken";
        userTable.persistUser(new UserInfo("username" + epochMilli, "hashedPassword", "secretSalt", updatedToken));

        Assert.assertEquals(updatedToken, userTable.findByName("username" + epochMilli).get().getToken());
    }
}