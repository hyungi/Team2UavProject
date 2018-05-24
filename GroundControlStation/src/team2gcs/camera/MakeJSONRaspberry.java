package team2gcs.camera;

import org.json.JSONObject;

public class MakeJSONRaspberry {
    private double temp;
    private double light;
    private double gas;
    private double dist;
    private boolean tracking;
    private double gyro;
    
    private JSONObject jsonObject;

    public String MakeJSONRaspberry(double temp, double light, double gas, double dist, boolean tracking, double gyro) {
        jsonObject = new JSONObject();
        jsonObject.put("temp", temp);
        jsonObject.put("light", light);
        jsonObject.put("gas", gas);
        jsonObject.put("dist", dist);
        jsonObject.put("tracking", tracking);
        jsonObject.put("gyro", gyro);
        String json = jsonObject.toString();
        return json;
    }
}
