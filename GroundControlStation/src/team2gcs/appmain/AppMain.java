package team2gcs.appmain;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppMain extends Application{
	public static AppMain instance;
	public Stage primaryStage;
	public Scene scene;
	public String theme;

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		this.primaryStage = primaryStage;
		
		Parent root = FXMLLoader.load(getClass().getResource("appmain.fxml"));
		scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("../images/app.css").toExternalForm());
		primaryStage.setTitle("UAV Ground Control Station(Team2)");
		primaryStage.setScene(scene);		
		primaryStage.setMaximized(true);
		primaryStage.show();
		
		Thread thread = new Thread(){
            @Override
            public void run() {
            	while(true) {
                try{
            		Thread.sleep(1000);
            		System.out.println("thread");
            		 Platform.runLater(()->{
            			 AppMainController.instance.currTime();
            			 System.out.println("thread2");
                 	 });	
                }
                catch(Exception e){}
                }
            }
        };
		
		thread.start();
	}
	
	@Override
	public void stop() {
		System.exit(0);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
