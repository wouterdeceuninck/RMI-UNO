package rmi.unogame.client.service;

import org.junit.Ignore;
import org.junit.Test;
import rmi.unogame.client.auth.login.LoginService;
import rmi.unogame.exceptions.InvalidInputException;

@Ignore
public class LoginServiceTest {

    @Test(expected = InvalidInputException.class)
    public void registerNewUser_expectInvalidInput() {
        LoginService loginService = new LoginService();
        loginService.registerUser("username", "password", "passwordssss");
    }
}