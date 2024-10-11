package com.carefoot.puckpicks.main;

import com.carefoot.puckpicks.gui.PPApplication;
import com.carefoot.puckpicks.gui.scenes.MainMenu;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Main JavaFX application launcher class.
 * Launches application and holds application instance.
 * 
 * @author jeremycarefoot
 */
public class AppLauncher extends Application {
	
	private static PPApplication app;

	// JavaFX start method
	public void start(Stage stage) throws Exception {
		app = new PPApplication(stage); 	// create new PuckPicks application wrapper class
		app.open();
		app.setScene(new MainMenu(), false); 	// present user with main menu on application start
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	/**
	 * Get the application instance
	 * @return PPApplication instance
	 */
	public static PPApplication getApp() {
		return app;
	}

}
