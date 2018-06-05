package team2gcs.altdialog;

import java.net.URL;
import java.util.ResourceBundle;

import gcs.network.Network;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import team2gcs.appmain.AppMainController;

public class altdialogController implements Initializable {
	@FXML private TextField txtAlt;
	@FXML private Button btnTakeoff;
	@FXML private Button btnExit;
	public static int alt;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if(Network.getUav().altitude > 1.5) {
			btnTakeoff.setText("Change");
		}
		else {
			btnTakeoff.setText("Take off");
		}
		btnTakeoff.setOnAction((event) -> {handleTakeoff(event);});
		btnExit.setOnAction((event)-> {AppMainController.altStage.close();});
	}
	
	public void handleTakeoff(ActionEvent event) {
		if(Network.getUav().altitude > 1.5) {
			alt = Integer.valueOf(txtAlt.getText());
			Network.getUav().changeAlt(alt);
			AppMainController.instance2.statusMessage("Altitude " + alt + "M.");
			AppMainController.altStage.close();
		} else if(Network.getUav().altitude < 1.5) {
			alt = Integer.valueOf(txtAlt.getText());
			Network.getUav().takeoff(alt);
			AppMainController.instance2.statusMessage("UAV Takeoff.");
			AppMainController.takeoffStart = true;
			AppMainController.altStage.close();
		}
	}
}
