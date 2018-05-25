/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team2gcs.camera;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 *
 * @author Administrator
 */
public class ViewerController implements Initializable {
    @FXML private Canvas canvas;
    @FXML private AnchorPane hudview;
    @FXML private StackPane menuview;
    
    private static MqttClient client;
    private static MqttMessage mqttMessage;
    private static String message;
    private static boolean emer = false;
    
    
    public void publisher() throws Exception{
        client = new MqttClient("tcp://192.168.3.16:1883", MqttClient.generateClientId(),null);
        client.connect();
    }
    
    public static void setCamera(String message) throws Exception{
        mqttMessage = new MqttMessage(MakeJSONPC.makeJson(MakeJSONPC.CAMEAR_ANGLE_MODE, message).getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }
    
    public static void setSpeed1(String message) throws Exception{
       mqttMessage = new MqttMessage(MakeJSONPC.makeJson(MakeJSONPC.WHEEL_SPEED_MODE1, message).getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }
    
    public static void setSpeed2(String message) throws Exception{
       mqttMessage = new MqttMessage(MakeJSONPC.makeJson(MakeJSONPC.WHEEL_SPEED_MODE2, message).getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }
    
    public static void setAngle(String message) throws Exception{
        mqttMessage = new MqttMessage(MakeJSONPC.makeJson(MakeJSONPC.WHEEL_ANGLE_MODE, message).getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }
    
    public static void setLCD1(String message) throws Exception{
        mqttMessage = new MqttMessage(MakeJSONPC.makeJson(MakeJSONPC.LCD_LINE1_MODE, message).getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }
    
    public static void setLCD2(String message) throws Exception{
        mqttMessage = new MqttMessage(MakeJSONPC.makeJson(MakeJSONPC.LCD_LINE2_MODE, message).getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }
    
    public static void setMorse(String message) throws Exception{
        mqttMessage = new MqttMessage(MakeJSONPC.makeJson(MakeJSONPC.LASER_MOS_MODE, message).getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }
    
    public static void setUturn() throws Exception {
        mqttMessage = new MqttMessage(MakeJSONPC.makeJson("uturn", "yes").getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }
    
    public static void setEmer() throws Exception{
        emer = !emer;
        System.out.println(emer);
        mqttMessage = new MqttMessage(MakeJSONPC.makeJson(MakeJSONPC.EMERGENCY_MODE, emer).getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }
    
    public static void setBuzzer(String message) throws Exception{
        mqttMessage = new MqttMessage(MakeJSONPC.makeJson(MakeJSONPC.BUZZER_MODE, message).getBytes("UTF-8"));
        client.publish("/devieces", mqttMessage);
    }

    
    
        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mqttView();
    }

    
    private void mqttView(){
        try{
            //규정씨 244
            CamStream camStream = new CamStream("tcp://192.168.3.16:1883","/car/camera",canvas);
            camStream.start();
            publisher();
        }catch(Exception e){
            
        }
    }
    
}
