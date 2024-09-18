package com.carefoot.puckpicks.main;

import com.carefoot.puckpicks.data.DataManager;
import com.carefoot.puckpicks.data.SkaterRequest;
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
		
		debug();
	}
	
	// Main method
	public static void main(String[] args) {
		launch(args);
	}
	
	private void debug() {		
		DataManager dm = new DataManager();
		System.out.println(dm.submitRequest(new SkaterRequest("assists", 50)));
	}

}
