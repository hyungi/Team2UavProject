package gcs.network;

import team2gcs.appmain.AppMainController;

public class Network {
	public static String mqttIp = AppMainController.ip;
	public static String mqttPort = AppMainController.port;
	public static String uavPubTopic = "/uav2/pub";
	public static String uavSubTopic = "/uav2/sub";
	public static String uavCameraPubTopicF = "/uav2/cameraFront/pub";
	public static String uavCameraSubTopicF = "/uav2/cameraFront/sub";
	public static String uavCameraPubTopicB = "/uav2/cameraBottom/pub"; // GCS 입장에서는 Sub
	public static String uavCameraSubTopicB = "/uav2/cameraBottom/sub"; // GCS 입장에서는 Publish
	private static UAV uav;
	
	public static void connect() {
		uav = new UAV();
		System.out.println("커넥트");
		uav.connect();
	}
	
	public static void disconnect() {
		uav.disconnect();
	}
	
	public static UAV getUav() {
	 	return uav;
	}
}
