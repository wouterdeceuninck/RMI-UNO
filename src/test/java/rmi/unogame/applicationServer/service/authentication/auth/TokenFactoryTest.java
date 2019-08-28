package rmi.unogame.applicationServer.service.authentication.auth;

import org.junit.Assert;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import rmi.unogame.client.auth.dto.Credentials;

import java.time.Instant;

public class TokenFactoryTest {

    @Test
    public void createToken() {
        String gensalt = BCrypt.gensalt();
        Credentials credentials = new Credentials("username", "password");

        JWTToken token = TokenFactory.createToken(
                credentials,
                Instant.now().toEpochMilli(),
                gensalt);

        Assert.assertEquals(token.getUserName(), "username");
    }
}