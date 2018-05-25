package team2gcs.camera;

import org.json.JSONObject;

public class MakeJSONPC {
    public static final String WHEEL_ANGLE_MODE = "wheelAngle";
    public static final String WHEEL_SPEED_MODE1 = "wheelSpeed1";
    public static final String WHEEL_SPEED_MODE2 = "wheelSpeed2";
    public static final String CAMEAR_ANGLE_MODE = "cameraAngle";
    public static final String LCD_LINE1_MODE = "lcdLine1";
    public static final String LCD_LINE2_MODE = "lcdLine2";
    public static final String LASER_MOS_MODE = "laserMos";
    public static final String EMERGENCY_MODE = "emergency";
    public static final String BUZZER_MODE = "buzzer";
    
    private static JSONObject jsonObject;
    
//    private String mode;
//    private int wheelAngle;
//    private int wheelSpeed;
//    private String cameraAngle;
//    private String lcdLine1;
//    private String lcdLine2;
//    private String mosInput;
//    private boolean emergency;


    public static String makeJson(String mode, String value) {
        jsonObject = new JSONObject();
        jsonObject.put("mode", mode);
        jsonObject.put("value", value);
        String json = jsonObject.toString();
        return json;
    }
    
    public static String makeJson(String mode, int value){
        jsonObject = new JSONObject();
        jsonObject.put("mode", mode);
        jsonObject.put("value", value);
        String json = jsonObject.toString();
        return json;
    }
    
    public static String makeJson(String mode, boolean value){
        jsonObject = new JSONObject();
        jsonObject.put("mode", mode);
        jsonObject.put("value", value);
        String json = jsonObject.toString();
        return json;
    }   
}
