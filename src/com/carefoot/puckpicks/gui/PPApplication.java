package com.carefoot.puckpicks.gui;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PPApplication {
	
	private final Stage stage;
	private final LoadingScene loading;
	
	public PPApplication(Stage stage) {
		this.stage = stage;
		this.loading = new LoadingScene();
		loading.build();
		setTitle("PuckPicks");
	}
	
	/**
	 * Opens the application window
	 */
	public void open() {
		stage.show();
	}
	
	/**
	 * Closes the application window
	 */
	public void close() {
		stage.close();
	}
	
	/**
	 * Enables the loading screen until scene is updated
	 */
	public void loading() {
		setScene(loading);
	}

	/**
	 * Sets the title of the application window
	 * @param title New window title
	 */
	public void setTitle(String title) {
		stage.setTitle(title);
	}

	/**
	 * Sets a new scene in the application.
	 * Loads the scene asynchronously if it hasn't been built yet.
	 * If the scene is built, immediately updates the application.
	 * @param scene PPScene object (does not have to be initialized)
	 */
	public void setScene(PPScene scene) {
		if (!scene.initialized()) {
			loading();
			new Thread(() -> {
				scene.build();
				Platform.runLater(() -> stage.setScene(scene.scene()));
			}).start();
		} else {
			stage.setScene(scene.scene());
		}
	}
	
	/**
	 * Get the current window title
	 * @return Window title 
	 */
	public String getTitle() {
		return stage.getTitle();
	}
	
	/**
	 * Gets instance of current JavaFX scene
	 * @return Scene object
	 */
	public Scene getScene() {
		return stage.getScene();
	}

}
