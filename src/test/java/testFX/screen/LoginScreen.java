package testFX.screen;

import org.junit.Before;
import org.testfx.framework.junit.ApplicationTest;
import rmi.unogame.main.LoginMain;
import testFX.utils.BaseFXTest;

public class LoginScreen extends BaseFXTest {

    private String btn_signUp = "#btn_SignUp";
    private String btn_signIn = "#btn_SignIn";
    private String btn_login= "#btn_Login";
    private String btn_register = "#btn_Register";

    private String loginUsernameTextField = "#loginUsername";
    private String loginPasswordField = "#loginPassword";

    private String registerUsernameTextField = "#registerUsername";
    private String registerPasswordField1 = "#password1";
    private String registerPasswordField2 = "#password2";


    @Override
    @Before
    public void setupClass() throws Exception {
        ApplicationTest.launch(LoginMain.class);
    }

    public void loginUser(String username, String password) {
        clickOn(btn_signIn);
        clickOn(loginUsernameTextField);
        write(username);
        clickOn(loginPasswordField);
        write(password);
        clickOn(btn_login);
    }

    public void registerUser(String username, String password1, String password2) {
        clickOn(btn_signUp);
        clickOn(registerUsernameTextField);
        write(username);
        clickOn(registerPasswordField1);
        write(password1);
        clickOn(registerPasswordField2);
        write(password2);
        clickOn(btn_register);
    }
}
