package team2gcs.appmain;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AppMainController implements Initializable{
	public static AppMainController instance;
	
	// 아래 버튼 & Pane & 둘을 가지고있는 VBox & control 값
	@FXML private AnchorPane openBottom;
	@FXML private BorderPane missionPane;
	@FXML private VBox bottomMovePane;
	@FXML private Label bottomOpenLabel;
	private boolean bottomControl = true;
	// 우측 버튼 & Pane & 둘을 가지고있는 HBox & control 값
	@FXML private AnchorPane openRight;
	@FXML private AnchorPane viewPane;
	@FXML private HBox rightMovePane;
	@FXML private Label rightOpenLabel;
	private boolean rightControl = true;
	
	// Pane을 움직이기 위해 Double 속성값을 사용 -> Listener를 등록가능
	private DoubleProperty bottomPaneLocation 
	 = new SimpleDoubleProperty(this,"bottomPaneLocation");
	private DoubleProperty rightPaneLocation
	 = new SimpleDoubleProperty(this,"rightPaneLocation");

	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initSlide();
	}
	
////////////////////////////////// Slide Menu 관련 ////////////////////////////////
	public void initSlide() {
		// 맨 처음 값을 200(닫혀있음)으로 만듬
		bottomPaneLocation.set(200);
		rightPaneLocation.set(350);
		openBottom.setOnMouseClicked(event -> {
			animateBottomPane();
		});
		openRight.setOnMouseClicked(event->{
			animateRightPane();
		});
		bottomPaneLocation.addListener(change -> updateVBox());
		rightPaneLocation.addListener(change -> updateHBox());
	}
	
	private void updateVBox() {
		bottomMovePane.setTranslateY(bottomPaneLocation.get());
	}
	
	private void updateHBox() {
		rightMovePane.setTranslateX(rightPaneLocation.get());
	}
	
	private void animateRightPane() {
		if(rightControl) {
			// bottomPane이 열려있으면 닫아줌 -> fxThread로 안돌리면 오류난다.
			if(!bottomControl) Platform.runLater(()->animateBottomPane());
			rightOpenLabel.setText("Close");
			slidePane(0,rightPaneLocation);
		}else {
			rightOpenLabel.setText("Open");
			slidePane(350,rightPaneLocation);
		}
	}
	
	private void animateBottomPane() {
		if(bottomControl) {
			// rightPane이 열려있으면 닫아줌
			if(!rightControl) Platform.runLater(()->animateRightPane());
			bottomOpenLabel.setText("Close Mission");
			slidePane(0,bottomPaneLocation);
		}else {
			bottomOpenLabel.setText("Open Mission");
			slidePane(200,bottomPaneLocation);
		}
	}
	
	private void slidePane(double to, DoubleProperty property) {
		KeyValue keyValue = new KeyValue(property, to);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(300),keyValue);
		Timeline timeline = new Timeline(keyFrame);
		timeline.play();
		timeline.setOnFinished((event)->{
			if(property.equals(bottomPaneLocation))	bottomControl = !bottomControl;
			else rightControl = !rightControl;
		});
	}



}
