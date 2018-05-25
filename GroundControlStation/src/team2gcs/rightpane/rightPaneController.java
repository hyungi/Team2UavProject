package team2gcs.rightpane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.util.Callback;

public class rightPaneController implements Initializable{
	public static rightPaneController instance;
	@FXML private BorderPane rightStatusPane;
	@FXML private BorderPane rightCameraPane;
	@FXML private Button rightStatusBtn;
	@FXML private Button rightCameraBtn;
	@FXML private ListView<String> rightStatusListView;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnEvent();
		ObservableList<String> list = FXCollections.observableArrayList();
//		for(int i=0; i<7; i++) 
		list.add("abcdefghijklmnopqrstuvwxyz");
//		addRightStatus("ADD");
//		rightStatusListView.setCellFactory(callback);
		rightStatusListView.setItems(list);
		System.out.println("asdasdsad");
	}
	
	public void btnEvent() {
		rightStatusBtn.setOnAction((event) -> {handleStatusBtn(event);});
		rightCameraBtn.setOnAction((event) -> {handleCameraBtn(event);});
	}
	
	public void handleStatusBtn (ActionEvent event) {
		rightStatusPane.setVisible(true);
		rightCameraPane.setVisible(false);
	}
	
	public void handleCameraBtn (ActionEvent event) {
		rightStatusPane.setVisible(false);
		rightCameraPane.setVisible(true);
	}
	
	public void addRightStatus(String message) {
//		list.add(message);
		rightStatusListView.setCellFactory(callback);
//		rightStatusListView.setItems(list);
	}
	
	private Callback<ListView<String>, ListCell<String>> callback = new Callback<ListView<String>, ListCell<String>>() {
		//ListView가 셀을 요청했을 때 실행하는 call 메소드
		@Override
		public ListCell<String> call(ListView<String> param) {
			//새로운 ListCell 생성
			ListCell<String> listCell = new ListCell<String>() {
				protected void updateItem(String item, boolean empty) {		//
					
					//이벤트 처리 및 선택 색상 적용을 위해 부모의 메소드 호출
					super.updateItem(item, empty);
					
					//버퍼링된 셀일 경우 데이터 세팅이 필요없음. empty가 true면 buffer 된걸 보여줌. 재사용x.
					if(empty == true) return;	
					//새로운 데이터로 세팅
					try {
						//안드로이드와 다른 점은 재사용 여부에 대한 조건이 없음.
						//1) item.fxml을 갖고 UI 생성.
						StackPane rightStackPane= (StackPane) FXMLLoader.load(getClass().getResource("item.fxml"));	//load메소드는 fxml의 최상위 컨테이너 타입으로 리턴.
							
						Label rightStatusLabel = (Label) rightStackPane.lookup("#rightStatusLabel");
						Button statusCloseBtn = (Button) rightStackPane.lookup("#statusCloseBtn");
//						rightStatusLabel.setText("!@#!@#!");
						//UI 전달
						setGraphic(rightStackPane);
					} catch(Exception e) {
						e.printStackTrace();
					}
				}
			};
			return listCell;
		}
	};
}
