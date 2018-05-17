package team2gcs.login;

import java.net.URL;
import java.util.ResourceBundle;

import gcs.network.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.SplitMenuButton;
import javafx.scene.control.TextField;

public class LoginController implements Initializable{
	@FXML private TextField txtIP;
	@FXML private TextField txtPort;
	@FXML private Button btnConnect;
	@FXML private Button btnCancle;
	@FXML private SplitMenuButton com;
	public static String ip;
	public static String port;
	boolean connectState=false;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initConnect();
		initLoginButton();
	}
	
	public void initConnect() {
		ip=txtIP.getText();
		port=txtPort.getText();
	}
	
	public void initLoginButton() {
		btnConnect.setOnAction((event)->{handleConnect(event);});
		btnCancle.setOnAction((event)->{handleCancle(event);});
	}
	
	public void handleConnect(ActionEvent event) {
		System.out.println("1");
		Network.connect();
		
	}
	
	public void handleCancle(ActionEvent event) {
		System.exit(0);
	}


	
}