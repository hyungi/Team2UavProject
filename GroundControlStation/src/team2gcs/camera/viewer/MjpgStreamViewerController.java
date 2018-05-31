package team2gcs.camera.viewer;

import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import team2gcs.camera.CameraStream;

public class MjpgStreamViewerController implements Initializable {
    @FXML private Canvas canvas;
    @FXML private Canvas canvas2;
	@FXML private Label captureLabel;
	@FXML private Label imgdownloadLabel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //httpView();
        mqttView();        
		captureLabel.setOnMouseClicked((event) -> {try {handleCaptureBtn(event);} catch (Exception e) {e.printStackTrace();}});
		imgdownloadLabel.setOnMouseClicked((event) -> {try {handleImgdownloadBtn(event);} catch (Exception e) {e.printStackTrace();}});
		
    }
    private void httpView() {
        try {
            CameraStream camStream = new CameraStream("http://192.168.3.41:50005/?action=stream", canvas);
            camStream.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    private void mqttView() {
        try {
//        	CameraStream front = new CameraStream("tcp://192.168.3.16:1883", "/uav2/cameraFront", canvas);
//        	CameraStream bottom = new CameraStream("tcp://192.168.3.16:1883", "/uav2/cameraBottom", canvas2);
        	CameraStream bottom = new CameraStream("tcp://106.253.56.122:1883", "/uav2/cameraBottom", canvas2);
        	CameraStream front = new CameraStream("tcp://106.253.56.122:1883", "/uav2/cameraFront", canvas);

        	bottom.start();
            front.start();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	public void handleCaptureBtn (MouseEvent event) throws Exception {
		System.out.println("capture");
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), evt -> captureLabel.setVisible(false)),
                new KeyFrame(Duration.seconds( 0.1), evt -> captureLabel.setVisible(true)));
		timeline.setCycleCount(1);
		timeline.play();
		SnapshotParameters parameters = new SnapshotParameters();
        WritableImage wi = new WritableImage(320, 240);
        WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), wi);

        File output = new File("camera" + new Date().getTime() + ".png");
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
	}
	
	public void handleImgdownloadBtn (MouseEvent event) {
		System.out.println("download");
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), evt -> imgdownloadLabel.setVisible(false)),
                new KeyFrame(Duration.seconds( 0.1), evt -> imgdownloadLabel.setVisible(true)));
		timeline.setCycleCount(1);
		timeline.play();
	}
}
