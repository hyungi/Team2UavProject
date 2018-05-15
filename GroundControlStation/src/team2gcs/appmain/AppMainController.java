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
	
	//상단 라벨
	@FXML private Label currtimeLabel;
	@FXML private Label homeLabel;
	@FXML private Label locationLabel;
	@FXML private Label batteryLabel;
	@FXML private Label signalLabel;
	
	// Pane을 움직이기 위해 Double 속성값을 사용 -> Listener를 등록가능
	private DoubleProperty bottomPaneLocation 
	 = new SimpleDoubleProperty(this,"bottomPaneLocation");
	private DoubleProperty rightPaneLocation
	 = new SimpleDoubleProperty(this,"rightPaneLocation");	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initSlide();
		initTop();
	}
////////////////////////////////// Top Menu 관련 ////////////////////////////////
	public void initTop() {
		currTime();
		homeLabel.setText("12m");
		locationLabel.setText("12m");
		batteryLabel.setText("12m");
		signalLabel.setText("12m");
	}
	
	public void currTime() {
		String inTime   = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
		currtimeLabel.setText(inTime);
	}
	
////////////////////////////////// Slide Menu 관련 ////////////////////////////////
	public void initSlide() {
		// 맨 처음 값을 200(닫혀있음)으로 만듬
		bottomPaneLocation.set(200);
		rightPaneLocation.set(350);
		// 열닫힘 버튼 클릭 이벤트 등록
		openBottom.setOnMouseClicked(event -> {
			animateBottomPane();
		});
		openRight.setOnMouseClicked(event->{
			animateRightPane();
		});
		// 위에서 선언한 Double 속성값의 Listener 등록
		bottomPaneLocation.addListener(change -> updateVBox());
		rightPaneLocation.addListener(change -> updateHBox());
	}
	
	// 각 전체 Pane의 위치를 property 값 만큼 변경(SlidePane 메소드를 통해 차례로 변경된 값이 적용됨)
	private void updateVBox() {	bottomMovePane.setTranslateY(bottomPaneLocation.get());}
	
	private void updateHBox() {
		rightMovePane.setTranslateX(rightPaneLocation.get());
	}
	
	// animate 메소드는 현재 상태를 파악하여 어느위치로 이동시켜야되는지 slidePane에게 전달해준다.
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
	
	// slidePane은 property의 값을 property에 있는 현재 위치값에서부터 to 변수로 주어진 값만큼 숫자를 0.3초 동안 올려주거나 내려준다.
	// 여기서 property의 값이 변경되면 initSlide()에 등록한 Listener가 실행되고 각 Box의 위치를 조절한다.
	// KeyValue를 통해 property의 현재 값을 -> to로 변경시키겠다.
	// KeyFrame을 통해 keyValue를 0.3초 동안 변경시키겠다.
	// Timeline을 통해 keyFrame의 값을 가지고 Animation play
	private void slidePane(double to, DoubleProperty property) {
		KeyValue keyValue = new KeyValue(property, to);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(300),keyValue);
		Timeline timeline = new Timeline(keyFrame);
		timeline.play();
		// 위치 수정이 끝나면 여닫힘 상태를 갱신
		timeline.setOnFinished((event)->{
			if(property.equals(bottomPaneLocation))	bottomControl = !bottomControl;
			else rightControl = !rightControl;
		});
	}
<<<<<<< HEAD
	
	//맵
	private void initWenView() {
		webEngine = webView.getEngine();		
		webEngine.load(getClass().getResource("javascript/index.html").toExternalForm());
	}
	

	
	private void initRightPane() {
		functionsLabel.setTextFill(Color.WHITE);
		cameraLabel.setTextFill(Color.WHITE);
		statusLabel.setTextFill(Color.WHITE);
	}
	
	private void initLeftPane() {
		
	}
=======
>>>>>>> branch 'master' of https://github.com/hyungi/Team2UavProject
}
