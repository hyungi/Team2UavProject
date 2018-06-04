package gcs.mission;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

public class WayPoint {
	public int no;
	public String kind;
	public int repeat;
	public int next;
   	public SimpleStringProperty lat 
   	= new SimpleStringProperty();
   	public SimpleStringProperty lng 
   	= new SimpleStringProperty();
	public double altitude;
	public int jumpNo;
	public int repeatCount;
	public double waitingTime;
	private Button button = new Button("x");
	public int nfz;
	
	public int getNfz() {
		return nfz;
	}

	public void setNfz(int nfz) {
		this.nfz = nfz;
	}

	public String getLat() {
		return lat.get();
	}

	public void setLat(String lat) {
		this.lat.set(lat);
	}

	public String getLng() {
		return lng.get();
	}

	public void setLng(String lng) {
		this.lng.set(lng);
	}

	public Button getButton() {
		return button;
	}
	
	public void setButton(Button button) {
		this.button = button;
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
	public double getAltitude() {
		return altitude;
	}
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}
	public int getJumpNo() {
		return jumpNo;
	}
	public void setJumpNo(int jumpNo) {
		this.jumpNo = jumpNo;
	}
	public int getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(int repeatCount) {
		this.repeatCount = repeatCount;
	}
	public double getWaitingTime() {
		return waitingTime;
	}
	public void setWaitingTime(double waitingTime) {
		this.waitingTime = waitingTime;
	}
}
