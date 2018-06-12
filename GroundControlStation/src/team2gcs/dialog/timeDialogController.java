package team2gcs.dialog;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import team2gcs.appmain.AppMainController;

public class timeDialogController implements Initializable {
	@FXML TextField txtTime;
	@FXML Button btnSet;
	@FXML Button btnExit;
	public static int missionTime;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		btnSet.setDefaultButton(true);
		btnSet.setOnAction((event) -> {handleTakeoff(event);});
		btnExit.setOnAction((event)-> {AppMainController.timeStage.close();});
	}

	private void handleTakeoff(ActionEvent e) {
		missionTime = Integer.parseInt(txtTime.getText());
		AppMainController.instance2.btnMissionTime.setText("Time: " + missionTime);
		AppMainController.timeStage.close();
	}
}
