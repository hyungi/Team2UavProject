package team2gcs.noflyzone;

import java.net.URL;
import java.util.ResourceBundle;

import org.json.JSONObject;

import gcs.network.Network;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import team2gcs.appmain.AppMainController;

public class NoFlyZoneController implements Initializable{

	@FXML private Button btnOk;
	@FXML private TextField txtNFZLat;
	@FXML private TextField txtNFZLng;
	@FXML private TextField txtNFZRadius;
	private int x;
	private int y;
	private int r;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initButton();
	}
	public void initButton() {
		btnOk.setOnAction((event)->{handleOk(event);});
	}
	public void handleOk(ActionEvent event) {
		try {
			x = Integer.valueOf(txtNFZLat.getText());
			y = Integer.valueOf(txtNFZLng.getText());
			r = Integer.valueOf(txtNFZRadius.getText());
			Platform.runLater(()->{
				AppMainController.instance2.jsproxy.call("makeNoFlyZone",x,y,r);
			});

			}catch(Exception e) {}
		Stage dialog = (Stage)btnOk.getScene().getWindow();
		dialog.close();
	}
	
}