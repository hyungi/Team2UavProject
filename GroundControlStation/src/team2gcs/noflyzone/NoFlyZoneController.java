package team2gcs.noflyzone;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import netscape.javascript.JSObject;
import team2gcs.appmain.AppMainController;

public class NoFlyZoneController implements Initializable{

	@FXML private Button btnOk;
	@FXML private TextField txtNFZLat;
	@FXML private TextField txtNFZLng;
	@FXML private TextField txtNFZRadius;
	private String circleX1;
	private String circleY1;
	private String circleRadius;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initTextField();
		initButton();
	}
	public void initTextField() {
		circleX1 = txtNFZLat.getText();
		circleY1 = txtNFZLng.getText();
		circleRadius = txtNFZRadius.getText();
	}
	public void initButton() {
		btnOk.setOnAction((event)->{handleOk(event);});
	}
	public void handleOk(ActionEvent event) {
		Platform.runLater(() -> {
			try {
				double x = Double.parseDouble(circleX1);
				double y = Double.parseDouble(circleY1);
				double r = Double.parseDouble(circleRadius);
				AppMainController.instance2.jsproxy.call("makeNoFlyZone",x,y,r);
				}catch(Exception e) {}			
		});
	}
	
}