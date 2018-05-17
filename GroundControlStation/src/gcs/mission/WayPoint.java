package gcs.mission;

public class WayPoint {
	private int no;
	private String kind;
	private int repeat;
	private int next;
	private double lat;
	private double lng;
	private double alt;
	
	
	
	public WayPoint(int no, String kind, int next, int repeat,  double lat, double lng, double alt) {
		this.no = no;
		this.kind = kind;
		this.repeat = repeat;
		this.next = next;
		this.lat = lat;
		this.lng = lng;
		this.alt = alt;
	}
	
	public int getNext() {
		return next;
	}
	public void setNext(int next) {
		this.next = next;
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
	
	
}
