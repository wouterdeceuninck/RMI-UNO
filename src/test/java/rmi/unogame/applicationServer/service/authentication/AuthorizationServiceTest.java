package rmi.unogame.applicationServer.service.authentication;

import org.junit.Assert;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.applicationServer.service.authentication.auth.TokenFactory;
import rmi.unogame.client.auth.dto.Credentials;
import rmi.unogame.exceptions.TokenRefreshNeededException;

import java.time.Duration;
import java.time.Instant;

public class AuthorizationServiceTest {

    private final AuthorizationService authorizationService = new AuthorizationService();

    @Test
    public void validateToken() {
        Credentials credentials = new Credentials("username", "password");
        JWTToken token = TokenFactory.createToken(credentials, Instant.now().toEpochMilli(), BCrypt.gensalt());

        Assert.assertTrue(authorizationService.verify(token));
    }

    @Test(expected = TokenRefreshNeededException.class)
    public void validateToken_RefreshNeeded() {
        Credentials credentials = new Credentials("username", "password");
        long timeInMillis = Instant.now().minusMillis(Duration.ofDays(2).toMillis()).toEpochMilli();

        JWTToken token = TokenFactory.createToken(credentials, timeInMillis, BCrypt.gensalt());

        Assert.assertTrue(authorizationService.verify(token));
    }

    @Test(expected = TokenRefreshNeededException.class)
    public void revokedToken() {
        String gensalt = BCrypt.gensalt();
        Credentials credentials = new Credentials("username", "password");
        long timeInMillis = Instant.now().toEpochMilli();
        JWTToken token1 = TokenFactory.createToken(credentials, timeInMillis, gensalt);
        JWTToken token2 = TokenFactory.createToken(credentials, timeInMillis, gensalt);
        authorizationService.revokeToken(token1);
        boolean verify = authorizationService.verify(token2);
    }
}