package rmi.unogame.applicationServer.service.authentication;

import rmi.unogame.applicationServer.service.authentication.auth.JWTToken;
import rmi.unogame.client.auth.dto.Credentials;

import java.io.Serializable;

public interface AuthenticationInterface extends Serializable {
    JWTToken registerUser(Credentials credentials);

    JWTToken loginUser(Credentials credentials);
}
