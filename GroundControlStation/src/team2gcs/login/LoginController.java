package team2gcs.login;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

public class LoginController implements Initializable{
	@FXML private TextField textIP;
	@FXML private PasswordField textPort;
	@FXML private Button btnConnect;
	@FXML private SplitMenuButton com;
	String mqttAddress;
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		mqttAddress = "tcp://"+textIP.getText()+":"+textPort.getText();
		
	}

	
}