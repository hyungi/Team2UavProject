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
	public static int alt;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnTakeoff.setOnAction((event) -> {handleTakeoff(event);});
	}
	
	public void handleTakeoff(ActionEvent event) {
		alt = Integer.valueOf(txtAlt.getText());
		Network.getUav().takeoff(alt);
		AppMainController.instance2.statusMessage("UAV Takeoff.");
		AppMainController.takeoffStart = true;
		AppMainController.altStage.close();
	}
}
