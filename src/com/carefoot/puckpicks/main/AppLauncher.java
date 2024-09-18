package com.carefoot.main;

import com.carefoot.gui.LoadingScene;
import com.carefoot.gui.PPApplication;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppLauncher extends Application {

	// JavaFX start method
	public void start(Stage stage) throws Exception {
		PPApplication app = new PPApplication(stage);
		LoadingScene ls = new LoadingScene();
		app.setScene(ls.scene());
		app.open();
	}
	
	// Main method
	public static void main(String[] args) {
		launch(args);
	}

}
