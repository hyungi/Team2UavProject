package gcs.mission;

public class Noflyzone {
	private	static double a;
	private static double b;
	private static double c;
		
	public static double ifNoflyzone(double nX, double nY, double x1, double y1, double x2, double y2) {
		//1번WP = (x1,y1) 다음WP(x2,y2) noflyzone(nX,nY)
		x1=x1*111189.57696002942;
		x2=x2*111189.57696002942;
		y1=y1;
		y2=y2;
		nX=nX*111189.57696002942;
		nY=nY;
		a = (y1-y2)/(x1-x2);
		b = -1;
		c = -a*x1+y1;
		double m=0;
		double n=0;
		if(a*nX+b*nY+c>0){
			m = a*nX+b*nY+c;
		}else{
			m= -a*nX+b*nY+c;
		}
		n = Math.sqrt(Math.pow(a, 2)+1);
		return m/n; // 리턴값이 반지름 +3보다 작으면 새로운 웨이포인트 생성 아니면 그냥 ㄱㄱ
	}
}