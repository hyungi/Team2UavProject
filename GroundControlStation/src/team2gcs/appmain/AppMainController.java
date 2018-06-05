package team2gcs.appmain;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import gcs.mission.FencePoint;
import gcs.mission.WayPoint;
import gcs.network.Network;
import gcs.network.UAV;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.concurrent.Worker.State;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;
import netscape.javascript.JSObject;
import team2gcs.altdialog.altdialogController;
import team2gcs.leftpane.leftPaneController;
import team2gcs.noflyzone.NoFlyZoneController;
import team2gcs.noflyzone.Noflyzone;

public class AppMainController implements Initializable{	
	public static AppMainController instance2;
	// child들의 높이 조정을 위해
	public static double heightSize;
	public static Stage altStage;
	String strFenceArr;
	int jumpNo = 1;
	int repeatCount = 2;
	double altitude = altdialogController.alt;
	//공용

	@FXML private BorderPane mainBorderPane;
	@FXML private BorderPane loginBorderPane;
	String inTime;
    int takeoffH = 0, takeoffM = 0, takeoffS = 0;
    int missionH = 0, missionM = 0, missionS = 0;
	public static String missionTime;
	public static String takeoffTime;
	public static boolean missionStart = false;
	public static boolean takeoffStart = false;
    public static double gotoLat;
    public static double gotoLng;
	
	// 좌측
	@FXML private VBox leftPane;
	@FXML private Label mapButton;
	@FXML private Label satButton;
	@FXML private Label plusButton;
	@FXML private Label minusButton;
	private int zoom = 18;
	
	// 우측
	@FXML private VBox rightStatusPane;
	@FXML private AnchorPane rightCameraPane;
	@FXML private Label rightStatusLabel;
	@FXML private Label rightCameraLabel;
	@FXML private Button rightDeleteBtn;
	@FXML private ListView<String> statusListView; 
	List<String> statusList = new ArrayList<String>();
	String messageTemp = "messageTemp";
	
	// 아래 버튼 & Pane & 둘을 가지고있는 VBox & control 값
	@FXML private AnchorPane openBottom;
	@FXML private BorderPane missionPane;
	@FXML private VBox bottomMovePane;
	@FXML private Label bottomOpenLabel;
	private boolean bottomControl = true;	
	// 우측 버튼 & Pane & 둘을 가지고있는 HBox & control 값
	@FXML private AnchorPane openRight;
	@FXML private HBox rightMovePane;
	@FXML private Label rightOpenLabel;;
	private boolean rightControl = true;	
	//맵
	@FXML WebView webView;
	private WebEngine webEngine;
	public JSObject jsproxy;
	//로그인부분
	@FXML private TextField txtIP;
	@FXML private TextField txtPort;
	@FXML private Button btnConnect;
	@FXML private Button btnCancle;
	@FXML private Label loginLabel;
	public static String ip;
	public static String port;
	public static boolean connectState;
	
	//미션부분
	@FXML private Button btnMissionSet;
	@FXML private Button btnMissionRead;
	@FXML private Button btnMissionUpload;
	@FXML private Button btnMissionDownload;
	@FXML private Button btnMissionGoto;
	@FXML private Button btnMissionJump;
	@FXML private Button btnMissionLoi;
	@FXML private Button btnMissionDelete;
	@FXML private Button btnMissionRTL;
	@FXML private Button armBtn;
	@FXML private Button takeoffBtn;
	@FXML private Button landBtn;
	@FXML private Button rtlBtn;
	@FXML private Button loiterBtn;
	@FXML private Button btnMissionStart;
	@FXML private Button btnMissionStop;
	@FXML private TextField txtAlt;
	@FXML private Button btnTop;
	@FXML private Button btnRight;
	@FXML private Button btnBottom;
	@FXML private Button btnLeft;
	@FXML private Button btnHeadingToNorth;
	@FXML private Button btnMissionHomeWP;
	@FXML private Button btnMissionLand;
	@FXML private Button btnMissionTime;
	
	//펜스
	@FXML private Button btnFenceSet;
	@FXML private Button btnFenceUpload;
	@FXML private Button btnFenceDownload;
	@FXML private Button btnFenceActivate;
	@FXML private Button btnFenceDeactivate;
	@FXML private Button btnFenceDelete;
	
	//비행금지구역
	@FXML private Button btnNoflyzoneSet;
	@FXML private Button btnNoflyzoneDelete;
	@FXML private Button btnNoflyzoneActivate;
	public boolean rotation;
	
	//화물
	@FXML private Button btnCargoStart;
	@FXML private Button btnCargoStop;
	
	//미션 테이블 뷰
	@FXML private TableView<WayPoint> tableView;
     
   	// Pane을 움직이기 위해 Double 속성값을 사용 -> Listener를 등록가능
   	private DoubleProperty bottomPaneLocation 
   	= new SimpleDoubleProperty(this,"bottomPaneLocation");
   	private DoubleProperty rightPaneLocation
   	= new SimpleDoubleProperty(this,"rightPaneLocation");   

   	//상단 라벨
   	@FXML private Label currtimeLabel;
	@FXML private Label homeLatLabel;
	@FXML private Label homeLngLabel;
	@FXML private Label locationLatLabel;
	@FXML private Label locationLngLabel;
	@FXML private Label batteryLabel;
	@FXML private Label signalLabel;
	@FXML private ImageView connButton;
	public int alt;
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance2 = this;
		mainBorderPane.setVisible(false);
		loginBorderPane.setVisible(true);
		initWebView();
		initTableView();
		initMissionButton();
		initLoginButton();
		initSlide();
		initTop();
		initRightPane();
		heightSize = webView.getHeight();
		try {
			Parent leftRoot = FXMLLoader.load(getClass().getResource("../leftpane/left.fxml"));
			Parent cameraRoot = FXMLLoader.load(getClass().getResource("../camera/viewer/mjpgstreamviewer.fxml"));
			leftPane.getChildren().add(leftRoot);
			rightCameraPane.getChildren().add(cameraRoot);
		}catch (Exception e) {}
	}

//////////////////////////////////Top Menu 관련 ////////////////////////////////
	public void initTop() {
		homeLatLabel.setText("Disarmed");
		homeLngLabel.setText("Disarmed");
		locationLngLabel.setText("Disconnected");
		locationLatLabel.setText("Disconnected");
		batteryLabel.setText("0%");
		signalLabel.setText("No signal");
		// 연결 이벤트 클릭 관리
		connButton.setOnMouseClicked((event)->{
			if(connectState) {
				mainBorderPane.setVisible(false);
				loginBorderPane.setVisible(true);
				Network.getUav().disconnect();
				connectState = false;
			}
		});
		mapButton.setOnMouseClicked((event)->{
			Platform.runLater(() -> {
				jsproxy.call("setMapType",0);
			});
		});
		satButton.setOnMouseClicked((event)->{
			Platform.runLater(() -> {
				jsproxy.call("setMapType",1);
			});
		});
		plusButton.setOnMouseClicked((event)->{
			if(zoom != 19) {
				Platform.runLater(() -> {
					jsproxy.call("setMapZoom",++zoom);
				});
			}
		});
		minusButton.setOnMouseClicked((event)->{
			if(zoom != 3) {
				Platform.runLater(() -> {
					jsproxy.call("setMapZoom",--zoom);
				});
			}
		});
	}

	public void setZoomSliderValue(int zoom) {
		this.zoom = zoom;
	}

	public void currTime() {
		inTime   = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
		currtimeLabel.setText(inTime);
		missionTime();
		takeoffTime();
	}

	public void missionTime() {
		if(missionStart) {
			missionS++;
			if(missionS==60) {
				missionM++;
				missionS = 00;
			} if(missionM==60) {
				missionH++;
				missionM = 00;
			}
			missionTime = String.format("%02d:%02d:%02d", missionH, missionM, missionS);
		}
	}

	public void takeoffTime() {
		if(takeoffStart) {
			takeoffS++;
			if(takeoffS==60) {
				takeoffM++;
				takeoffS = 00;
			} if(takeoffM==60) {
				takeoffH++;
				takeoffM = 00;
			}
		}
		takeoffTime = String.format("%02d:%02d:%02d", takeoffH, takeoffM, takeoffS);
	}

////////////////////////////////// Slide Menu 관련 ////////////////////////////////
	public void initSlide() {
		// 맨 처음 값을 200(닫혀있음)으로 만듬
		bottomPaneLocation.set(200);
		rightPaneLocation.set(350);
		// 열닫힘 버튼 클릭 이벤트 등록
		openBottom.setOnMouseClicked(event -> {
		animateBottomPane();
		});
		openRight.setOnMouseClicked(event->{
			animateRightPane();
		});
		// 위에서 선언한 Double 속성값의 Listener 등록
		bottomPaneLocation.addListener(change -> updateVBox());
		rightPaneLocation.addListener(change -> updateHBox());
	}

   // 각 전체 Pane의 위치를 property 값 만큼 변경(SlidePane 메소드를 통해 차례로 변경된 값이 적용됨)
	private void updateVBox() { bottomMovePane.setTranslateY(bottomPaneLocation.get());}

	private void updateHBox() {	rightMovePane.setTranslateX(rightPaneLocation.get());}

	// animate 메소드는 현재 상태를 파악하여 어느위치로 이동시켜야되는지 slidePane에게 전달해준다.
	private void animateRightPane() {
		if(rightControl) {
			// bottomPane이 열려있으면 닫아줌 -> fxThread로 안돌리면 오류난다.
			if(!bottomControl) Platform.runLater(()->animateBottomPane());
			rightOpenLabel.setText("Close");
			slidePane(200,rightPaneLocation);
		}else {
			rightOpenLabel.setText("Open");
			slidePane(550,rightPaneLocation);
		}
	}

	private void animateBottomPane() {
		if(bottomControl) {
			// rightPane이 열려있으면 닫아줌
			if(!rightControl) Platform.runLater(()->animateRightPane());
			bottomOpenLabel.setText("Close Mission");
			slidePane(0,bottomPaneLocation);
		}else {
			bottomOpenLabel.setText("Open Mission");
			slidePane(200,bottomPaneLocation);
		}
	}
   
	// slidePane은 property의 값을 property에 있는 현재 위치값에서부터 to 변수로 주어진 값만큼 숫자를 0.3초 동안 올려주거나 내려준다.
	// 여기서 property의 값이 변경되면 initSlide()에 등록한 Listener가 실행되고 각 Box의 위치를 조절한다.
	// KeyValue를 통해 property의 현재 값을 -> to로 변경시키겠다.
	// KeyFrame을 통해 keyValue를 0.3초 동안 변경시키겠다.
	// Timeline을 통해 keyFrame의 값을 가지고 Animation play
	private void slidePane(double to, DoubleProperty property) {
		KeyValue keyValue = new KeyValue(property, to);
		KeyFrame keyFrame = new KeyFrame(Duration.millis(300),keyValue);
		Timeline timeline = new Timeline(keyFrame);
		timeline.play();
		// 위치 수정이 끝나면 여닫힘 상태를 갱신
		timeline.setOnFinished((event)->{
			if(property.equals(bottomPaneLocation))   bottomControl = !bottomControl;
			else rightControl = !rightControl;
		});
	}
   
	//맵////////////////////////////////////////////////////////////////////////////////
	private void initWebView() {
		webEngine = webView.getEngine();
		webEngine.getLoadWorker().stateProperty().addListener(webEngineLoadStateListener);	
		webEngine.load(getClass().getResource("javascript/map.html").toExternalForm());
	}
	
   private ChangeListener<State> webEngineLoadStateListener = (observable, oldValue, newValue) -> {
	      if(newValue == State.SUCCEEDED) {
	         Platform.runLater(() -> {
	            try {
	               webEngine.executeScript("console.log = function(message) { jsproxy.java.log(message); };");
	               jsproxy = (JSObject) webEngine.executeScript("jsproxy");
	               jsproxy.setMember("java", AppMainController.this);
	               //setMapSize();
	            } catch(Exception e) {
	               e.printStackTrace();
	            }
	         });
	      }
	   };
  

	//로그인 버튼////////////////////////////////////////////////////////////////////////////////////////
	public void initLoginButton() {
		btnConnect.setOnAction((event)->{handleConnect(event);});
		btnCancle.setOnAction((event)->{handleCancle(event);});
	}
	
	//로그인화면 연결 버튼 이벤트 처리
	public void handleConnect(ActionEvent event) {
		//ip,port 보내기
		ip=txtIP.getText();
		port=txtPort.getText();

		if(!ip.equals("")&&!port.equals("")) {
			Network.connect();
			mainBorderPane.setVisible(true);
			loginBorderPane.setVisible(false);
		} else {
			loginLabel.setText("Broker IP와 Port 모두 입력하세요.");
		}
		statusMessage("MQTT broker connected.");
	}
	// 로그인화면 취소 버튼 이벤트처리
	public void handleCancle(ActionEvent event) {
		System.exit(0);
	}
	
	//미션부분 버튼
	public void initMissionButton() {
		btnMissionSet.setOnAction((event)->{handleMissionSet(event);});
		btnMissionRead.setOnAction((event)->{handleMissionRead(event);});
		btnMissionUpload.setOnAction((event)->{handleMissionUpload(event);});
		btnMissionDownload.setOnAction((event)->{handleMissionDownload(event);});
		btnMissionGoto.setOnAction((event)->{handleMissionGoto(event);});
		btnMissionJump.setOnAction((event)->{handleMissionJump(event);});
		btnMissionLoi.setOnAction((event)->{handleMissionLoi(event);});
		btnMissionDelete.setOnAction((event)->{handleMissionDelete(event);});
		btnMissionRTL.setOnAction((event)->{handleMissionRTL(event);});
		armBtn.setOnAction((event)->{try {handleArm();} catch (Exception e) {}});
		armBtn.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35)));
		takeoffBtn.setOnAction((event)->{try{handleTakeoff();}catch(Exception e) {}});
		landBtn.setOnAction((event)->{handleLand();});
		loiterBtn.setOnAction((event)->{handleLoiter(event);});
		rtlBtn.setOnAction((event)->{handleRtl(event);});
		btnFenceSet.setOnAction((event)->{handleFenceSet(event);});
		btnFenceUpload.setOnAction((event)->{handleFenceUpload(event);});
		btnFenceDownload.setOnAction((event)->{handleFenceDownload(event);});
		btnFenceActivate.setOnAction((event)->{handleFenceActivate(event);});
		btnFenceDeactivate.setOnAction((event)->{handleFenceDeactivate(event);});
		btnFenceDelete.setOnAction((event)->{handleFenceDelete(event);});
		btnNoflyzoneSet.setOnAction((event)->{handleNoflyzoneSet(event);});
		btnNoflyzoneDelete.setOnAction((event)->{handleNoflyzoneDelete(event);});
		btnMissionStart.setOnAction((event)->{handleMissionStart(event);});
		btnMissionStop.setOnAction((event)->{handleMissionStop(event);});
		btnCargoStart.setOnAction((event)->{handleCargoStart(event);});
		btnCargoStop.setOnAction((event)->{handleCargoStop();});
		btnNoflyzoneActivate.setOnAction((event)->{handleNoflyzoneActivate(event);});
		btnMissionHomeWP.setOnAction((event)->{handleMissionHomeWP();});
		btnMissionLand.setOnAction((event)->{handleMissionLand();});
		btnMissionTime.setOnAction((event)->{handleMissionTime();});
		btnTop.setOnAction((event)->{handleBtnTop(event);});
		btnRight.setOnAction((event)->{handleBtnRight(event);});
		btnBottom.setOnAction((event)->{handleBtnBottom(event);});
		btnLeft.setOnAction((event)->{handleBtnLeft(event);});
		btnHeadingToNorth.setOnAction((event)->{handleBtnHeadingToNorth(event);});
	}
	public static boolean wait;
	public static WayPoint tPoint;
	public static WayPoint tPoint2;
	public static List<WayPoint> listCP = new ArrayList<WayPoint>();
	
	public void handleMissionTime() {
//		30초 대기
		try{Thread.sleep(30000);}catch(Exception e) {}
	}
	
	public static boolean checkLand = false;
	public static int landNum = -999;
	public static int lastNum = -999;
	public void handleMissionLand() {
		checkLand = !checkLand;
		if(checkLand) btnMissionLand.setStyle("-fx-text-fill: #55FF55;");
		else btnMissionLand.setStyle("-fx-text-fill: white;");
		Platform.runLater(() -> {
			jsproxy.call("landMake");
		});
		statusMessage("Land made.");
	}
	
	//홈위치WP
	public void handleMissionHomeWP() {
		if(Network.getUav().homeLat!=0&&Network.getUav().homeLng!=0) {
			WayPoint wp = new WayPoint();
			wp.no=list.size()+1;
			wp.kind = "waypoint";
			wp.setLat(Network.getUav().homeLat+"");
			wp.setLng(Network.getUav().homeLng+"");
			wp.getButton().setOnAction((event)->{
				list.remove(wp.no-1);
				for(WayPoint wp1 : list) {
					if(wp1.no>wp.no) wp1.no--;
				}
				setTableViewItems(list);
				setMission(list);
			});
			list.add(list.size(),wp);
			setMission(list);
			setTableViewItems(list);
		}
	}
	
	public void handleNoflyzoneActivate(ActionEvent event) {		
		WayPoint wp = new WayPoint();
		wp.no=0;
		wp.kind = "waypoint";
		wp.setLat(Network.getUav().latitude+"");
		wp.setLng(Network.getUav().longitude+"");
		wp.nfz=2;
		list.add(0,wp);

		
		for(int i=0;i<list.size()-1;i++) {
			//WP1(x1,y1), WP2(x2,y2)
			System.out.println("i ==== "+i);
			tPoint = list.get(i);
			tPoint2 = list.get(i+1);
			Noflyzone.listchange();
			double x1 = Double.parseDouble(list.get(i).getLng());
			double y1 = Double.parseDouble(list.get(i).getLat());
			double x2 = Double.parseDouble(list.get(i+1).getLng());
			double y2 = Double.parseDouble(list.get(i+1).getLat());
			
			//Noflyzone x,y,r이 입력 되였냐
			if(NoFlyZoneController.instance.x!=0&&NoFlyZoneController.instance.y!=0&&NoFlyZoneController.instance.r!=0) {
				//Noflyzone안에 wp선이 들어 오냐
				if(Noflyzone.ifNoflyzone(NoFlyZoneController.instance.x,NoFlyZoneController.instance.y,x1,y1,x2,y2)<=NoFlyZoneController.instance.r*1.1) {
					// 시계 or 반시계
					Noflyzone.rotationCase(NoFlyZoneController.instance.x,NoFlyZoneController.instance.y,x1,y1,x2,y2);
					int s = Noflyzone.s;
					int e = Noflyzone.e;
					// 반시계 로 돈다면 여기
					if(!rotation) {
						Noflyzone.circleWP1(NoFlyZoneController.instance.x,NoFlyZoneController.instance.y,NoFlyZoneController.instance.r,x1,y1,x2,y2,i+2);
						list = Noflyzone.list;
						i+=(int)((e-s)/10) +1-(Noflyzone.k+Noflyzone.j);
					// 시계 로 돈다면 여기
					}else{
						System.out.println("반시계");
						Noflyzone.circleWP2(NoFlyZoneController.instance.x,NoFlyZoneController.instance.y,NoFlyZoneController.instance.r,x1,y1,x2,y2,i+2);
						list = Noflyzone.list;
						i += (int)((s-e+1)/10)+1-(Noflyzone.k+Noflyzone.j);

					}
				}
			}
		}
		for(int a=0;a<list.size();a++) {
			if(list.get(a).no==0) {
				list.remove(0);
			}
		}
		setMission(list);
		setTableViewItems(list);
	}
	
	public void handleBtnTop(ActionEvent e) {
		Network.getUav().move(5, 0, 0, 0.5);
	}

	public void handleBtnBottom(ActionEvent e) {
		Network.getUav().move(-5, 0, 0, 0.5);
	}

	public void handleBtnRight(ActionEvent e) {
		Network.getUav().move(0, 5, 0, 0.5);
	}

	public void handleBtnLeft(ActionEvent e) {
		Network.getUav().move(0, -5, 0, 0.5);
	}

	public void handleBtnHeadingToNorth(ActionEvent e) {
		Network.getUav().changeHeading(0);
	}
	//화물 부착 시작,끝
	public void handleCargoStart(ActionEvent event) {
		Network.getUav().cargo("cargoStart");
	}
	public void handleCargoStop() {
		Network.getUav().cargo("cargoStop");
	}
	//미션 삭제
	public void handleMissionDelete(ActionEvent event) {
		list.clear();
		setTableViewItems(list);
		setMission(list);
		statusMessage("Mission deleted.");
	}
	//미션 RTL 추가
	public void handleMissionRTL(ActionEvent event) {
		WayPoint waypoint = new WayPoint();
		waypoint.kind = "rtl";
		waypoint.setLat(Network.getUav().homeLat +"");
		waypoint.setLng(Network.getUav().homeLng+"");
		tableView.getItems().add(waypoint);
		waypoint.no = tableView.getItems().size();
		Platform.runLater(() -> {
			jsproxy.call("addRTL");
		});
	}
	//미션 시작 정지
	public void handleMissionStart(ActionEvent event) {
		Network.getUav().missionStart();
		Platform.runLater(() -> {
			jsproxy.call("missionStart");
		});
		statusMessage("Mission started.");
		missionStart = true;
	}
	public void handleMissionStop(ActionEvent event) {
		Network.getUav().missionStop();
		Platform.runLater(() -> {
			jsproxy.call("missionStop");
		});
		missionStart = false;
		missionH = 0; missionM = 0; missionS = 0;
		statusMessage("Mission stopped.");
	}
	//펜스 이벤트 처리
	public void handleFenceSet(ActionEvent event) {
		Platform.runLater(() -> {
			jsproxy.call("fenceMake");
		});
		statusMessage("Fence data set.");
	}
	public void fenceUpload(String jsonFencePoints) {
		Network.getUav().fenceUpload(jsonFencePoints);
	}
	public void handleFenceUpload(ActionEvent event) {
		jsproxy.call("fenceUpload");
		statusMessage("Fence data uploaded.");
	}
	public void handleFenceDownload(ActionEvent event) {
		if(strFenceArr == null) statusMessage("No Fence.");
		else {
			Network.getUav().fenceDownload();
			statusMessage("Fence data downloaded.");
		}
	}
	public void handleFenceActivate(ActionEvent event) {
		Network.getUav().fenceEnable();
		statusMessage("Fence activated.");
	}
	public void handleFenceDeactivate(ActionEvent event) {
		Network.getUav().fenceDisable();
		statusMessage("Fence disactivated.");
	}
	public void handleFenceDelete(ActionEvent event) {
		Network.getUav().fenceClear();
		jsproxy.call("fenceClear");
		statusMessage("Fence deleted.");
	}
	//비행금지구역 이벤트 처리
	public void handleNoflyzoneSet(ActionEvent event) {
		try {
			Stage dialog = new Stage();
			dialog.setTitle("NoFlyZone");
			dialog.initModality(Modality.APPLICATION_MODAL);
			dialog.initOwner(AppMain.instance.primaryStage);
			Parent parent = FXMLLoader.load(getClass().getResource("../noflyzone/noflyzone.fxml"));
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("../images/app.css").toExternalForm());
			dialog.setScene(scene);
			dialog.setResizable(false);
			dialog.show();
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void handleNoflyzoneDelete(ActionEvent event) {
		statusMessage("No-fly zone deleted.");
		Platform.runLater(() -> {
			jsproxy.call("deleteNoFlyZone");
		});
		NoFlyZoneController.instance.x=0;
		NoFlyZoneController.instance.y=0;
		NoFlyZoneController.instance.r=0;
		for(int i=0;i<list.size();i++) {
			if(list.get(i).nfz==1) {
				list.remove(i);
				i-=1;
			}else {
				list.get(i).no=i+1;
			}

		}
		setMission(list);
		setTableViewItems(list);
	}
	
	//Arm, Takeoff, Land, Roiter, Rtl
	public void handleArm() throws Exception {
		Network.getUav().arm();
	}
	public void handleTakeoff() throws Exception {
		if(Network.getUav().armed) {
			altStage = new Stage();
			altStage.setTitle("Altitude Setting.");
			altStage.initModality(Modality.WINDOW_MODAL);
			altStage.initOwner(AppMain.primaryStage);
			Parent root = FXMLLoader.load(getClass().getResource("../altdialog/altdialog.fxml"));
			Scene scene = new Scene(root);
			altStage.setScene(scene);

			altStage.show();
		} else statusMessage("Arm before taking off.");
	}
	public void handleLand() {
		Network.getUav().land();
		takeoffStart = false;
		statusMessage("UAV land.");
	}
	public void handleLoiter(ActionEvent event) {
		System.out.println("로이터 모드 실행 그러나 코딩 안함");
		statusMessage("UAV loiter mode.");
	}
	public void handleRtl(ActionEvent event) {
		Network.getUav().rtl();
		Platform.runLater(() -> {
			jsproxy.call("rtlStart");
		});
		statusMessage("UAV rtl mode.");
	}
	
	//미션생성 이벤트 처리
	public void handleMissionSet(ActionEvent event) {
		Platform.runLater(() -> {
			jsproxy.call("missionMake");
		});
		statusMessage("Mission set.");
	}

	//미션읽기
	public void handleMissionRead(ActionEvent event) {
		Platform.runLater(() -> {
			jsproxy.call("getMission");
		});
		statusMessage("Mission read.");
	}
	
	// List를 계속 관리하기 위해서 Field 영역으로 가져옴
	public static List<WayPoint> list = new ArrayList<>();
	public void getMissionResponse(String data) {
		double alt = altdialogController.alt;
		list.clear();
		Platform.runLater(() -> {	
			JSONArray jsonArray = new JSONArray(data);
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				WayPoint wayPoint = new WayPoint();
	 			wayPoint.no = jsonObject.getInt("no");
				wayPoint.kind = jsonObject.getString("kind"); //all is "waypoint";
				wayPoint.setLat(jsonObject.getDouble("lat")+"");
				wayPoint.setLng(jsonObject.getDouble("lng")+"");
				wayPoint.altitude = altitude;
				wayPoint.altitude = alt;
				wayPoint.getButton().setOnAction((event)->{
					list.remove(wayPoint.no-1);
					for(WayPoint wp : list) {
						if(wp.no>wayPoint.no) wp.no--;
					}
					setTableViewItems(list);
					setMission(list);
				});
				list.add(wayPoint);
			}
			setTableViewItems(list);
		});
	}
	public void setTableViewItems(List<WayPoint> list) {
 		tableView.getItems().clear();
		tableView.setItems(FXCollections.observableArrayList(list));
	}
	
	
	public static boolean uploadState = false;
	//미션 업로드
	public void handleMissionUpload(ActionEvent event) {
		List<WayPoint> list = tableView.getItems();
		Network.getUav().missionUpload(list);
		statusMessage("Mission uploaded.");
	}
	
	//미션 다운로드
	public void handleMissionDownload(ActionEvent event) {
		Network.getUav().missionDownload();
		statusMessage("Mission downloaded.");
	}
	
	//미션 바로가기
	public void handleMissionGoto(ActionEvent event) {
		Platform.runLater(() -> {
			jsproxy.call("gotoMake");
		});
	}
	
	//미션 점프
	public void handleMissionJump(ActionEvent event) {
		addJump();
		statusMessage("Jump added.");
	}
	
	private void addJump() {
		WayPoint waypoint = new WayPoint();
		waypoint.kind = "jump";
		waypoint.no = list.size()+1;
		waypoint.jumpNo = jumpNo;
		waypoint.repeatCount = repeatCount;
		list.add(waypoint);
		setTableViewItems(list);
	}
	
	//미션 Loi
	public void handleMissionLoi(ActionEvent event) {
		roiMake();
		statusMessage("Roi made.");
	}
	private void roiMake() {
		int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
		
		WayPoint wp = tableView.getItems().get(selectedIndex);
		wp = tableView.getItems().get(selectedIndex+1);
		
		Platform.runLater(() -> {
			jsproxy.call("roiMake", selectedIndex);
		});
	}
	
	public void addROI(String data) {
		Platform.runLater(() -> {
			WayPoint waypoint = new WayPoint();
			waypoint.kind = "roi";
			JSONObject jsonObject = new JSONObject(data);
			waypoint.setLat(jsonObject.getDouble("lat")+"");
			waypoint.setLng(jsonObject.getDouble("lng")+"");
			int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
			if(selectedIndex != tableView.getItems().size()-1) {
				tableView.getItems().add(selectedIndex+1, waypoint);
			} else {
				tableView.getItems().add(waypoint);
			}
			for(int i=0; i<tableView.getItems().size(); i++) {
				WayPoint wp = tableView.getItems().get(i);
				wp.no = i+1;
			}
		});
		statusMessage("ROI added.");
	}		
	
	//테이블뷰 설정////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void initTableView() {
		tableView.setEditable(true);
		TableColumn<WayPoint, Integer> column1 = new TableColumn<WayPoint, Integer>("No");
		column1.setCellValueFactory(new PropertyValueFactory<WayPoint, Integer>("no"));
		column1.setPrefWidth(50);
		column1.setSortable(false);
		column1.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column1);

		TableColumn<WayPoint, String> column2 = new TableColumn<WayPoint, String>("Command");
		column2.setCellValueFactory(new PropertyValueFactory<WayPoint, String>("kind"));
		column2.setPrefWidth(200);
		column2.setSortable(false);
		column2.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column2);

		TableColumn<WayPoint, String> column3 = new TableColumn<WayPoint, String>("Latitude");
		column3.setCellValueFactory(new PropertyValueFactory<WayPoint, String>("lat"));
		column3.setEditable(true);
		column3.setCellFactory(TextFieldTableCell.forTableColumn());
		column3.setOnEditCommit((target)->{
			target.getTableView().getItems().get(
					target.getTablePosition().getRow()).setLat(target.getNewValue());
			setMission(list);
		});
		column3.setPrefWidth(200);
		column3.setSortable(false);
		column3.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column3);

		TableColumn<WayPoint, String> column4 = new TableColumn<WayPoint, String>("Longitude");
		column4.setCellValueFactory(new PropertyValueFactory<WayPoint, String>("lng"));
		column4.setEditable(true);
		column4.setCellFactory(TextFieldTableCell.forTableColumn());
		column4.setOnEditCommit((target)->{
			target.getTableView().getItems().get(
				target.getTablePosition().getRow()).setLng(target.getNewValue());
			setMission(list);
		});
		column4.setPrefWidth(200);
		column4.setSortable(false);
		column4.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column4);

		TableColumn<WayPoint, Double> column5 = new TableColumn<WayPoint, Double>("Altitude");
		column5.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("altitude"));
		column5.setEditable(true);
		column5.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
		column5.setOnEditCommit((target)->{
			target.getTableView().getItems().get(
					target.getTablePosition().getRow()).setAltitude(target.getNewValue());
			setMission(list);
		});
		column5.setPrefWidth(200);
		column5.setSortable(false);
		column5.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column5);

		TableColumn<WayPoint, Integer> column6 = new TableColumn<WayPoint, Integer>("JumpNo");
		column6.setCellValueFactory(new PropertyValueFactory<WayPoint, Integer>("jumpNo"));
		column6.setEditable(true);
		column6.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		column6.setOnEditCommit((target)->{
			target.getTableView().getItems().get(
					target.getTablePosition().getRow()).setJumpNo(target.getNewValue());
			setMission(list);
		});
		column6.setPrefWidth(80);
		column6.setSortable(false);
		column6.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column6);

		TableColumn<WayPoint, Integer> column7 = new TableColumn<WayPoint, Integer>("RepeatCount");
		column7.setCellValueFactory(new PropertyValueFactory<WayPoint, Integer>("repeatCount"));
		column7.setEditable(true);
		column7.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
		column7.setOnEditCommit((target)->{
			target.getTableView().getItems().get(
					target.getTablePosition().getRow()).setRepeatCount(target.getNewValue());
			setMission(list);
		});
		column7.setPrefWidth(80);
		column7.setSortable(false);
		column7.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column7);
		
		TableColumn<WayPoint, Double> column8 = new TableColumn<WayPoint, Double>("Waiting Time");
		column8.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("waitingTime"));
		column8.setPrefWidth(80);
		column8.setSortable(false);
		column8.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column8);

		TableColumn<WayPoint, Button> column9 = new TableColumn<WayPoint, Button>("Delete");
		column9.setCellValueFactory(new PropertyValueFactory<>("button"));
		column9.setPrefWidth(80);
		column9.setSortable(false);
		column9.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column9);
	}
	

	public void viewStatus(UAV uav) {
		try {
			setStatus(uav);
			setMissionStatus(uav);
			leftPaneController.instance.getRollStatus(uav);
			leftPaneController.instance.getStatus(uav);
			Platform.runLater(new Runnable() {
				@Override
				public void run() {
					leftPaneController.instance.setRollStatus();
					leftPaneController.instance.setStatus();
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setMissionStatus(UAV uav) {		
		Platform.runLater(() -> {
			if(uav.homeLat >= 10) {
				jsproxy.call("setHomeLocation", uav.homeLat, uav.homeLng);
				homeLatLabel.setText(String.format("Lat:	%.6f", uav.homeLat));
				homeLngLabel.setText(String.format("Lng:	%.6f", uav.homeLng));
				jsproxy.call("setUavLocation", uav.latitude, uav.longitude, uav.heading);
			}
			
			if(uav.wayPoints.size() != 0) {
				setMission(uav.wayPoints);
			} 
			
			jsproxy.call("setNextWaypointNo", UAV.nextWP);			
			
			if(Network.getUav().mode.equals("AUTO")) {
				for(int i=0; i<tableView.getItems().size(); i++) {
					WayPoint wp = tableView.getItems().get(i);
					if(wp.no == uav.nextWaypointNo) {
						tableView.getSelectionModel().select(wp);
					}
				}
			}
			
			if(uav.fenceEnable == 0) {
				btnFenceActivate.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35)));
			} else {
				btnFenceActivate.setGraphic(new Circle(5, Color.RED)); 
			}
			
			if(uav.fencePoints.size() != 0) {
				setFence(uav.fencePoints);
			}
		});
	}
	public void setStatus(UAV uav) {
		Platform.runLater(() -> {
			if(uav.connected) {
				if(uav.armed) {
					armBtn.setText("Disarm");
					armBtn.setGraphic(new Circle(5, Color.RED)); 
				} else {
					armBtn.setText("Arm");
					armBtn.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35)));
				}
			}
		});
	}
	
	public void setMission(List<WayPoint> wayPoints) {
		setTableViewItems(wayPoints);
		JSONArray jsonArray = new JSONArray();
		for(WayPoint wayPoint : wayPoints) {
				JSONObject jsonObject = new JSONObject();
				if(wayPoint.kind.equals("takeoff")) {
					jsonObject.put("kind",  wayPoint.kind);
					jsonObject.put("lat", Network.getUav().homeLat);
					jsonObject.put("lng", Network.getUav().homeLng);
				} else if(wayPoint.kind.equals("waypoint")) {
					jsonObject.put("kind",  wayPoint.kind);
					jsonObject.put("lat", Double.parseDouble(wayPoint.getLat()));
					jsonObject.put("lng", Double.parseDouble(wayPoint.getLng()));
				} else if(wayPoint.kind.equals("jump")) {
					jsonObject.put("kind",  wayPoint.kind);
					jsonObject.put("seq", wayPoint.jumpNo);
					jsonObject.put("cnt", wayPoint.repeatCount);
				} else if(wayPoint.kind.equals("rtl")) {
					jsonObject.put("kind",  wayPoint.kind);
					jsonObject.put("lat", Network.getUav().homeLat);
					jsonObject.put("lng", Network.getUav().homeLng+0.00005);
				} else if(wayPoint.kind.equals("roi")) {
					jsonObject.put("kind",  wayPoint.kind);
					jsonObject.put("lat", Double.parseDouble(wayPoint.getLat()));
					jsonObject.put("lng", Double.parseDouble(wayPoint.getLng()));
				} else if(wayPoint.kind.equals("land")) {
					landNum = wayPoint.no;
					jsonObject.put("kind",  wayPoint.kind);
					jsonObject.put("lat", Double.parseDouble(wayPoint.getLat()));
					jsonObject.put("lng", Double.parseDouble(wayPoint.getLng()));
				} else if(wayPoint.kind.equals("arm")) {
					jsonObject.put("kind",  wayPoint.kind);
					jsonObject.put("lat", Double.parseDouble(wayPoint.getLat()));
					jsonObject.put("lng", Double.parseDouble(wayPoint.getLng()));
				}
				jsonArray.put(jsonObject);
		}
			String strMissionArr = jsonArray.toString();
			Platform.runLater(() -> {
				jsproxy.call("setMission", strMissionArr);
			});
			statusMessage("Mission set.");
	}
	
	public void setFence(List<FencePoint> fencePoints) {
		JSONArray jsonArray = new JSONArray();
		for(FencePoint fencePoint : fencePoints) {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("idx", fencePoint.idx);
			jsonObject.put("count", fencePoint.count);
			jsonObject.put("lat", fencePoint.lat);
			jsonObject.put("lng", fencePoint.lng);
			jsonArray.put(jsonObject);
		}
		strFenceArr = jsonArray.toString();
		Platform.runLater(() -> {
			jsproxy.call("setFence", strFenceArr);
		});
		statusMessage("Set Fence.");
	}
	
	public void log(String message) {
		System.out.println(message);
	}
	
	///////////////////////////// 미션 관련 //////////////////////////////////////
	public void gotoStart(String data) {
		Platform.runLater(() -> {
			JSONObject jsonObject = new JSONObject(data);
			gotoLat = jsonObject.getDouble("lat");
			gotoLng = jsonObject.getDouble("lng");
			Network.getUav().gotoStart(gotoLat, gotoLng, altitude);
		});
		statusMessage("Go to!");
	}

	public void batterySet(double level) {
		Platform.runLater(()->{
			batteryLabel.setText(level+"%");
		});
	}
	
	public void locationSet(double lat, double lng) {
		Platform.runLater(()->{
			locationLatLabel.setText("Lat:	" + lat);
			locationLngLabel.setText("Lng:	" + lng);
		});
	}
	
///////////////////////////// 우측 //////////////////////////////////////
	public void initRightPane() {
		rightBtnEvent();
	}
	
	public void rightBtnEvent() {
		rightStatusLabel.setOnMouseClicked((event) -> {handleStatusBtn(event);});
		rightCameraLabel.setOnMouseClicked((event) -> {handleCameraBtn(event);});
		rightDeleteBtn.setOnAction((event) -> {handleDeleteBtn(event);});
	}
	
	public void handleStatusBtn (MouseEvent event) {
		rightStatusPane.setVisible(true);
		rightCameraPane.setVisible(false);
		rightStatusLabel.setStyle("-fx-background-color: white; -fx-text-fill: black");
		rightCameraLabel.setStyle("-fx-background-color: black; -fx-text-fill: white");
	}
	
	public void handleCameraBtn (MouseEvent event) {
		rightStatusPane.setVisible(false);
		rightCameraPane.setVisible(true);
		rightStatusLabel.setStyle("-fx-background-color: black; -fx-text-fill: white");
		rightCameraLabel.setStyle("-fx-background-color: white; -fx-text-fill: black");
	}
	
	public void handleDeleteBtn(ActionEvent event) {
		statusList.removeAll(statusList);
		statusListView.setItems(FXCollections.observableArrayList(statusList));
	}
	
///////////////////////////// 메세지 //////////////////////////////////////
	public void statusMessage(String message) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				inTime   = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
				if(!Network.getUav().connected && !statusList.contains("   UAV Disconnected.")){
					leftPaneController.instance.setStatusLabels("UAV Disconnected.");
					statusList.add("   UAV Disconnected.");
					statusListView.setItems(FXCollections.observableArrayList(statusList));
				} else if(Network.getUav().connected){
					if(message.equals("UAV Armed.")) {
						if(!messageTemp.equals(message)) {
							leftPaneController.instance.setStatusLabels(message);
							statusList.add("   " + inTime + "			" + message);
							statusListView.setItems(FXCollections.observableArrayList(statusList));
							messageTemp = message;
						}	
					} else if(message.equals("UAV Disarmed.")) {
						if(!messageTemp.equals(message)) {
							if(list.size() > 0 && list.get(list.size()-1).equals("UAV Disarmed.")) {}
							else {
								leftPaneController.instance.setStatusLabels(message);
								statusList.add("   " + inTime + "			" + message);
								statusListView.setItems(FXCollections.observableArrayList(statusList));
								messageTemp = message;
							}
						}
					} else {
						leftPaneController.instance.setStatusLabels(message);
						statusList.add("   " + inTime + "			" + message);
						statusListView.setItems(FXCollections.observableArrayList(statusList));
					}
				} 
			}
		});
	}
}