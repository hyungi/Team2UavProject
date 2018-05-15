package team2gcs.login;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Login extends Application{
	public static Login instance;
	public Stage primaryStage;
	public Scene scene;
	public String theme;

	@Override
	public void start(Stage primaryStage) throws Exception {
		instance = this;
		this.primaryStage = primaryStage;
		
		Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
		scene = new Scene(root);
		
		primaryStage.setTitle("UAV Ground Control Station(Team2)");
		primaryStage.setScene(scene);		
		primaryStage.setMaximized(true);
		primaryStage.show();
		
	}
	
	@Override
	public void stop() {
		System.exit(0);
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
