package team2gcs.appmain;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import gcs.mission.WayPoint;
import gcs.network.Network;
import javafx.animation.AnimationTimer;
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
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.util.Duration;
import netscape.javascript.JSObject;

public class AppMainController implements Initializable{
	public static AppMainController instance2;
	//공용
	@FXML private AnchorPane bottomPane;
	@FXML private BorderPane mainBorderPane;
	@FXML private BorderPane loginBorderPane;

   
	//좌측 메뉴
	@FXML private VBox leftVbox;
	@FXML private Label rollLabel;
	@FXML private Label pitchLabel;
	@FXML private Label yawLabel;
	@FXML private Canvas hudCanvas;
	@FXML private Canvas hudLineCanvas;
	private GraphicsContext ctx;
	private GraphicsContext ctx2;
	private int roll = 0;
	private int pitch = 0;
	private int yaw = 0;
	
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
	private JSObject jsproxy;
	//로그인부분
	@FXML private TextField txtIP;
	@FXML private TextField txtPort;
	@FXML private Button btnConnect;
	@FXML private Button btnCancle;
	public static String ip;
	public static String port;
	public static boolean connectState=false;
	//미션부분
	@FXML private Button btnMissionSet;
	@FXML private Button btnMissionRead;
	@FXML private Button btnMissionUpload;
	@FXML private Button btnMissionDownload;
	@FXML private Button btnMissionGoto;
	@FXML private Button btnMissionJump;
	@FXML private Button btnMissionLoi;

	
	//미션 테이블 뷰
	@FXML private TableView tableView;
     
   	// Pane을 움직이기 위해 Double 속성값을 사용 -> Listener를 등록가능
   	private DoubleProperty bottomPaneLocation 
   	= new SimpleDoubleProperty(this,"bottomPaneLocation");
   	private DoubleProperty rightPaneLocation
   	= new SimpleDoubleProperty(this,"rightPaneLocation");   

   	//상단 라벨
   	@FXML private Label currtimeLabel;
	@FXML private Label homeLabel;
	@FXML private Label locationLabel;
	@FXML private Label batteryLabel;
	@FXML private Label signalLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		instance2 = this;
		mainBorderPane.setVisible(true);
		loginBorderPane.setVisible(false);
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
		
		initWebView();
		initTableView();
		initMissionButton();
		initLoginButton();

 		initCanvasLayer();
		initSlide();
		initTop();

		initLeftPane();
		handleYaw();
		ctx2.rotate(roll);
		

	}

	
//////////////////////////////////Top Menu 관련 ////////////////////////////////
	public void initTop() {
	//	currTime();
		homeLabel.setText("12m");
		locationLabel.setText("12m");
		batteryLabel.setText("12m");
		signalLabel.setText("12m");
	
	}
	
	public void currTime() {
		String inTime   = new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
		currtimeLabel.setText(inTime);
	}
	
	////////////////////////////////// 좌측 메뉴 ////////////////////////////////
	class ViewLoop extends AnimationTimer {
		@Override
		public void handle(long now) {

			ctx.translate(-50, -50);
			ctx.clearRect(0, 0, 140, 140);
			ctx.translate(50, 50);
			ctx2.translate(-50, -50);
			ctx2.clearRect(0, 0, 140, 140);
			ctx2.translate(50, 50);

			hudDraw();
			hudLine();
		}
	}

	private void hudDraw() {
		ctx.setLineWidth(5);
		// 큰원
		ctx.setFill(Color.rgb(36, 35, 35));
		ctx.fillOval(20, 25, 110, 110);
		// 위반원
		ctx.setFill(Color.rgb(12, 143, 217));
		ctx.fillArc(25, 30, 100, 100, 0, 180, ArcType.ROUND);
		// 아래반원
		ctx.setFill(Color.rgb(75, 187, 161));
		ctx.fillArc(25, 30, 100, 100, 0, -180, ArcType.ROUND);
		// ctx.rotate(180);
		// yaw원
		ctx.setFill(Color.WHITE);
		ctx.fillOval(70 + 50 * Math.cos(yaw * 0.01735 - Math.PI / 2), 75 + 50 * Math.sin(yaw * 0.01735 - Math.PI / 2),
				10, 10);
	}

	public void hudLine() {
		ctx2.setLineWidth(1);
		ctx2.setStroke(Color.WHITE);
		ctx2.strokeLine(55, 80.5, 95, 80.5);

		for (int i = 0; i < 20; i += 5) {
			if (i != 0) {
				ctx2.strokeLine(65, 80.5 - (i * 1.8), 85, 80.5 - (i * 1.8));
				ctx2.strokeLine(65, 80.5 + (i * 1.8), 85, 80.5 + (i * 1.8));
			}
		}
	}

	private void handleYaw() {
		Platform.runLater(() -> {
			AppMain.tempScene.setOnKeyPressed((event) -> {
				if (event.getCode() == KeyCode.LEFT) {
					yaw--;
					if (yaw == -1)
						yaw = 359;
					System.out.println(yaw);
				} else if (event.getCode() == KeyCode.RIGHT) {
					yaw++;
					if (yaw == 360)
						yaw = 0;
					System.out.println(yaw);
				} else if (event.getCode() == KeyCode.NUMPAD4) {
					if (roll >= -21) {
						ctx2.rotate(-1);
						roll--;
					}
				} else if (event.getCode() == KeyCode.NUMPAD6) {
					if (roll < 21) {
						ctx2.rotate(1);
						roll++;
					}
				}
			});
		});
	}

	private void initLeftPane() {
		ViewLoop viewLoop = new ViewLoop();
		viewLoop.start();
		initCanvasLayer();
	}

	private void initCanvasLayer() {
		ctx = hudCanvas.getGraphicsContext2D();
		ctx2 = hudLineCanvas.getGraphicsContext2D();
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
	private void updateVBox() {   bottomMovePane.setTranslateY(bottomPaneLocation.get());}
   
	private void updateHBox() {
		rightMovePane.setTranslateX(rightPaneLocation.get());
	}
	
	// animate 메소드는 현재 상태를 파악하여 어느위치로 이동시켜야되는지 slidePane에게 전달해준다.
	private void animateRightPane() {
		if(rightControl) {
			// bottomPane이 열려있으면 닫아줌 -> fxThread로 안돌리면 오류난다.
			if(!bottomControl) Platform.runLater(()->animateBottomPane());
			rightOpenLabel.setText("Close");
			slidePane(0,rightPaneLocation);
		}else {
			rightOpenLabel.setText("Open");
			slidePane(350,rightPaneLocation);
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
		System.out.println(ip);
		System.out.println(port);
		
		if(!ip.equals(null)&&!port.equals(null)) {
			Network.connect();
			Thread thread = new Thread(){
	            @Override
	            public void run() {
	               while(!connectState) {
	                   try{
	                	   Platform.runLater(()->{
	                           labelConnect.setText("Connect.");
	                       });   
	                       Thread.sleep(500);
	                       Platform.runLater(()->{
	                           labelConnect.setText("Connect..");
	                       });   
	                       Thread.sleep(500);
	                       Platform.runLater(()->{
	                           labelConnect.setText("Connect...");
 	                       });
	                       Thread.sleep(500);
	                   }
	                   catch(Exception e){}
	                }
	               mainBorderPane.setVisible(true);
	               loginBorderPane.setVisible(false);
	            }
	        };
	      thread.start();
		}else {
			labelConnect.setText("IP 또는 Port를 입력해주세요");
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
//		btnMissionUpload.setOnAction((event)->{handleMissionUpload(event);});
//		btnMissionDownload.setOnAction((event)->{handleMissionDownload(event);});
//		btnMissionGoto.setOnAction((event)->{handleMissionGoto(event);});
//		btnMissionJump.setOnAction((event)->{handleMissionJump(event);});
//		btnMissionLoi.setOnAction((event)->{handleMissionLoi(event);});
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
				wayPoint.lat = jsonObject.getDouble("lat");
				wayPoint.lng = jsonObject.getDouble("lng");
				wayPoint.alt = 10;
				list.add(wayPoint);
			}
			setTableViewItems(list);
		});
	}
	public void setTableViewItems(List<WayPoint> list) {
		tableView.getItems().clear();
		tableView.setItems(FXCollections.observableArrayList(list));
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
		column3.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("lat"));
		column3.setPrefWidth(200);
		column3.setSortable(false);
		column3.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column3);
		
		TableColumn<WayPoint, Double> column4 = new TableColumn<WayPoint, Double>("Longitude");
		column4.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("lng"));
		column4.setPrefWidth(200);
		column4.setSortable(false);
		column4.impl_setReorderable(false); //헤더를 클릭하면 멈춤 현상을 없애기 위해
		tableView.getColumns().add(column4);
		
		TableColumn<WayPoint, Double> column5 = new TableColumn<WayPoint, Double>("Altitude");
		column5.setCellValueFactory(new PropertyValueFactory<WayPoint, Double>("alt"));
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
}