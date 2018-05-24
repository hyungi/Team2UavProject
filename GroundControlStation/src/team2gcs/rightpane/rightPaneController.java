package team2gcs.rightpane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;

public class rightPaneController implements Initializable{
	public static rightPaneController instance;
	@FXML private BorderPane rightStatusPane;
	@FXML private BorderPane rightCameraPane;
	@FXML private Label rightStatusLabel;
	@FXML private Label rightCameraLabel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rightStatusLabel.setOnMouseClicked((event) -> {handleLabel(event);});
	}
	
	public void setTxtArea(String message) {
//		statusTxtArea.setText(message);
	}
	
	public void handleLabel (MouseEvent event) {
		rightStatusPane.setVisible(true);
		rightCameraPane.setVisible(false);
	}
}
