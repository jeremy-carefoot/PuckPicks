package com.carefoot.puckpicks.main;

import com.carefoot.puckpicks.gui.LoadingScene;
import com.carefoot.puckpicks.gui.PPApplication;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppLauncher extends Application {

	// JavaFX start method
	public void start(Stage stage) throws Exception {
		PPApplication app = new PPApplication(stage);
		LoadingScene loading = new LoadingScene();
		app.setScene(loading);
		app.open();
	}
	
	// Main method
	public static void main(String[] args) {
		launch(args);
	}

}
