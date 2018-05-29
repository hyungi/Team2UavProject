package gcs.network;

import team2gcs.appmain.AppMainController;

public class Network {
	public static String mqttIp = AppMainController.ip;
	public static String mqttPort = AppMainController.port;
	public static String uavPubTopic = "/uav2/pub" ;
	public static String uavSubTopic = "/uav2/sub" ;
	public static String cameraTopicF = "/uav2/cameraFront";
	public static String cameraTopicB = "/uav2/cameraBottom";
	private static UAV uav;
	
	public static void connect() {
		uav = new UAV();
		uav.connect();
	}
	
	public static void disconnect() {
		uav.disconnect();
	}
	
	public static UAV getUav() {
	 	return uav;
	}
}
