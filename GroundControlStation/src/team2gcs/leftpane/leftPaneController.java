package team2gcs.leftpane;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import team2gcs.appmain.AppMain;
import team2gcs.appmain.AppMainController;

public class leftPaneController implements Initializable{
	//좌측 메뉴
	@FXML private VBox leftVbox;
	@FXML private Label rollLabel;
	@FXML private Label pitchLabel;
	@FXML private Label yawLabel;
	@FXML private Canvas hudCanvas;
	@FXML private Canvas hudLineCanvas;
	private GraphicsContext ctx;
	private GraphicsContext ctx2;
	private int roll = 0;
	private int pitch = 0;
	private int yaw = 0;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		leftVbox.setPrefHeight(818);
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
 		initCanvasLayer();
		initLeftPane();
		handleYaw();
		ctx2.rotate(roll);
	}
	
	////////////////////////////////// 좌측 메뉴 ////////////////////////////////
	class ViewLoop extends AnimationTimer {
		@Override
		public void handle(long now) {

			ctx.translate(-50, -50);
			ctx.clearRect(0, 0, 140, 140);
			ctx.translate(50, 50);
			ctx2.translate(-50, -50);
			ctx2.clearRect(0, 0, 140, 140);
			ctx2.translate(50, 50);

			hudDraw();
			hudLine();
		}
	}

	private void hudDraw() {
		ctx.setLineWidth(5);
		// 큰원
		ctx.setFill(Color.rgb(36, 35, 35));
		ctx.fillOval(20, 25, 110, 110);
		// 위반원
		ctx.setFill(Color.rgb(12, 143, 217));
		ctx.fillArc(25, 30, 100, 100, 0, 180, ArcType.ROUND);
		// 아래반원
		ctx.setFill(Color.rgb(75, 187, 161));
		ctx.fillArc(25, 30, 100, 100, 0, -180, ArcType.ROUND);
		// ctx.rotate(180);
		// yaw원
		ctx.setFill(Color.WHITE);
		ctx.fillOval(70 + 50 * Math.cos(yaw * 0.01735 - Math.PI / 2), 75 + 50 * Math.sin(yaw * 0.01735 - Math.PI / 2),
				10, 10);
	}

	public void hudLine() {
		ctx2.setLineWidth(1);
		ctx2.setStroke(Color.WHITE);
		ctx2.strokeLine(55, 80.5, 95, 80.5);

		for (int i = 0; i < 20; i += 5) {
			if (i != 0) {
				ctx2.strokeLine(65, 80.5 - (i * 1.8), 85, 80.5 - (i * 1.8));
				ctx2.strokeLine(65, 80.5 + (i * 1.8), 85, 80.5 + (i * 1.8));
			}
		}
	}

	private void handleYaw() {
		Platform.runLater(() -> {
			AppMain.tempScene.setOnKeyPressed((event) -> {
				if (event.getCode() == KeyCode.LEFT) {
					yaw--;
					if (yaw == -1)
						yaw = 359;
					System.out.println(yaw);
				} else if (event.getCode() == KeyCode.RIGHT) {
					yaw++;
					if (yaw == 360)
						yaw = 0;
					System.out.println(yaw);
				} else if (event.getCode() == KeyCode.NUMPAD4) {
					if (roll >= -21) {
						ctx2.rotate(-1);
						roll--;
					}
				} else if (event.getCode() == KeyCode.NUMPAD6) {
					if (roll < 21) {
						ctx2.rotate(1);
						roll++;
					}
				}
			});
		});
	}

	private void initLeftPane() {
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
		initCanvasLayer();
	}

	private void initCanvasLayer() {
		ctx = hudCanvas.getGraphicsContext2D();
		ctx2 = hudLineCanvas.getGraphicsContext2D();
	}

}
