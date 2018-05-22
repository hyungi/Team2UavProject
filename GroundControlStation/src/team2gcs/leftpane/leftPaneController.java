package team2gcs.leftpane;

import java.net.URL;
import java.util.ResourceBundle;

import gcs.network.UAV;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import team2gcs.appmain.AppMain;

public class leftPaneController implements Initializable{
	public static leftPaneController instance;
	// 좌측 메뉴
	@FXML private BorderPane sensorBorderPane;
   	@FXML private BorderPane sensorDetailBorderPane;
	@FXML private VBox leftVbox;
   	@FXML private Label rollLabel;
   	@FXML private Label pitchLabel;
   	@FXML private Label yawLabel;
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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance = this;
		leftVbox.setPrefHeight(300);
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
 		initCanvasLayer();
		initLeftPane();
	}
	
	////////////////////////////////// 좌측 메뉴 ////////////////////////////////
	class ViewLoop extends AnimationTimer {
		@Override
   		public void handle(long now) {
			ctx1.clearRect(0, 0, 150, 150); 
			ctx2.clearRect(0, 0, 150, 150);
			
			drawHud();
			drawHudLine();
			try {
				handleRPY();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rollLabel.setText("   Roll:	" + roll);
			pitchLabel.setText("   Pitch:	" + pitch);
			yawLabel.setText("   Yaw:	" + yaw);
  		} 
   	}
	
	private void initCanvasLayer() {
   		ctx1 = hudLineCanvas.getGraphicsContext2D();
   		ctx2 = yawCanvas.getGraphicsContext2D();
   	}   
	
   	private void drawHud() {
		ImagePattern img = new ImagePattern(new Image(getClass().getResourceAsStream("../images/hudBg1.png")), 0, -pitch, 100, 300, false);
	   	circle.setFill(img);
    	
	    	//yaw
	    	ctx2.setFill(Color.WHITE);
	    	ctx2.fillOval(45.25+39.85*Math.cos(yaw*0.01745-Math.PI/2),45.25+39.85*Math.sin(yaw*0.01745-Math.PI/2), 10, 10);
   	}
   	
   	public void drawHudLine() {
		ctx1.setLineWidth(1);
		ctx1.setStroke(Color.WHITE);
		ctx1.strokeLine(30, 50-pitch*1.1, 70, 50-pitch*1.1);
		
		for(int i=5; i<15-pitch; i+=5) {
			ctx1.strokeLine(40, 50-(i*1.6+pitch*1.1), 60, 50-(i*1.6+pitch*1.05));
		}
		for(int i=5; i<15+pitch; i+=5) {
			ctx1.strokeLine(40, 50+(i*1.6-pitch*1.1), 60, 50+(i*1.6-pitch*1.05));
		}
	}
	
	private void handleRPY() throws Exception {
		Platform.runLater(() -> {
			AppMain.tempScene.setOnKeyPressed((event) -> {
				if(event.getCode() == KeyCode.Q) {
					yaw--;
					System.out.println(yaw);
					if(yaw == -1) yaw = 359;
				} else if(event.getCode() == KeyCode.E) {
					yaw++;
					System.out.println(yaw);
					if(yaw == 360) yaw = 0;
				} else if(event.getCode() == KeyCode.A) {
					if(roll>= -21) {
						roll--;
						hudLineCanvas.setRotate(roll);
						circle.setRotate(roll);
						System.out.println(roll);
					}
				} else if(event.getCode() == KeyCode.D) {
					if(roll < 21) {
						roll++;
						hudLineCanvas.setRotate(roll);
						circle.setRotate(roll);
						System.out.println(roll);
					}
				} else if(event.getCode() == KeyCode.S) {
					if(pitch > -10) {
						pitch--;
						System.out.println(pitch);
					}
				} else if(event.getCode() == KeyCode.W) {
					if(pitch < 10) {
						pitch++;
						System.out.println(pitch);
					}
				}
			});
		});
	} 
	
   	private void initLeftPane() {
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
		initCanvasLayer();
		sensorLabelEvent();
   	}
   	
   	private void sensorLabelEvent() {
		sensorLabel.setOnMouseClicked(new EventHandler<Event>() {
			@Override
			public void handle(Event event) {
				sensorDetailBorderPane.setVisible(true);
				sensorBorderPane.setVisible(false);
				System.out.println("+");
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
	
	public void setStatus(UAV uav) {
		roll = uav.roll;
		pitch = uav.pitch;
		yaw = uav.yaw;
	}
}
