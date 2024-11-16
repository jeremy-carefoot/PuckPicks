package com.carefoot.puckpicks.main;

import java.io.IOException;
import java.util.Arrays;

import com.carefoot.puckpicks.data.FileStorage;
import com.carefoot.puckpicks.data.exceptions.NotAuthenticatedException;
import com.carefoot.puckpicks.data.exceptions.PPServerException;
import com.carefoot.puckpicks.data.requests.fantasy.League;
import com.carefoot.puckpicks.data.requests.fantasy.LeagueCollection;
import com.carefoot.puckpicks.data.requests.fantasy.PlayerCollection;
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
		debug();
	}
	
	// JavaFX stop method
	public void stop() {
		FileStorage.saveFiles(); 	// save all instance-independent data
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
	
	// TODO delete for prod
	private void debug() {
		try {
			League[] leagues = new LeagueCollection().array();
			PlayerCollection test = new PlayerCollection(leagues[0].getLeagueKey(), "status=A;sort=PTS");
			System.out.println(Arrays.toString(test.array()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotAuthenticatedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (PPServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
