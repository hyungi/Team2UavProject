package team2gcs.rightpane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import team2gcs.appmain.AppMainController;

public class rightPaneController implements Initializable{
	@FXML VBox rightVbox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rightVbox.setPrefHeight(818);
	}

}
