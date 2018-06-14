package team2gcs.camera.viewer;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import netscape.javascript.JSObject;
import team2gcs.camera.CameraStreamB;
import team2gcs.camera.CameraStreamF;

public class MjpgStreamViewerController implements Initializable {
    @FXML private Canvas canvas;
    @FXML private Canvas canvas2;
	@FXML private Button captureBtn;
	@FXML private Button startCamBtn;
	@FXML private Button stopCamBtn;
	
	public JSObject jsproxy;
	
	public boolean camFlag = false;
	
	private static final double FRAME_RATE = 5;
	private static final int SECONDS_TO_RUN_FOR = 20;
	private static Dimension screenBounds;
	
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //httpView();
        mqttView();        
        captureBtn.setOnMouseClicked((event) -> {try {handleCaptureBtn(event);} catch (Exception e) {e.printStackTrace();}});
        //startCamBtn.setOnMouseClicked((event) -> {try {handleStartCamBtn(event);} catch (Exception e) {e.printStackTrace();}});
        //stopCamBtn.setOnMouseClicked((event) -> {try {handleStopCamBtn(event);} catch (Exception e) {e.printStackTrace();}});
		
    }
    /*private void httpView() {
        try {
            CameraStream camStream = new CameraStream("http://192.168.3.41:50005/?action=stream", canvas);
            camStream.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }*/
    
    private void mqttView() {
        try {
//        	CameraStream front = new CameraStream("tcp://192.168.3.16:1883", "/uav2/cameraFront", canvas);
//        	CameraStream bottom = new CameraStream("tcp://192.168.3.16:1883", "/uav2/cameraBottom", canvas2);
        	CameraStreamB bottom = new CameraStreamB(canvas2);
        	CameraStreamF front = new CameraStreamF(canvas);
        	
        	bottom.start();
            front.start();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

	public void handleCaptureBtn (MouseEvent event) throws Exception {
		System.out.println("capture");
		Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.05), evt -> captureBtn.setVisible(false)),
                new KeyFrame(Duration.seconds( 0.1), evt -> captureBtn.setVisible(true)));
		timeline.setCycleCount(1);
		timeline.play();
		
        WritableImage wi = new WritableImage(320, 240);
        WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), wi);

        File output = new File("src/team2gcs/camera/images/"+"capture" + new Date().getTime() + ".png");
        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
	}

	@SuppressWarnings("deprecation")
	public void handleStartCamBtn(MouseEvent event) throws Exception{
		System.out.println("StartCam");
		
		/*String outputTime = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
		String outputFilename = "src/team2gcs/camera/video/" + "outputTime";
		final IMediaWriter writer = ToolFactory.makeWriter(outputFilename);
		screenBounds = Toolkit.getDefaultToolkit().getScreenSize();
		// We tell it we're going to add one video stream, with id 0,
		// at position 0, and that it will have a fixed frame rate of FRAME_RATE.
		writer.addVideoStream(0, 0, ICodec.ID.CODEC_ID_MPEG4,
				screenBounds.width / 2, screenBounds.height / 2);
		long startTime = System.nanoTime();

		// tell the writer to close and write the trailer if needed
		writer.close();*/
		
		Thread thread = new Thread(){
            @Override
            public void run() {
            	while(true && !camFlag) {
	                try{
 	            		Platform.runLater(()->{
	             			 try {
	             				WritableImage wi = new WritableImage(320, 240);
	             		        WritableImage snapshot = canvas.snapshot(new SnapshotParameters(), wi);

	             		        File output = new File("src/team2gcs/camera/images/"+"img" + new Date().getTime() + ".png");
	             		        ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", output);
	             				//System.out.println("gg");
	             		        
	             		        /*BufferedImage screen = ImageIO.read(output);
	             				// convert to the right image type
	             				BufferedImage bgrScreen = convertToType(screen,
	             						BufferedImage.TYPE_3BYTE_BGR);
	             				// encode the image to stream #0
	             				writer.encodeVideo(0, bgrScreen, System.nanoTime() - startTime,
	             						TimeUnit.NANOSECONDS);*/
	             				// sleep for frame rate milliseconds
	             				/*try {
	             					Thread.sleep((long) (1000 / FRAME_RATE));
	             				}
	             				// ignore
	             				catch (InterruptedException e) {}*/
							} catch (Exception e) {	e.printStackTrace();}
	             		});	
	            		Thread.sleep(500);
	                }
	                catch(Exception e){}
            	}
            }
        };
		thread.setDaemon(true);
		thread.start();
	}
	
	@SuppressWarnings("deprecation")
	public void handleStopCamBtn(MouseEvent event) throws Exception{
		camFlag = true;
		System.out.println("StopCam");

	}
	
	public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
		BufferedImage image;
		// if the source image is already the target type, return the source image
		if (sourceImage.getType() == targetType) {
			image = sourceImage;
		}
		// otherwise create a new image of the target type and draw the new image
		else {
			image = new BufferedImage(sourceImage.getWidth(),
					sourceImage.getHeight(), targetType);
			image.getGraphics().drawImage(sourceImage, 0, 0, null);
		}
		return image;
	}
	
	private static BufferedImage getDesktopScreenshot() throws Exception{
		BufferedImage bufferedImage = ImageIO.read(new File("src/team2gcs/camera/images/"+"img" + new Date().getTime() + ".png"));
		return bufferedImage;
		/*Robot robot = new Robot();
		Rectangle captureSize = new Rectangle(screenBounds);
		return robot.createScreenCapture(captureSize);*/
	}
}
