package rmi.unogame.applicationServer.service.authentication;

import org.mindrot.jbcrypt.BCrypt;
import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.exceptions.TokenRefreshNeededException;

import java.io.Serializable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class AuthorizationService implements Serializable {

    private List<JWTToken> revokedTokens;

    public AuthorizationService() {
        revokedTokens = new ArrayList<>();
    }

    public boolean verify(JWTToken jwtToken) {
        if (isRevoked(jwtToken)) throw new TokenRefreshNeededException("token got revoked");
        if (tokenIsFresh(jwtToken)) {
            return BCrypt.checkpw(jwtToken.getUserName() + jwtToken.getStartTime(), jwtToken.getHashedChecksum());
        }
        throw new TokenRefreshNeededException("Token was expired, please log in");
    }

    private boolean isRevoked(JWTToken jwtToken) {
        return revokedTokens.stream()
                .anyMatch(token -> token.equals(jwtToken));
    }

    private boolean tokenIsFresh(JWTToken jwtToken) {
        long epochMilli = Instant.now().toEpochMilli();
        long millisForOneDay = Duration.ofDays(1).toMillis();

        return jwtToken.getStartTime() + millisForOneDay > epochMilli;

    }

    public void revokeToken(JWTToken token) {
        this.revokedTokens.add(token);
    }
}
