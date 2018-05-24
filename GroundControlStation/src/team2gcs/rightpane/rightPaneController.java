package team2gcs.rightpane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

public class rightPaneController implements Initializable{
	public static rightPaneController instance;
	@FXML private BorderPane rightStatusPane;
	@FXML private BorderPane rightCameraPane;
	@FXML private Button rightStatusBtn;
	@FXML private Button rightCameraBtn;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnEvent();
	}
	
	public void btnEvent() {
		rightStatusBtn.setOnAction((event) -> {handleStatusBtn(event);});
		rightCameraBtn.setOnAction((event) -> {handleCameraBtn(event);});
	}
	
	public void handleStatusBtn (ActionEvent event) {
		rightStatusPane.setVisible(true);
		rightCameraPane.setVisible(false);
	}
	
	public void handleCameraBtn (ActionEvent event) {
		rightStatusPane.setVisible(false);
		rightCameraPane.setVisible(true);
	}
}
