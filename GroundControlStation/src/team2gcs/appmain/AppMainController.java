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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
import netscape.javascript.JSObject;
import team2gcs.leftpane.leftPaneController;

public class AppMainController implements Initializable{
	public static AppMainController instance2;
	// child들의 높이 조정을 위해
	public static double heightSize;
	//공용
	@FXML private AnchorPane bottomPane;
	@FXML private BorderPane mainBorderPane;
	@FXML private BorderPane loginBorderPane;
	
	// 좌측
	@FXML private VBox leftPane;
	// 우측
	@FXML private VBox rightPane;	
	// 아래 버튼 & Pane & 둘을 가지고있는 VBox & control 값
	@FXML private AnchorPane openBottom;
	@FXML private BorderPane missionPane;
	@FXML private VBox bottomMovePane;
	@FXML private Label bottomOpenLabel;
	private boolean bottomControl = true;	
	// 우측 버튼 & Pane & 둘을 가지고있는 HBox & control 값
	@FXML private AnchorPane openRight;
	@FXML private AnchorPane viewPane;
	@FXML private HBox rightMovePane;
	@FXML private Label rightOpenLabel;
	@FXML private VBox cameraVbox;
	@FXML private VBox statusVbox;
	@FXML private Label labelConnect;
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
	@FXML private Button armBtn;
	@FXML private Button takeoffBtn;
	@FXML private Button landBtn;
	@FXML private Button rtlBtn;
	@FXML private Button loiterBtn;
	@FXML private Button btnMissionStart;
	@FXML private Button btnMissionStop;
	
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
	//화물
	@FXML private Button btnCargoWP;
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
		
	//Test
	@FXML private Label alt;
	
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
		heightSize = webView.getHeight();
		try {
			Parent leftRoot = FXMLLoader.load(getClass().getResource("../leftpane/left.fxml"));
			Parent rightRoot = FXMLLoader.load(getClass().getResource("../rightpane/right.fxml"));
			leftPane.getChildren().add(leftRoot);
			rightPane.getChildren().add(rightRoot);
		}catch (Exception e) {}
	}
	
	public void loginKeyAction() {
		
	}

//////////////////////////////////Top Menu 관련 ////////////////////////////////
	public void initTop() {
	//	currTime();
		homeLatLabel.setText("init");
		homeLngLabel.setText("init");
		locationLngLabel.setText("init");
		locationLatLabel.setText("init");
		batteryLabel.setText("init");
		signalLabel.setText("init");	
		// 연결 이벤트 클릭 관리
		connButton.setOnMouseClicked((event)->{
			if(connectState) {
				mainBorderPane.setVisible(false);
				loginBorderPane.setVisible(true);
				Network.getUav().disconnect();
				connectState = false;
			}
		});
	}
	
	public void currTime() {
		String inTime   = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
		currtimeLabel.setText(inTime);
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
	}
	// 로그인화면 취소 버튼 이벤트처리
	public void handleCancle(ActionEvent event) {
		System.exit(0);
	}
	
	//미션////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	//미션부분 버튼
	public void initMissionButton() {
		btnMissionSet.setOnAction((event)->{handleMissionSet(event);});
		btnMissionRead.setOnAction((event)->{handleMissionRead(event);});
		btnMissionUpload.setOnAction((event)->{handleMissionUpload(event);});
		btnMissionDownload.setOnAction((event)->{handleMissionDownload(event);});
		btnMissionGoto.setOnAction((event)->{handleMissionGoto(event);});
		btnMissionJump.setOnAction((event)->{handleMissionJump(event);});
		btnMissionLoi.setOnAction((event)->{handleMissionLoi(event);});
		armBtn.setOnAction((event)->{handleArm(event);});
		armBtn.setGraphic(new Circle(5, Color.rgb(0x35, 0x35, 0x35)));
		takeoffBtn.setOnAction((event)->{handleTakeoff(event);});
		landBtn.setOnAction((event)->{handleLand(event);});
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
		btnCargoWP.setOnAction((event)->{handleCargoWP(event);});
		btnCargoStart.setOnAction((event)->{handleCargoStart(event);});
		btnCargoStop.setOnAction((event)->{handleCargoStop(event);});
		
	}
	//화물 부착 시작,끝
	public void handleCargoStart(ActionEvent event) {
		Platform.runLater(() -> {
			jsproxy.call("missionStart");
		});
	}
	public void handleCargoStop(ActionEvent event) {
		Platform.runLater(() -> {
			jsproxy.call("missionStart");
		});
	}
	//미션 시작 정지
	public void handleMissionStart(ActionEvent event) {
		Network.getUav().missionStart();
		Platform.runLater(() -> {
			jsproxy.call("missionStart");
		});
	}
	public void handleMissionStop(ActionEvent event) {
		Network.getUav().missionStop();
		Platform.runLater(() -> {
			jsproxy.call("missionStop");
		});
	}
	//펜스 이벤트 처리
	public void handleFenceSet(ActionEvent event) {
		Platform.runLater(() -> {
			jsproxy.call("fenceMake");
		});
	}
	public void handleFenceUpload(ActionEvent event) {
		jsproxy.call("fenceUpload");
	}
	public void handleFenceDownload(ActionEvent event) {
		Network.getUav().fenceDownload();
	}
	public void handleFenceActivate(ActionEvent event) {
		Network.getUav().fenceEnable();
	}
	public void handleFenceDeactivate(ActionEvent event) {
		Network.getUav().fenceDisable();
	}
	public void handleFenceDelete(ActionEvent event) {
		Network.getUav().fenceClear();
		jsproxy.call("fenceClear");
	}
	//비햄금지구역 이벤트 처리
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
		System.out.println("비행금지구역삭제");

	}
	//화물운송 WP 이벤트 처
	public void handleCargoWP(ActionEvent event) {
		System.out.println("화물운송WP");
	}
	
	//Arm, Takeoff, Land, Roiter, Rtl
	public void handleArm(ActionEvent event) {
		Network.getUav().arm();
	}
	public void handleTakeoff(ActionEvent event) {
		Network.getUav().takeoff(10);//나중에 숫자입력으로 바꾸
	}
	public void handleLand(ActionEvent event) {
		Network.getUav().land();	
	}
	public void handleLoiter(ActionEvent event) {
		System.out.println("로이터 모드 실행 그러나 코딩 안함");
	}
	public void handleRtl(ActionEvent event) {
		Network.getUav().rtl();
		Platform.runLater(() -> {
			jsproxy.call("rtlStart");
		});
	}
	
	//미션생성 이벤트 처리
	public void handleMissionSet(ActionEvent event) {
		Platform.runLater(() -> {
			jsproxy.call("missionMake");
		});
	}

	//미션읽기
	public void handleMissionRead(ActionEvent event) {
		Platform.runLater(() -> {
			jsproxy.call("getMission");
		});
	}
	public void getMissionResponse(String data) {
		Platform.runLater(() -> {
			List<WayPoint> list = new ArrayList<WayPoint>();			
			JSONArray jsonArray = new JSONArray(data);
			for(int i=0; i<jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				WayPoint wayPoint = new WayPoint();
	 			wayPoint.no = jsonObject.getInt("no");
				wayPoint.kind = jsonObject.getString("kind"); //all is "waypoint";
				wayPoint.latitude = jsonObject.getDouble("lat");
				wayPoint.longitude = jsonObject.getDouble("lng");
				wayPoint.altitude = 10;
				list.add(wayPoint);
			}
			setTableViewItems(list);
		});
	}
	public void setTableViewItems(List<WayPoint> list) {
 		tableView.getItems().clear();
		tableView.setItems(FXCollections.observableArrayList(list));
	}
	
	//미션 업로드
	public void handleMissionUpload(ActionEvent event) {
		List<WayPoint> list = tableView.getItems();
		Network.getUav().missionUpload(list);
	}
	
	//미션 다운로드
	public void handleMissionDownload(ActionEvent event) {
		Network.getUav().missionDownload();
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
	}
	private void addJump() {
		WayPoint waypoint = new WayPoint();
		waypoint.kind = "jump";
		
		int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
		WayPoint wp = tableView.getItems().get(selectedIndex);
		
		if(selectedIndex < tableView.getItems().size()-1) {
			tableView.getItems().add(selectedIndex+1, waypoint);
		} else {
			tableView.getItems().add(waypoint);
		}
		for(int i=0; i<tableView.getItems().size(); i++) {
			wp = tableView.getItems().get(i);
			wp.no = i+1;
		}
	}
	
	//미션 Lio
	public void handleMissionLoi(ActionEvent event) {
		roiMake();
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
			waypoint.latitude = jsonObject.getDouble("lat");
			waypoint.longitude = jsonObject.getDouble("lng");
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
	}		
	
	//테이블뷰 설정////////////////////////////////////////////////////////////////////////////////////////////////////////
	private void initTableView() {
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
		
		TableColumn<WayPoint, Double> column3 = new TableColumn<WayPoint, Double>("Latitude");
		column3.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("latitude"));
		column3.setPrefWidth(200);
		column3.setSortable(false);
		column3.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column3);
		
		TableColumn<WayPoint, Double> column4 = new TableColumn<WayPoint, Double>("Longitude");
		column4.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("longitude"));
		column4.setPrefWidth(200);
		column4.setSortable(false);
		column4.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column4);
		
		TableColumn<WayPoint, Double> column5 = new TableColumn<WayPoint, Double>("Altitude");
		column5.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("altitude"));
		column5.setPrefWidth(200);
		column5.setSortable(false);
		column5.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column5);
		
		TableColumn<WayPoint, Double> column6 = new TableColumn<WayPoint, Double>("Jump");
		column6.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("jump"));
		column6.setPrefWidth(80);
		column6.setSortable(false);
		column6.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column6);
		
		TableColumn<WayPoint, Double> column7 = new TableColumn<WayPoint, Double>("num");
		column7.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("jumpnum"));
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
		
		TableColumn<WayPoint, Button> column9 = new TableColumn<WayPoint, Button>("Delect");
		column9.setCellValueFactory(new PropertyValueFactory<>("button"));
		column9.setPrefWidth(80);
		column9.setSortable(false);
		column9.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column9);
	}
	
	public void viewStatus(UAV uav) {
		try {
			setStatus(uav);
			leftPaneController.instance.getStatus(uav);
			leftPaneController.instance.getRollStatus(uav);
			setMissionStatus(uav);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	public void setMissionStatus(UAV uav) {
		Platform.runLater(() -> {
			
			if(uav.homeLat != 0.0) {
				jsproxy.call("setHomeLocation", uav.homeLat, uav.homeLng);
				homeLatLabel.setText(String.format("Lat:	%.6f", uav.homeLat));
				homeLngLabel.setText(String.format("Lng:	%.6f", uav.homeLng));	
			}
			jsproxy.call("setUavLocation", uav.latitude, uav.longitude, uav.heading);
			
			if(uav.wayPoints.size() != 0) {
				setMission(uav.wayPoints);
			} 
			
			jsproxy.call("setNextWaypointNo", uav.nextWaypointNo);			
			
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
				alt.setText(String.valueOf(uav.altitude));
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
				jsonObject.put("lat", wayPoint.latitude);
				jsonObject.put("lng", wayPoint.longitude);
			} else if(wayPoint.kind.equals("jump")) {
				jsonObject.put("kind",  wayPoint.kind);
				jsonObject.put("lat", wayPoints.get((int)wayPoint.latitude-1).latitude);
				jsonObject.put("lng", wayPoints.get((int)wayPoint.latitude-1).longitude+0.00005);
			} else if(wayPoint.kind.equals("rtl")) {
				jsonObject.put("kind",  wayPoint.kind);
				jsonObject.put("lat", Network.getUav().homeLat);
				jsonObject.put("lng", Network.getUav().homeLng+0.00005);
			} else if(wayPoint.kind.equals("roi")) {
				jsonObject.put("kind",  wayPoint.kind);
				jsonObject.put("lat", wayPoint.latitude);
				jsonObject.put("lng", wayPoint.longitude);
			}
			jsonArray.put(jsonObject);
		}
		String strMissionArr = jsonArray.toString();
		Platform.runLater(() -> {
			jsproxy.call("setMission", strMissionArr);
		});
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
		String strFenceArr = jsonArray.toString();
		Platform.runLater(() -> {
			jsproxy.call("setFence", strFenceArr);
		});	
	}
	
	public void log(String message) {
		System.out.println(message);
	}
	
	///////////////////////////// 미션 관련 //////////////////////////////////////
	public void gotoStart(String data) {
		Platform.runLater(() -> {
			JSONObject jsonObject = new JSONObject(data);
			double latitude = jsonObject.getDouble("lat");
			double longitude = jsonObject.getDouble("lng");
			double altitude = 10;
			Network.getUav().gotoStart(latitude, longitude, altitude);
		});
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
}
