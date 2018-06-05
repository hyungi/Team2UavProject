/*package team2gcs.noflyzone;

import java.util.ArrayList;
import java.util.List;

import gcs.mission.WayPoint;
import javafx.application.Platform;
import team2gcs.altdialog.altdialogController;
import team2gcs.appmain.AppMainController;

public class Noflyzone {

	public static List<WayPoint> list;
	private static WayPoint tPoint;
	public static int s;
	public static int e;

		
	public static double ifNoflyzone(double nX, double nY, double x1, double y1, double x2, double y2) {
		
		nX=nX*88799.53629131494;
		nY=nY*111189.57696002942;
		x1=x1*88799.53629131494;
		y1=y1*111189.57696002942;
		x2=x2*88799.53629131494;
		y2=y2*111189.57696002942;
		System.out.println(distance(nX, nY, nX+1, nY));
		double a1 = (y2-y1)/(x2-x1);
		double b1 = -1;
		double c1 = -a1*x1+y1;
		double m=0;
		double n=0;

		m = Math.abs(a1*nX+b1*nY+c1);
		n = Math.sqrt(Math.pow(a1, 2)+Math.pow(b1, 2));
		
		System.out.println("거리: "+m/n);
		return m/n; // 리턴값이 반지름 +3보다 작으면 새로운 웨이포인트 생성 아니면 그냥 ㄱㄱ
	}
	
	public static void listchange() {
		list = AppMainController.list;
		tPoint = AppMainController.tPoint;
	}
	
	//거리
    private static double distance(double lon1, double lat1, double lon2, double lat2) {
        
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
         
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515* 1609.344;
        return dist;
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
    
 // 시계 방향 돌면서 WP 찍기 nX=noflyzone X좌표, nY=noflyzone Y좌표, WP1(x1,y1), WP(x2,y2)
 	public static void circleWP1(double nX,double nY, double nR,double x1,double y1,double x2,double y2,int no) {
 		System.out.println("circleWP1");
 		WayPoint tPoint = AppMainController.tPoint;
 		//a=alt값넣기 우리는 써서 넣음
 		List<WayPoint> beforeList = new ArrayList<>();
 		for(int i=0; i<list.size(); i++) {
 			if(list.get(i).getNo() <= tPoint.getNo())
 				beforeList.add(list.get(i));
 		}
 		List<WayPoint> afterList = new ArrayList<>();
 		for(int i=0; i<list.size(); i++) {
 			if(list.get(i).getNo() > tPoint.getNo())
 				afterList.add(list.get(i));
 		}
 		list.clear();
 			int nono=no;
 			//10도 간격으로 WP리스트에 넣기
 			for(int i=s; i<e; i+=10) {				
 				WayPoint wayPoint = new WayPoint();
 	 			wayPoint.no = nono;
 				wayPoint.kind = "waypoint";
 				wayPoint.setLat((nY+(nR*1.1)*Math.sin(Math.PI/180*i)/111189.57696002942)+"");
 				double xx = distance(nX, nY, nX+Math.cos(Math.PI/180*i), nY)/Math.cos(Math.PI/180*i);
 				if(xx<0) {
 					xx=-xx;
 				}
 				wayPoint.setLng((nX+(nR*1.1)*Math.cos(Math.PI/180*i)/xx)+"");
 				wayPoint.altitude = altdialogController.alt;
 				wayPoint.nfz=1;
 				wayPoint.getButton().setOnAction((event2)->{
 					list.remove(wayPoint.no-1);
 					for(WayPoint wp : list) {
 						if(wp.no>wayPoint.no) wp.no--;
 					}
 					AppMainController.instance2.setTableViewItems(list);
 					AppMainController.instance2.setMission(list);
 				});
 				list.add(wayPoint);
 				nono+=1;
 			}
 			List<WayPoint> resultList = new ArrayList<>();
 			for(WayPoint wp: beforeList)
 				resultList.add(wp);
 			beforeList.clear();
 			list.remove(0);
 			list.remove(0);
 			list.remove(list.size()-1);
 			list.remove(list.size()-1);
 			list.remove(list.size()-1);
 			for(WayPoint wp: list) {
 				wp.setNo(wp.getNo()-3);
 				resultList.add(wp);
 			}
 			list.clear();
 			for(WayPoint wp: afterList) {
 				wp.setNo(wp.getNo()+(nono-no-1)-5);
 				resultList.add(wp);
 			}
 			afterList.clear();
 			list = resultList;
 	 		Platform.runLater(() -> {	
 	 			AppMainController.instance2.setMission(list);
 	 			AppMainController.instance2.setTableViewItems(list);
 	 		});
 	}
 	//반시계방향
 	public static void circleWP2(double nX,double nY, double nR,double x1,double y1,double x2,double y2,int no) {
 		System.out.println("circleWP2");

 		//a=alt값넣기 우리는 써서 넣음
 		List<WayPoint> beforeList = new ArrayList<>();
 		for(int i=0; i<list.size(); i++) {
 			if(list.get(i).getNo() <= tPoint.getNo())
 				beforeList.add(list.get(i));
 		}
 		List<WayPoint> afterList = new ArrayList<>();
 		for(int i=0; i<list.size(); i++) {
 			if(list.get(i).getNo() > tPoint.getNo())
 				afterList.add(list.get(i));
 		}
 		list.clear();

		int nono=no;
		for(int i=s; i>e; i-=10) {
			WayPoint wayPoint = new WayPoint();
 			wayPoint.no = nono;
			wayPoint.kind = "waypoint";
			wayPoint.setLat((nY+(nR*1.1)*Math.sin(Math.PI/180*i)/111189.57696002942)+"");
			double xx = distance(nX, nY, nX+Math.cos(Math.PI/180*i), nY)/Math.cos(Math.PI/180*i);
			if(xx<0) {
				xx=-xx;
			}
			wayPoint.setLng((nX+(nR*1.1)*Math.cos(Math.PI/180*i)/xx)+"");
			wayPoint.altitude = altdialogController.alt;
			wayPoint.nfz=1;
			wayPoint.getButton().setOnAction((event2)->{
				list.remove(wayPoint.no-1);
				for(WayPoint wp : list) {
					if(wp.no>wayPoint.no) wp.no--;
				}
				AppMainController.instance2.setTableViewItems(list);
				AppMainController.instance2.setMission(list);
			});
			list.add(wayPoint);
			nono++;
		}
		List<WayPoint> resultList = new ArrayList<>();
		for(WayPoint wp: beforeList)
			resultList.add(wp);
		beforeList.clear();
		list.remove(0);
		list.remove(0);
		list.remove(list.size()-1);
		list.remove(list.size()-1);
		list.remove(list.size()-1);
		for(WayPoint wp: list) {
			wp.setNo(wp.getNo()-3);
			resultList.add(wp);
		}
		list.clear();
		for(WayPoint wp: afterList) {
			wp.setNo(wp.getNo()+(nono-no-1)-5);
			resultList.add(wp);
		}
		afterList.clear();
		list = resultList;
 		Platform.runLater(() -> {	
 			AppMainController.instance2.setMission(list);
 			AppMainController.instance2.setTableViewItems(list);
 		});
 	}

 	
 	//회전방향 case
 	public static void rotationCase(double nX,double nY,double x1,double y1,double x2,double y2) {
 		System.out.println("rotationCase");
 		double angle1 = angle(nX,nY,x1,y1);
 		double angle2 = angle(nX,nY,x2,y2);

 		if(angle1>=0&&angle1<=90) {
 			if(angle2>angle1&&angle2<180+angle1) {
 				AppMainController.instance2.rotation=false;
 				if(angle2<angle1){
 					angle2+=360;
 				}
 				s=(int)angle1;
 				e=(int)angle2;
 			}else {
 				if(angle2>angle1) {
 					angle2-=360;
 				}
 				AppMainController.instance2.rotation=true;
 				s=(int)angle1;
 				e=(int)angle2;
 			}
 		}else if(angle1>90&&angle1<=180) {
 			if(angle2>angle1&&angle2<180+angle1) {
 				AppMainController.instance2.rotation=false;
 				s=(int)angle1;
 				e=(int)angle2;
 			}else {
 				AppMainController.instance2.rotation=true;
 				if(angle2>angle1) {
 					angle2-=360;
 				}
 				s=(int)angle1;
 				e=(int)angle2;
 			}
 		}else if(angle1>180&&angle1<=270) {
 			if(angle2<angle1&&angle2>angle1-180) {
 				AppMainController.instance2.rotation=true;
 				s=(int)angle1;
 				e=(int)angle2;
 			}else {
 				AppMainController.instance2.rotation=false;
 				if(angle2<angle1) {
 					angle2+=360;
 				}
 				s=(int)angle1;
 				e=(int)angle2;
 			}
 		}else if(angle1>270&&angle1<360) {
 			if(angle2<angle1&&angle2>angle1-180) {
 				AppMainController.instance2.rotation=true;
 				s=(int)angle1;
 				e=(int)angle2;
 			}else {
 				AppMainController.instance2.rotation=false;
 				if(angle2<angle1) {
 					angle2+=360;
 				}
 				s=(int)angle1;
 				e=(int)angle2;
 			}
 		}
 	}
 	
 	//각도
 	public static double angle(double P1_longitude, double P1_latitude, double P2_longitude, double P2_latitude){
         // 현재 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
         double Cur_Lat_radian = P1_latitude * (Math.PI / 180);
         double Cur_Lon_radian = P1_longitude * (Math.PI / 180);


         // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
         double Dest_Lat_radian = P2_latitude * (Math.PI / 180);
         double Dest_Lon_radian = P2_longitude * (Math.PI / 180);
         double degree=0;
         // radian distance
         double radian_distance = 0;
         radian_distance = Math.acos(Math.sin(Cur_Lat_radian) * Math.sin(Dest_Lat_radian) + Math.cos(Cur_Lat_radian) * Math.cos(Dest_Lat_radian) * Math.cos(Cur_Lon_radian - Dest_Lon_radian));

         // 목적지 이동 방향을 구한다.(현재 좌표에서 다음 좌표로 이동하기 위해서는 방향을 설정해야 한다. 라디안값이다.
         double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math.sin(Cur_Lat_radian) * Math.cos(radian_distance)) / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));        // acos의 인수로 주어지는 x는 360분법의 각도가 아닌 radian(호도)값이다.        

         double true_bearing = 0;
         if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0)
         {
             true_bearing = radian_bearing * (180 / Math.PI);
             true_bearing = 360 - true_bearing;
         }
         else
         {
             true_bearing = radian_bearing * (180 / Math.PI);
         }
         if(true_bearing<270&&true_bearing>=180) {
         	degree=360-true_bearing+90;
         }else if(true_bearing<360&&true_bearing>270) {
         	degree=360-true_bearing+90;
         }else if(true_bearing>=0&&true_bearing<90) {
         	degree=90-true_bearing;
         }else if(true_bearing>=90&&true_bearing<180) {
         	degree=360-true_bearing+90;
         }
         
         
         return degree;
     }
}*/

package team2gcs.noflyzone;

import java.util.ArrayList;
import java.util.List;

import gcs.mission.WayPoint;
import javafx.application.Platform;
import team2gcs.altdialog.altdialogController;
import team2gcs.appmain.AppMainController;

public class Noflyzone {

	public static List<WayPoint> list;
	private static WayPoint tPoint;
	public static int s;
	public static int e;

		
	public static double ifNoflyzone(double nX, double nY, double x1, double y1, double x2, double y2) {
		
		nX=nX*88799.53629131494;
		nY=nY*111189.57696002942;
		x1=x1*88799.53629131494;
		y1=y1*111189.57696002942;
		x2=x2*88799.53629131494;
		y2=y2*111189.57696002942;
		System.out.println(distance(nX, nY, nX+1, nY));
		double a1 = (y2-y1)/(x2-x1);
		double b1 = -1;
		double c1 = -a1*x1+y1;
		double m=0;
		double n=0;

		m = Math.abs(a1*nX+b1*nY+c1);
		n = Math.sqrt(Math.pow(a1, 2)+Math.pow(b1, 2));
		
		System.out.println("거리: "+m/n);
		return m/n; // 리턴값이 반지름 +3보다 작으면 새로운 웨이포인트 생성 아니면 그냥 ㄱㄱ
	}
	
	public static void listchange() {
		list = AppMainController.list;
		tPoint = AppMainController.tPoint;
	}
	
	//거리
    private static double distance(double lon1, double lat1, double lon2, double lat2) {
        
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
         
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515* 1609.344;
        return dist;
    }

    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }
    
 	public static int k = 0;
	public static int j = 0;
 // 시계 방향 돌면서 WP 찍기 nX=noflyzone X좌표, nY=noflyzone Y좌표, WP1(x1,y1), WP(x2,y2)
 	public static void circleWP1(double nX,double nY, double nR,double x1,double y1,double x2,double y2,int no) {
 		System.out.println("circleWP1");
 		WayPoint tPoint = AppMainController.tPoint;
 		//a=alt값넣기 우리는 써서 넣음
 		List<WayPoint> beforeList = new ArrayList<>();
 		for(int i=0; i<list.size(); i++) {
 			if(list.get(i).getNo() <= tPoint.getNo())
 				beforeList.add(list.get(i));
 		}
 		List<WayPoint> afterList = new ArrayList<>();
 		for(int i=0; i<list.size(); i++) {
 			if(list.get(i).getNo() > tPoint.getNo())
 				afterList.add(list.get(i));
 		}
 		list.clear();
 			int nono=no;
 			//10도 간격으로 WP리스트에 넣기
 			for(int i=s; i<e; i+=10) {				
 				WayPoint wayPoint = new WayPoint();
 	 			wayPoint.no = nono;
 				wayPoint.kind = "waypoint";
 				wayPoint.setLat((nY+(nR*1.1)*Math.sin(Math.PI/180*i)/111189.57696002942)+"");
 				double xx = distance(nX, nY, nX+Math.cos(Math.PI/180*i), nY)/Math.cos(Math.PI/180*i);
 				if(xx<0) {
 					xx=-xx;
 				}
 				wayPoint.setLng((nX+(nR*1.1)*Math.cos(Math.PI/180*i)/xx)+"");
 				wayPoint.altitude = altdialogController.alt;
 				wayPoint.nfz=1;
 				wayPoint.getButton().setOnAction((event2)->{
 					list.remove(wayPoint.no-1);
 					for(WayPoint wp : list) {
 						if(wp.no>wayPoint.no) wp.no--;
 					}
 					AppMainController.instance2.setTableViewItems(list);
 					AppMainController.instance2.setMission(list);
 				});
 				list.add(wayPoint);
 				nono+=1;
 			}
 			List<WayPoint> resultList = new ArrayList<>();
 			for(WayPoint wp: beforeList)
 				resultList.add(wp);
 			beforeList.clear();
 			k = 0;
 			j = 0;
 			try {
	 			list.remove(0);
	 			k++;
	 			list.remove(0);
	 			k++;
	 			list.remove(list.size()-1);
	 			j++;
	 			list.remove(list.size()-1);
	 			j++;
	 			list.remove(list.size()-1);
	 			j++;
 			}catch(Exception e) {}
 			for(WayPoint wp: list) {
 				wp.setNo(wp.getNo()-j);
 				resultList.add(wp);
 			}
 			list.clear();
 			for(WayPoint wp: afterList) {
 				wp.setNo(wp.getNo()+(nono-no)-(k+j));
 				resultList.add(wp);
 			}
 			afterList.clear();
 			list = resultList;
// 	 		Platform.runLater(() -> {	
// 	 			AppMainController.instance2.setMission(list);
// 	 			AppMainController.instance2.setTableViewItems(list);
// 	 		});
 	}
 	

 	//반시계방향
 	public static void circleWP2(double nX,double nY, double nR,double x1,double y1,double x2,double y2,int no) {
 		System.out.println("circleWP2");

 		//a=alt값넣기 우리는 써서 넣음
 		List<WayPoint> beforeList = new ArrayList<>();
 		for(int i=0; i<list.size(); i++) {
 			if(list.get(i).getNo() <= tPoint.getNo())
 				beforeList.add(list.get(i));
 		}
 		List<WayPoint> afterList = new ArrayList<>();
 		for(int i=0; i<list.size(); i++) {
 			if(list.get(i).getNo() > tPoint.getNo())
 				afterList.add(list.get(i));
 		}
 		list.clear();

		int nono=no;
		for(int i=s; i>e; i-=10) {
			WayPoint wayPoint = new WayPoint();
 			wayPoint.no = nono;
			wayPoint.kind = "waypoint";
			wayPoint.setLat((nY+(nR*1.1)*Math.sin(Math.PI/180*i)/111189.57696002942)+"");
			double xx = distance(nX, nY, nX+Math.cos(Math.PI/180*i), nY)/Math.cos(Math.PI/180*i);
			if(xx<0) {
				xx=-xx;
			}
			wayPoint.setLng((nX+(nR*1.1)*Math.cos(Math.PI/180*i)/xx)+"");
			wayPoint.altitude = altdialogController.alt;
			wayPoint.nfz=1;
			wayPoint.getButton().setOnAction((event2)->{
				list.remove(wayPoint.no-1);
				for(WayPoint wp : list) {
					if(wp.no>wayPoint.no) wp.no--;
				}
				AppMainController.instance2.setTableViewItems(list);
				AppMainController.instance2.setMission(list);
			});
			list.add(wayPoint);
			nono++;
		}
		List<WayPoint> resultList = new ArrayList<>();
		for(WayPoint wp: beforeList)
			resultList.add(wp);
		beforeList.clear();
		k = 0;
		j = 0;
		try {
			list.remove(0);
			k++;
			list.remove(0);
			k++;
			list.remove(list.size()-1);
			j++;
			list.remove(list.size()-1);
			j++;
			list.remove(list.size()-1);
			j++;
		}catch(Exception e) {}
		for(WayPoint wp: list) {
			wp.setNo(wp.getNo()-j);
			resultList.add(wp);
		}
		list.clear();
		for(WayPoint wp: afterList) {
			wp.setNo(wp.getNo()+(nono-no)-(k+j));
			resultList.add(wp);
		}
		afterList.clear();
		list = resultList;
// 		Platform.runLater(() -> {	
// 			AppMainController.instance2.setMission(list);
// 			AppMainController.instance2.setTableViewItems(list);
// 		});
 	}

 	
 	//회전방향 case
 	public static void rotationCase(double nX,double nY,double x1,double y1,double x2,double y2) {
 		System.out.println("rotationCase");
 		double angle1 = angle(nX,nY,x1,y1);
 		double angle2 = angle(nX,nY,x2,y2);

 		if(angle1>=0&&angle1<=90) {
 			if(angle2>angle1&&angle2<180+angle1) {
 				AppMainController.instance2.rotation=false;
 				if(angle2<angle1){
 					angle2+=360;
 				}
 				s=(int)angle1;
 				e=(int)angle2;
 			}else {
 				if(angle2>angle1) {
 					angle2-=360;
 				}
 				AppMainController.instance2.rotation=true;
 				s=(int)angle1;
 				e=(int)angle2;
 			}
 		}else if(angle1>90&&angle1<=180) {
 			if(angle2>angle1&&angle2<180+angle1) {
 				AppMainController.instance2.rotation=false;
 				s=(int)angle1;
 				e=(int)angle2;
 			}else {
 				AppMainController.instance2.rotation=true;
 				if(angle2>angle1) {
 					angle2-=360;
 				}
 				s=(int)angle1;
 				e=(int)angle2;
 			}
 		}else if(angle1>180&&angle1<=270) {
 			if(angle2<angle1&&angle2>angle1-180) {
 				AppMainController.instance2.rotation=true;
 				s=(int)angle1;
 				e=(int)angle2;
 			}else {
 				AppMainController.instance2.rotation=false;
 				if(angle2<angle1) {
 					angle2+=360;
 				}
 				s=(int)angle1;
 				e=(int)angle2;
 			}
 		}else if(angle1>270&&angle1<360) {
 			if(angle2<angle1&&angle2>angle1-180) {
 				AppMainController.instance2.rotation=true;
 				s=(int)angle1;
 				e=(int)angle2;
 			}else {
 				AppMainController.instance2.rotation=false;
 				if(angle2<angle1) {
 					angle2+=360;
 				}
 				s=(int)angle1;
 				e=(int)angle2;
 			}
 		}
 	}
 	
 	//각도
 	public static double angle(double P1_longitude, double P1_latitude, double P2_longitude, double P2_latitude){
         // 현재 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
         double Cur_Lat_radian = P1_latitude * (Math.PI / 180);
         double Cur_Lon_radian = P1_longitude * (Math.PI / 180);


         // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
         double Dest_Lat_radian = P2_latitude * (Math.PI / 180);
         double Dest_Lon_radian = P2_longitude * (Math.PI / 180);
         double degree=0;
         // radian distance
         double radian_distance = 0;
         radian_distance = Math.acos(Math.sin(Cur_Lat_radian) * Math.sin(Dest_Lat_radian) + Math.cos(Cur_Lat_radian) * Math.cos(Dest_Lat_radian) * Math.cos(Cur_Lon_radian - Dest_Lon_radian));

         // 목적지 이동 방향을 구한다.(현재 좌표에서 다음 좌표로 이동하기 위해서는 방향을 설정해야 한다. 라디안값이다.
         double radian_bearing = Math.acos((Math.sin(Dest_Lat_radian) - Math.sin(Cur_Lat_radian) * Math.cos(radian_distance)) / (Math.cos(Cur_Lat_radian) * Math.sin(radian_distance)));        // acos의 인수로 주어지는 x는 360분법의 각도가 아닌 radian(호도)값이다.        

         double true_bearing = 0;
         if (Math.sin(Dest_Lon_radian - Cur_Lon_radian) < 0)
         {
             true_bearing = radian_bearing * (180 / Math.PI);
             true_bearing = 360 - true_bearing;
         }
         else
         {
             true_bearing = radian_bearing * (180 / Math.PI);
         }
         if(true_bearing<270&&true_bearing>=180) {
         	degree=360-true_bearing+90;
         }else if(true_bearing<360&&true_bearing>270) {
         	degree=360-true_bearing+90;
         }else if(true_bearing>=0&&true_bearing<90) {
         	degree=90-true_bearing;
         }else if(true_bearing>=90&&true_bearing<180) {
         	degree=360-true_bearing+90;
         }
         
         
         return degree;
     }
}
