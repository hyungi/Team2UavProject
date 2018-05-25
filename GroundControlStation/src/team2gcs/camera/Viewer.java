/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package team2gcs.camera;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Administrator
 */
public class Viewer extends Application{
    @Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("camera.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		// 창 끄면 종
		primaryStage.setOnCloseRequest((event)->{
			System.exit(0);
		});
        }
        
        public static void main(String[] args) {
            launch(args);
    }
}
