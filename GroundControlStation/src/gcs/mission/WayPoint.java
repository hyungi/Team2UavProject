package gcs.mission;

import javafx.scene.control.Button;

public class WayPoint {
	public int no;
	public String kind;
	public int repeat;
	public int next;
	public double latitude;
	public double longitude;
	public double altitude;
	public int jump;
	public int jumpnum;
	public double waitingTime;
	private Button button = new Button("x");
	
	
	
	public Button getButton() {
		return button;
	}
	public void setButton(Button button) {
		this.button = button;
		this.button.setOnAction((event)->{
			System.out.println("okay");
		});
	}
	public int getNo() {
		return no;
	}
	public void setNo(int no) {
		this.no = no;
	}
	public String getKind() {
		return kind;
	}
	public void setKind(String kind) {
		this.kind = kind;
	}
	public int getRepeat() {
		return repeat;
	}
	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public int getJump() {
		return jump;
	}
	public void setJump(int jump) {
		this.jump = jump;
	}
	public int getJumpnum() {
		return jumpnum;
	}
	public void setJumpnum(int jumpnum) {
		this.jumpnum = jumpnum;
	}
	public double getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(double waitingTime) {
		this.waitingTime = waitingTime;
	}
	
	
}
