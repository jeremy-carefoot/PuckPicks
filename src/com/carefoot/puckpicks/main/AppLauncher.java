package com.carefoot.puckpicks.main;

import com.carefoot.puckpicks.gui.PPApplication;
import com.carefoot.puckpicks.gui.scenes.Leaderboard;

import javafx.application.Application;
import javafx.stage.Stage;

public class AppLauncher extends Application {

	// JavaFX start method
	public void start(Stage stage) throws Exception {
		PPApplication app = new PPApplication(stage);
		app.open();

		debug(app);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private void debug(PPApplication app) {
		app.setScene(new Leaderboard());
	}

}
