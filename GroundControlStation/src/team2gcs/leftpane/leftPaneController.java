package team2gcs.leftpane;

import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import gcs.network.Network;
import gcs.network.UAV;
import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import team2gcs.appmain.AppMainController;

public class leftPaneController implements Initializable{
	public static leftPaneController instance;
	// 좌측 메뉴
	@FXML private BorderPane sensorBorderPane;
   	@FXML private BorderPane sensorDetailBorderPane;
	@FXML private VBox leftVbox;
   	@FXML private Label rollLabel;
   	@FXML private Label pitchLabel;
   	@FXML private Label yawLabel;
   	@FXML private Label armedLabel;
   	@FXML private Label modeLabel;
   	@FXML private Label airSpeedLabel;
   	@FXML private Label groundSpeedLabel;
   	@FXML private Label takeoffTimeLabel;
   	@FXML private Label altitudeLabel;
   	@FXML private Label statusLabel;
   	@FXML private Label detailModeLabel;
   	@FXML private Label detailAirSpeedLabel;
   	@FXML private Label detailGroundSpeedLabel;
   	@FXML private Label detailAltitudeLabel;
   	@FXML private Label detailMissionTimeLabel;
   	@FXML private Label detailDistHomeLabel;
   	@FXML private Label detailTakeoffTimeLabel;
   	@FXML private Label detailFenceLabel;
   	@FXML private Label detailMissionLabel;
   	@FXML private Label detailNoFlyLabel;
   	@FXML private Label detailVoltageLabel;
   	@FXML private Label sensorLabel;
   	@FXML private Label sensorDetailLabel;
   	@FXML private Circle circle;
   	@FXML private Canvas hudLineCanvas;
   	@FXML private Canvas yawCanvas;
   	
   	private GraphicsContext ctx1;
   	private GraphicsContext ctx2;
   	private double roll = 0;
   	private double pitch = 0;
   	private double yaw = 0;
   	private double airSpeed = 0;
   	private double groundSpeed = 0;
   	private double altitude = 0;
   	private double distWP = 0;
   	private double distHome = 0;
   	private double timeAir = 0;
   	private double voltage = 0;
   	private double fenceEnable = 0.0;
   	private String mode = "DisArmed";
   	private String missionTime;
   	private String takeoffTime;
   	private boolean armed = false;
   	private boolean missionData = false;
   	private boolean noFlyData = false;
   	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		initLeftPane();
	}
	
   	private void initLeftPane() {
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
		initCanvasLayer();
		sensorLabelEvent();
   	}
	class ViewLoop extends AnimationTimer {
		@Override
   		public void handle(long now) {
			ctx1.clearRect(0, 0, hudLineCanvas.getWidth(), hudLineCanvas.getHeight()); 
			ctx2.clearRect(0, 0, yawCanvas.getWidth(), yawCanvas.getHeight());
			drawHud();
			drawHudLine();
  		} 
   	}
	
	private void initCanvasLayer() {
   		ctx1 = hudLineCanvas.getGraphicsContext2D();
   		ctx2 = yawCanvas.getGraphicsContext2D();
   	}   
	
   	private void drawHud() {
		ImagePattern img = new ImagePattern(new Image(getClass().getResourceAsStream("../images/hudBg1.png")), 0, pitch*2, 100, 300, false);
	   	circle.setFill(img);
    	
    	//yaw
    	ctx2.setFill(Color.WHITE);
    	ctx2.fillOval(82.5+(65*Math.cos(yaw*0.017468-Math.PI/2)), 82+(65*Math.sin(yaw*0.017468-Math.PI/2)), 15, 15);
   	}

   	public void drawHudLine() {
		ctx1.setLineWidth(1);
		ctx1.setStroke(Color.WHITE);
		ctx1.strokeLine(30, 70+pitch*2, 110, 70+pitch*2);
		
		for(int i=5; i<25-pitch; i+=5) {
			ctx1.strokeLine((i%2==0)?40:50, 70+(i*1.75+pitch*2), (i%2==0)?100:90, 70+(i*1.75+pitch*2));
			
		}
		for(int i=5; i<25+pitch; i+=5) {
			ctx1.strokeLine((i%2==0)?40:50, 70-(i*1.75-pitch*2), (i%2==0)?100:90, 70-(i*1.75-pitch*2));
		}
	}
   	
   	private void sensorLabelEvent() {
		sensorLabel.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				sensorDetailBorderPane.setVisible(true);
				sensorBorderPane.setVisible(false);
			}
		});
		sensorDetailLabel.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				sensorDetailBorderPane.setVisible(false);
				sensorBorderPane.setVisible(true);
				System.out.println("-");
			}
		});
   	}
	
	public void getRollStatus(UAV uav) {
		roll = uav.roll;
		pitch = uav.pitch;
		if(uav.yaw < 0) yaw = uav.yaw+360;
		else yaw = uav.yaw;
		armed = uav.armed;
	}
	
	public void setRollStatus() {
		rollLabel.setText(String.format("%.4f",roll));
		pitchLabel.setText(String.format("%.4f",pitch));
		yawLabel.setText(String.format("%.4f",yaw));
		if(armed == true) {
			armedLabel.setStyle("-fx-text-fill: red;");
			armedLabel.setText("Armed");
		} else if(armed == false) {
			armedLabel.setStyle("-fx-text-fill: white;");
			armedLabel.setText("DisArmed");
		}
		hudLineCanvas.setRotate(roll*2);
		circle.setRotate(roll*2);
	}
	
	public void getStatus(UAV uav) {
		if(uav.connected) {
			mode = uav.mode;
			airSpeed = uav.airSpeed;
			groundSpeed = uav.groundSpeed;
			altitude = uav.altitude;
			fenceEnable = uav.fenceEnable;
			if(AppMainController.list.size() == 0) missionData = false;
			else if(AppMainController.list.size() != 0) missionData = true;
			System.out.println("list: " + AppMainController.list.size());
			voltage = uav.batteryVoltage;
			missionTime = AppMainController.missionTime;
			takeoffTime = AppMainController.takeoffTime;
		}
	}
	
	public void setStatus() {
		//간단 모드
		if(Network.getUav().connected) {
			modeLabel.setText(mode);
			airSpeedLabel.setText(String.format("%.4f", airSpeed));
			groundSpeedLabel.setText(String.format("%.4f", groundSpeed));
			altitudeLabel.setText(String.format("%.2f", altitude));
			if(AppMainController.takeoffStart == true) takeoffTimeLabel.setText(takeoffTime);
			else if(AppMainController.takeoffStart == false) takeoffTimeLabel.setText("UAV Landed.");
			
			//상세모드
			if(Network.getUav().armed) {
				detailModeLabel.setText(mode);
				detailAirSpeedLabel.setText(String.format("%.6f", airSpeed));
				detailGroundSpeedLabel.setText(String.format("%.6f", groundSpeed));
				detailAltitudeLabel.setText(String.format("%.2f", altitude));
				if(fenceEnable == 0.0) {
					detailFenceLabel.setStyle("-fx-text-fill: white;");
					detailFenceLabel.setText("Deactivated.");
				}else if(fenceEnable == 1.0) {
					detailFenceLabel.setStyle("-fx-text-fill: red;");
					detailFenceLabel.setText("Activated.");
				}
				if(missionData == false) {
					detailMissionLabel.setStyle("-fx-text-fill: white;");
					detailMissionLabel.setText("No Mission.");
				} else if(missionData == true) {
					detailMissionLabel.setStyle("-fx-text-fill: red;");
					detailMissionLabel.setText("Set.");
				}
				if(AppMainController.missionStart == true) {
					detailMissionTimeLabel.setText(missionTime);
					if(UAV.nextWP == AppMainController.list.size()) detailMissionTimeLabel.setText("Completed");
				} else if(AppMainController.missionStart == false) detailMissionTimeLabel.setText("No Mission.");
				if(AppMainController.takeoffStart == true) detailTakeoffTimeLabel.setText(takeoffTime);
				else if(AppMainController.takeoffStart == false) detailTakeoffTimeLabel.setText("Landed.");
				detailVoltageLabel.setText(String.format("%.4f", voltage));
			} else if(!Network.getUav().armed) {
				detailModeLabel.setText("UAV Disarmed.");
				detailAirSpeedLabel.setText(String.format("%.6f", airSpeed));
				detailGroundSpeedLabel.setText(String.format("%.6f", groundSpeed));
			}
		}
	}
	
	public void setStatusLabels(String message) {
		statusLabel.setText(message);
	}
}