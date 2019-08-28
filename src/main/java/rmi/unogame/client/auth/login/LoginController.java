package rmi.unogame.client.auth.login;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import rmi.unogame.exceptions.InvalidInputException;
import rmi.unogame.exceptions.ServerNotFoundException;
import rmi.unogame.exceptions.UsernameAlreadyUsedException;

import java.io.File;
import java.net.URL;

public class LoginController {

	private final LoginService loginService;
	private String FXML_RESOURCE_PATH = "src/main/java/rmi/unogame/client/lobby/Lobby.fxml";

	@FXML
	private Button btn_Login;

	@FXML
	private Pane pn_Login, pn_Register;

	@FXML
	public TextField registerUsername, loginUsername;

	@FXML
	public PasswordField password1, password2, loginPassword;

	@FXML
	private void SignUpUp() {
		pn_Login.setVisible(false);
		pn_Register.setVisible(true);
	}

	@FXML
	private void SignInUp() {
		pn_Login.setVisible(true);
		pn_Register.setVisible(false);
	}

	public LoginController() {
		loginService = new LoginService();
	}

	@FXML
    private void Register() {
		try {
			loginService.registerUser(registerUsername.getText(), password1.getText(), password2.getText());
			startLobby();
		} catch (InvalidInputException | ServerNotFoundException e) {
			popUpAlert(e.getMessage());
		} catch (UsernameAlreadyUsedException usernameAlreadyUsed) {
			popUpAlert("The username is already used!");
		}
	}

	@FXML
	private void login() {
		try {
			loginService.loginUser(loginUsername.getText(), loginPassword.getText());
			startLobby();
		} catch (InvalidInputException | ServerNotFoundException e) {
			popUpAlert(e.getMessage());
		} catch (UsernameAlreadyUsedException usernameAlreadyUsed) {
			popUpAlert("The username is already used!");
		}
	}

	private void closeWindow() {
		Stage stage = (Stage) btn_Login.getScene().getWindow();
		stage.close();
	}

	private void popUpAlert(String string) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText("Error!");
		errorAlert.setContentText(string);
		errorAlert.showAndWait();
	}

	private void startLobby() {
		try {
			URL url = new File(FXML_RESOURCE_PATH).toURI().toURL();
			FXMLLoader fxmlLoader = new FXMLLoader(url);
			fxmlLoader.setController(loginService.createLobbyController());
			Parent root1 = fxmlLoader.load();

			Stage stage = createStage(root1);
			stage.show();
		} catch (Exception e) {
			popUpAlert("something went wrong");
		}
		closeWindow();
	}

	private Stage createStage(Parent root1) {
		Stage stage = new Stage();
		stage.setTitle("Lobby");
		stage.setScene(new Scene(root1));
		return stage;
	}
}
