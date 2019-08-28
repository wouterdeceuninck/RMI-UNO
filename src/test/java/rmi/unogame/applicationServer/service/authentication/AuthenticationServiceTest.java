package rmi.unogame.applicationServer.service.authentication;

import org.junit.Assert;
import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mockito;
import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.applicationServer.service.authentication.auth.TokenFactory;
import rmi.unogame.client.auth.dto.Credentials;
import rmi.unogame.database.DatabaseServer;
import rmi.unogame.database.DatabaseServerInterface;

import java.rmi.RemoteException;
import java.time.Instant;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class AuthenticationServiceTest {

    private DatabaseServerInterface persistInterface = Mockito.mock(DatabaseServer.class);
    private AuthenticationService authenticationService;

    @Test
    public void registerANewUser() throws RemoteException {
        when(persistInterface.createUser(any())).thenReturn(true);
        authenticationService = new AuthenticationService(persistInterface);
        Credentials userInfo = new Credentials("username", "password");
        JWTToken token = authenticationService.registerUser(userInfo);

        Assert.assertEquals(token.getUserName(), "username");
    }

    @Test
    public void loginUser_expectCorrectCredentials() throws RemoteException {
        when(persistInterface.createUser(any())).thenReturn(true);
        String gensalt = BCrypt.gensalt();
        String hashpw = BCrypt.hashpw("password", gensalt);

        JWTToken token1 = TokenFactory.createToken(new Credentials("username", "password"), Instant.now().toEpochMilli(), gensalt);
        when(persistInterface.getUserRecord(any())).thenReturn(new UserInfo("username", hashpw, gensalt, token1.toString()));

        authenticationService = new AuthenticationService(persistInterface);
        authenticationService.setApplicationServer(Collections.emptyMap());

        JWTToken token = authenticationService.loginUser(new Credentials("username", "password"));

        Assert.assertEquals(token.getUserName(), "username");
    }

}