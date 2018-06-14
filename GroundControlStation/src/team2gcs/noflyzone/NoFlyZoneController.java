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
	public static NoFlyZoneController instance;

	@FXML private Button btnOk;
	@FXML private TextField txtNFZLat;
	@FXML private TextField txtNFZLng;
	@FXML private TextField txtNFZRadius;

	public static double x;
	public static double y;
	public static double r;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initButton();
	}
	public void initButton() {
		btnOk.setDefaultButton(true);
		btnOk.setOnAction((event)->{handleOk(event);});
	}
	//지도에 no-fly-zone 그리기
	public void handleOk(ActionEvent event) {
		try {

			x = Double.parseDouble(txtNFZLng.getText());
			y = Double.parseDouble(txtNFZLat.getText());
			r = Double.parseDouble(txtNFZRadius.getText());
			Platform.runLater(()->{
				AppMainController.instance2.jsproxy.call("makeNoFlyZone",y,x,r);
			});
			}catch(Exception e) {}
		Stage dialog = (Stage)btnOk.getScene().getWindow();
		dialog.close();
	}
	
}