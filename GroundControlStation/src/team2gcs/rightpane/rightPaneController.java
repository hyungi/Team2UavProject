package team2gcs.rightpane;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import gcs.network.Network;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class rightPaneController implements Initializable{
	public static rightPaneController instance;
	@FXML private BorderPane rightStatusPane;
	@FXML private BorderPane rightCameraPane;
	@FXML private VBox rightVBox;
	@FXML private VBox statusVBox;
	@FXML private Button rightStatusBtn;
	@FXML private Button rightCameraBtn;
	@FXML private Button rightDeleteBtn;
	@FXML private ListView<String> statusListView;
	public static List<String> statusList = new ArrayList<String>();
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnEvent();
	}
	
	public void btnEvent() {
		rightStatusBtn.setOnAction((event) -> {handleStatusBtn(event);});
		rightCameraBtn.setOnAction((event) -> {handleCameraBtn(event);});
		rightDeleteBtn.setOnAction((event) -> {handleDeleteBtn(event);});
	}
	
	public void handleStatusBtn (ActionEvent event) {
		rightStatusPane.setVisible(true);
		rightCameraPane.setVisible(false);
	}
	
	public void handleCameraBtn (ActionEvent event) {
		rightStatusPane.setVisible(false);
		rightCameraPane.setVisible(true);
	}
	
	public void handleDeleteBtn(ActionEvent event) {
		statusList.removeAll(statusList);
	}
	
	public void addMessageLabel(String message) {
//		Label label = new Label();
//		label.setAlignment(Pos.CENTER);
//		label.setTextAlignment(TextAlignment.CENTER);
//		label.setPrefWidth(340);
//		label.setPrefHeight(50);
//		label.setTextFill(Color.WHITE);
//		label.setText(message);
//		statusVBox.getChildren().add(label);
		statusList.add(message);
		statusListView.getItems().clear();
		statusListView.setItems(FXCollections.observableArrayList(statusList));
	}
}
