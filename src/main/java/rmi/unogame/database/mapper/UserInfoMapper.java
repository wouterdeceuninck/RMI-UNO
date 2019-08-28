package rmi.unogame.database.mapper;

import rmi.unogame.applicationServer.service.authentication.UserInfo;
import rmi.unogame.database.users.dto.DatabaseUserInfo;

public class UserInfoMapper {

    public static UserInfo toUserInfo(DatabaseUserInfo databaseUserInfo) {
        return new UserInfo(databaseUserInfo.getUsername(),
                databaseUserInfo.getHashedPassword(),
                databaseUserInfo.getSecretSalt(),
                databaseUserInfo.getToken());
    }
}
