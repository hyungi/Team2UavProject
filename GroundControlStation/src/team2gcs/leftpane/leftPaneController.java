package team2gcs.leftpane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import team2gcs.appmain.AppMain;

public class leftPaneController implements Initializable{
	// 좌측 메뉴
	@FXML private BorderPane sensorBorderPane;
   	@FXML private BorderPane sensorDetailBorderPane;
	@FXML private VBox leftVbox;
   	@FXML private Label rollLabel;
   	@FXML private Label pitchLabel;
   	@FXML private Label yawLabel;
   	@FXML private Label sensorLabel;
   	@FXML private Label sensorDetailLabel;
   	@FXML private Canvas hudCanvas;
   	@FXML private Canvas hudLineCanvas;
   	@FXML private Canvas yawCircleCanvas;
   	private GraphicsContext ctx1;
   	private GraphicsContext ctx2;
   	private GraphicsContext ctx3;
   	private int roll = 0;
   	private int pitch = 0;
   	private int yaw = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		leftVbox.setPrefHeight(300);
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
 		initCanvasLayer();
		initLeftPane();
		try {
			handleRPY();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	////////////////////////////////// 좌측 메뉴 ////////////////////////////////
	class ViewLoop extends AnimationTimer {
		@Override
   		public void handle(long now) {
			ctx1.clearRect(0, 0, 150, 150); 
			ctx2.clearRect(0, 0, 150, 150);
			ctx3.clearRect(0, 0, 150, 150);
			
			hudDraw();
			hudLine();
  		} 
   	}
	
	private void initCanvasLayer() {
   		ctx1 = hudCanvas.getGraphicsContext2D();
   		ctx2 = hudLineCanvas.getGraphicsContext2D();
   		ctx3 = yawCircleCanvas.getGraphicsContext2D();
   	}   
	
   	private void hudDraw() {
   	//밑상자 50-pitch*1.15
    	ctx2.setFill(Color.rgb(75, 187, 161));
    	ctx2.fillRect(-50, -50, 200, 200);
   		//위상자
   		ctx2.setFill(Color.rgb(12, 143, 217));
    	ctx2.fillRect(-50, -50, 200, 100-pitch*1.15);
    	
    	//yaw
    	ctx3.setFill(Color.WHITE);
    	ctx3.fillOval(10, 10, 30, 30);
    	ctx3.setFill(Color.rgb(12, 143, 217));
    	ctx3.fillOval(21.5+15*Math.cos(yaw*0.01735-Math.PI/2),20.5+15*Math.sin(yaw*0.01735-Math.PI/2), 8, 8);

    	
//    	2안
    	//큰원
//    	ctx.setFill(Color.rgb(36, 35, 35));
//    	ctx.fillOval(20, 25, 110, 110);
    	//위반원
//    	ctx.fillArc(25, 30, 100-Math.abs(pitch)*0.3, 100-pitch*2.18, 0, 180, ArcType.OPEN);
    	//아래반원
//    	ctx.fillArc(25, 30-pitch*2.18, 100-Math.abs(pitch)*0.3, 100+pitch*2.18, 0, -180, ArcType.OPEN);
    	//yaw원
//    	ctx2.setFill(Color.WHITE);
//    	ctx2.fillOval(70+50*Math.cos(yaw*0.01735-Math.PI/2),75+50*Math.sin(yaw*0.01735-Math.PI/2), 10, 10);
   	}
   	
   	public void hudLine() {
		ctx1.setLineWidth(1);
		ctx1.setStroke(Color.WHITE);
		ctx1.strokeLine(30, 50-pitch*1.1, 70, 50-pitch*1.1);
		
		for(int i=5; i<15-pitch; i+=5) {
			ctx1.strokeLine(40, 50-(i*1.8+pitch*1.1), 60, 50-(i*1.8+pitch*1.1));
		}
		for(int i=5; i<15+pitch; i+=5) {
			ctx1.strokeLine(40, 50+(i*1.8-pitch*1.1), 60, 50+(i*1.8-pitch*1.1));
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
						hudCanvas.setRotate(roll);
						System.out.println(roll);
					}
				} else if(event.getCode() == KeyCode.D) {
					if(roll < 21) {
						roll++;
						hudCanvas.setRotate(roll);
						System.out.println(roll);
					}
				} else if(event.getCode() == KeyCode.S) {
					if(pitch > -30) {
						pitch--;
						System.out.println(pitch);
					}
				} else if(event.getCode() == KeyCode.W) {
					if(pitch < 30) {
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

}
