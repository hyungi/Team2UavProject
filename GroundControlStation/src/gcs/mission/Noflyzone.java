package gcs.mission;

public class Noflyzone {
		double a;
		double b;
		double c;
		
	
	public double ifNoflyzone(double nX, double nY, double x1, double y1, double x2, double y2) {
		//1번WP = (x1,y1) 다음WP(x2,y2) noflyzone(nX,nY)
		x1=x1*111189.57696002942;
		x2=x2*111189.57696002942;
		y1=y1*88799.53629131494;
		y2=y2*88799.53629131494;
		nX=nX*111189.57696002942;
		nY=nY*88799.53629131494;
		a = (y1-y2)/(x1-x2);
		b = -1;
		c = -a*x1+y1;
		double m=0;
		double n=0;
		if(a*nX+b*nY+c>0){
			m = a*nX+b*nY+c;
		}else if (a*nX+b*nY+c<0){
			m= -a*nX+b*nY+c;
		}
		n = Math.sqrt(Math.pow(a, 2)+1);

		return m/n; // 리턴값이 반지름 +2보다 작으면 새로운 웨이포인트 생성 아니면 그냥 ㄱㄱ
	}
	
	
	/*public void waypoint(double x1, double y1, double x2, double y2, double nR) {
		double[] X = new double[300];
		double[] Yp = new double[300];
		double[] Ym = new double[300];
		if(a>0) {
			int count=0;
			for(double i=0;i<x2-x1;i+=0.1) {
				double root =Math.sqrt(Math.pow(nR+2, 2)-Math.pow(x1+i,2));
				if(String.valueOf(root)!="NaN") {
					if(a*(x1+i)+b*(root+y1)+c<=0) {
						X[count] = x1+i;
						Yp[count] = root+y1;
						count++;
						if(a*(x1+i)+b*(-root+y1)+c<=0) {
							Ym[count-1] = -root+y1;
						}
					}
				}
			}
		}else {
			int count=0;
			for(double i=0;i<x2-x1;i+=0.1) {
				double root =Math.sqrt(Math.pow(nR+2, 2)-Math.pow(x1+i,2));
				if(String.valueOf(root)!="NaN") {
					if(a*(x1+i)+b*(root+y1)+c>=0) {
						X[count] = x1+i;
						Yp[count] = root+y1;
						count++;
						if(a*(x1+i)+b*(-root+y1)+c>=0) {
							Ym[count-1] = -root+y1;
						}
					}
				}
			}
		}
	}*/

}