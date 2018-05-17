package gcs.mission;

public class WayPoint {
	public int no;
	public String kind;
	public int repeat;
	public int next;
	public double lat;
	public double lng;
	public double alt;
	public int jump;
	public int jumpnum;
	public double waitingTime;
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
	public double getLat() {
		return lat;
	}
	public void setLat(double lat) {
		this.lat = lat;
	}
	public double getLng() {
		return lng;
	}
	public void setLng(double lng) {
		this.lng = lng;
	}
	public double getAlt() {
		return alt;
	}
	public void setAlt(double alt) {
		this.alt = alt;
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
