package team2gcs.appmain;

public class test {

	public static void main(String[] args) {
		double a = getAngle(0,0,-1/2,1);
		double b = getAngle(0,0,1/2,-1/2);
		
		System.out.println(a);
		System.out.println(b);
		System.out.println(a-b);


	}
	private static double getAngle(double x1,double y1, double x2,double y2){
		   double dx = x2 - x1;
		   double dy = y2 - y1;
		   
		   double rad= Math.atan2(dy, dx);
		   double degree = (rad*180)/Math.PI ;
		 
		   return degree;
	}

}
