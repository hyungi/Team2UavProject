package gcs.network;

import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import gcs.mission.FencePoint;
import gcs.mission.WayPoint;

public class UAV {
	
	public MqttClient mqttClient;
	public boolean connected;
	
	public UAV() {

	}
	
	
	public void connect() {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					//MQTT Broker와 연결하기
					mqttClient = new MqttClient("tcp://"+Network.mqttIp+":"+Network.mqttPort, MqttClient.generateClientId(), null);
					//콜백 객체 등록
					mqttClient.setCallback(new MqttCallback() {
						String json;
						
						@Override //메세지가 도착했을때
						public void messageArrived(String topic, MqttMessage message) throws Exception {
							connected =true;
							json = new String(message.getPayload());
							System.out.println(json);
							
						}
						
						@Override //메세지를 보냈을때
						public void deliveryComplete(IMqttDeliveryToken token) {
							// TODO Auto-generated method stub
							
						}
						
						@Override //연결이 끊어졌을때
						public void connectionLost(Throwable e) {
							disconnect();
						}
					});
					//MQTT Broker 연결
					mqttClient.connect();
					//MQTT 메세지 수신
					mqttClient.subscribe(Network.uavPubTopic);
					
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
	}
	
	public void disconnect() {
		try {
			connected =false;
			mqttClient.disconnect();
			mqttClient.close();
		}catch (Exception e) {}
		
	}
	
	public void send(String json){
		if(connected) {
			Thread thread = new Thread() {
				@Override
				public void run() {
					try {
						MqttMessage message = new MqttMessage(json.getBytes());
						mqttClient.publish(Network.uavSubTopic, message);
					}catch(Exception e) {}
				}
			};
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	public void arm() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "arm");
		String json = jsonObject.toString();
		send(json);
	}
	
	public void disarm() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "disarm");
		String json = jsonObject.toString();
		send(json);
	}
	
	public void takeoff(int alt) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "takeoff");
		jsonObject.put("alt", alt);
		String json = jsonObject.toString();
		send(json);
	}
	public void land() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "land");
		String json = jsonObject.toString();
		send(json);
	}
	public void rtl(int rtlAltMeter) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "rtl");
		jsonObject.put("rtl_alt", rtlAltMeter*100);
		String json = jsonObject.toString();
		send(json);
	}
	
	public void gotoLocation(double lat,double lng,double alt) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "goto");
		jsonObject.put("lat", lat);
		jsonObject.put("lng", lng);
		jsonObject.put("alt", alt);
		String json = jsonObject.toString();
		send(json);
	}
	
	public void missionUpload(List<WayPoint> list) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_upload");
		JSONArray jsonArray = new JSONArray();
		for(WayPoint wp: list) {
			JSONObject jo = new JSONObject();
			jo.put("kind", wp.getKind());
			jo.put("next", wp.getNext());
			jo.put("repeat", wp.getRepeat());
			jo.put("alt", wp.getAlt());
			jo.put("lat", wp.getLat());
			jo.put("lng", wp.getLng());
			jsonArray.put(jo);
		}
		jsonObject.put("waypoints", jsonArray);
		String json = jsonObject.toString();
		send(json);
		
	}
	public void missionDownload() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_download");
		String json = jsonObject.toString();
		send(json);
	}
	public void missionStart() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_start");
		String json = jsonObject.toString();
		send(json);
	}
	public void missionStop() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_stop");
		String json = jsonObject.toString();
		send(json);
	}
	public void missionClear() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "mission_clear");
		String json = jsonObject.toString();
		send(json);
	}
	public void fenceEnable() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_enable");
		String json = jsonObject.toString();
		send(json);
	}
	public void fenceDisable() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_disable");
		String json = jsonObject.toString();
		send(json);
	}
	public void fenceUpload(List<FencePoint> list) {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_upload");
		
		jsonObject.put("fence_type", 4); //타입 7가지 , 4번이 폴리곤
		jsonObject.put("fence_action", 1); //펜스 도달했을때 어떻게 할까? 0(report only) 1(RTL and LAND) , 그외 사용자 정의도 가능
		jsonObject.put("fence_radius", 500);
		jsonObject.put("fence_alt_max", 100);
		jsonObject.put("fence_margin", 10);
		jsonObject.put("fence_total", list.size());
		
		JSONArray jsonArray = new JSONArray();
		for(FencePoint fp : list) {
			JSONObject jsonFP = new JSONObject();
			jsonFP.put("idx", fp.getIdx());
			jsonFP.put("lat", fp.getLat());
			jsonFP.put("lng", fp.getLng());
			jsonArray.put(jsonFP);
		}
		jsonObject.put("fence_points", jsonArray);
		
		String json = jsonObject.toString();
		send(json);
	}
	public void fenceDownload() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_download");
		String json = jsonObject.toString();
		send(json);
	}
	public void fenceClear() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("command", "fence_clear");
		String json = jsonObject.toString();
		send(json);
	}
	
	
	public static void main(String[] args) throws Exception {
		UAV uav = new UAV();
		uav.connect();
		while(!uav.connected) {
			Thread.sleep(100);
		}
		
		//시동 켜기 테스트
		//uav.arm();
		
		//시동 끄기 테스트
		//uav.disarm();
		
		//이륙 테스트
		//uav.takeoff(30);
		
		//착륙 테스트
		//uav.land();
		
		//리턴 홈 테스트
		//uav.rtl(20);
		
		//바로가기 테스트
		/*double lat=37.1693195;
		double lng=128.4705967;
		double alt=30;
		uav.gotoLocation(lat, lng, alt);*/
		
		//미션 업로드 테스트
		/*List<WayPoint> list = Arrays.asList(
				new WayPoint(1,"takeoff",  0,0,0,0,10),
				new WayPoint(2,"waypoint", 0,0,37.1692554,128.4709883,10),
				new WayPoint(3,"waypoint", 0,0,37.1694349,128.4704572,10),
				new WayPoint(4,"waypoint", 0,0,37.1697812,128.4706879,10),
				new WayPoint(5,"waypoint", 0,0,37.1695461,128.4712189,10),
				new WayPoint(6,"jump", 2,3,0,0,0),
				new WayPoint(7,"rtl", 0,0,0,0,0)
		);
		uav.missionUpload(list);*/
		
		//미션 다운로드 테스트
		//uav.missionDownload();
		
		//미션 시작 테스트
		//uav.missionStart();
		
		//미션 시작 테스트
		//uav.missionStop();3
		
		//미션 지우기 테스트
		//uav.missionClear();
		
		//펜스 활성화 테스트
		//uav.fenceEnable();
		
		//펜스 비활성화 테스트
		//uav.fenceDisable();
		
		//펜스 업로드 테스트
		/*List<FencePoint> list = Arrays.asList(
				new FencePoint(0,37.1695242,128.4708725),//Home
				new FencePoint(1,37.1692554,128.4709883),
				new FencePoint(2,37.1694349,128.4704572),
				new FencePoint(3,37.1697812,128.4706879),
				new FencePoint(4,37.1695461,128.4712189),
				new FencePoint(5,37.1692554,128.4709883) // 1번다시
		);
		uav.fenceUpload(list);*/
		
		//펜스 다운로드 테스트
		//uav.fenceDownload();
		
		//펜스 클리어 테스트
		//uav.fenceClear();
		
		System.in.read();
	}
	
}
