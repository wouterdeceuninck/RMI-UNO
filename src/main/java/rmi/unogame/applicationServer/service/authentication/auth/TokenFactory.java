package rmi.unogame.applicationServer.service.authentication.auth;

import org.mindrot.jbcrypt.BCrypt;
import rmi.unogame.client.auth.dto.Credentials;

public class TokenFactory {

    public static JWTToken createToken(Credentials credentials,Long timeInMillis, String secretSalt) {
        String userName = credentials.getUserName();
        String validationTailer = createValidationTailer(secretSalt, userName, timeInMillis);

        return new JWTToken(userName, timeInMillis, validationTailer);
    }

    private static String createValidationTailer(String secretSalt, String userName, long millis) {
        return BCrypt.hashpw(userName + millis, secretSalt);
    }


}
