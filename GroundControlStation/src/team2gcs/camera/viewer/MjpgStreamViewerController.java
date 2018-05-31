package team2gcs.camera.viewer;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import team2gcs.camera.CameraStream;

public class MjpgStreamViewerController implements Initializable {
    @FXML private Canvas canvas;
    @FXML private Canvas canvas2;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //httpView();
        mqttView();        
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
        	CameraStream front = new CameraStream("tcp://106.253.56.122:1883", "/uav2/cameraFront", canvas);
        	CameraStream bottom = new CameraStream("tcp://106.253.56.122:1883", "/uav2/cameraBottom", canvas2);
        	
            front.start();
            bottom.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
