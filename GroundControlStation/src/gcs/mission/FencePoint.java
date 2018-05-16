package gcs.mission;

public class FencePoint {
	private int idx;
	private double lat;
	private double lng;
	
	public FencePoint(int idx, double lat, double lng) {
		super();
		this.idx = idx;
		this.lat = lat;
		this.lng = lng;
	}
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
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
	
	
}
