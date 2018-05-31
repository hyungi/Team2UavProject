package team2gcs.appmain;

public class test {
	public static double angle(double x1,double y1, double x2, double y2){
		   double dx = x2 - x1;
		   double dy = y2 - y1;
		   
		   double rad= Math.atan2(dy, dx);
		   double degree = (rad*180)/Math.PI ;
		   return degree;
	}
	
	public static void main(String args[]) {
		System.out.println(angle(0,0,-1,-1));
	}
}
