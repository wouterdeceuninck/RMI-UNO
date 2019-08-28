package rmi.unogame.applicationServer.service.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mindrot.jbcrypt.BCrypt;
import rmi.unogame.applicationServer.SecureApplicationServerInterface;
import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.applicationServer.service.authentication.auth.TokenFactory;
import rmi.unogame.client.auth.dto.Credentials;
import rmi.unogame.database.DatabaseServerInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class AuthenticationService implements AuthenticationInterface {

    private final DatabaseServerInterface databaseServerInterface;
    private final AuthorizationService authorizationService;
    private Map<Integer, SecureApplicationServerInterface> appServer;

    public AuthenticationService(DatabaseServerInterface databaseServerInterface) {
        this.databaseServerInterface = databaseServerInterface;
        this.appServer = new HashMap<>();
        authorizationService = new AuthorizationService();
    }

    @Override
    public JWTToken registerUser(Credentials credentials) {

        String gensalt = BCrypt.gensalt();
        JWTToken token = TokenFactory.createToken(credentials, Instant.now().toEpochMilli(), gensalt);
        String hashpw = BCrypt.hashpw(credentials.getPassword(), gensalt);
        UserInfo userInfo = new UserInfo(credentials.getUserName(), hashpw, gensalt, token.toString());

        persistUser(userInfo);
        return token;

    }

    private void persistUser(UserInfo userInfo) {
        try {
            databaseServerInterface.createUser(userInfo);
        } catch (RemoteException e) {
            throw new RuntimeException("database server is no longer found");
        }
    }

    private UserInfo createUserInfo(Credentials credentials, String gensalt, String token) {
        return new UserInfo(credentials.getUserName(), credentials.getPassword(), gensalt, token);
    }

    @Override
    public JWTToken loginUser(Credentials credentials) {
        UserInfo userData = getUserData(credentials);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JWTToken token = objectMapper.readValue(userData.getToken(), JWTToken.class);
            if (authorizationService.verify(token)) revokeToken(token);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(userData.hashedPassword);
        boolean checkpw = BCrypt.checkpw(credentials.getPassword(), userData.hashedPassword);

        if (checkpw) {
            return TokenFactory.createToken(credentials,Instant.now().toEpochMilli(), userData.getSecretSalt());
        } else throw new RuntimeException("Invalid credentials");
    }

    private void revokeToken(JWTToken token) {
        appServer.values()
                .forEach(server -> {
                    try {
                        server.revokeToken(token);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                });
    }

    private UserInfo getUserData(Credentials credentials) {
        try {
            return databaseServerInterface.getUserRecord(credentials.getUserName());
        } catch (RemoteException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get the user from the database");
        }
    }

    public void setApplicationServer(Map<Integer, SecureApplicationServerInterface> appServers) {
        this.appServer = appServers;
    }
}
