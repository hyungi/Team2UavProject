package team2gcs.appmain;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
//37.5480231 127.1202967
public class AppMain extends Application{
	//106.253.56.122
	//37.547568
	//127.119557
	
	public static AppMain instance;
	public static Stage primaryStage;
	public static Stage altStage;
	public Scene scene;

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		Font.loadFont(getClass().getResourceAsStream("../images/Bombardier.ttf"),70);
 		Parent root = FXMLLoader.load(getClass().getResource("appmain.fxml"));
 		scene = new Scene(root);
 		scene.getStylesheets().add(getClass().getResource("../images/app.css").toExternalForm());
 		scene.getStylesheets().add(getClass().getResource("../images/tabpane.css").toExternalForm());
		primaryStage.setTitle("UAV Ground Control Station(Team2)");
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
        primaryStage.setFullScreen(true);
		primaryStage.show();

		this.primaryStage = primaryStage;
		
		Thread thread = new Thread(){
            @Override
            public void run() {
            	while(true) {
	                try{
 	            		Platform.runLater(()->{
	             			 AppMainController.instance2.currTime();
	             		});	
	            		Thread.sleep(1000);
	                }
	                catch(Exception e){}
            	}
            }
        };
		thread.start();
		
		//프로그램 종료시 gps.txt 닫기
		Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
            	try {
					AppMainController.instance2.gpsTxt.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        });
	}
	
	@Override
	public void stop() {
		System.exit(0);
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
