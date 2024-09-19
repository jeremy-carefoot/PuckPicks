package com.carefoot.puckpicks.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class PPApplication {
	
	private final Stage stage;
	private final LoadingScene loading;
	
	public PPApplication(Stage stage) {
		this.stage = stage;
		this.loading = new LoadingScene();
		this.stage.setScene(loading.scene());
		setTitle("PuckPicks");
	}
	
	// Shows window
	public void open() {
		stage.show();
	}
	
	// Closes window
	public void close() {
		stage.close();
	}
	
	// Enables loading scene until scene is updated
	public void loading() {
		stage.setScene(loading.scene());
	}

	// Set the window title
	public void setTitle(String title) {
		stage.setTitle(title);
	}

	// Sets a new scene
	public void setScene(PPScene scene) {
		stage.setScene(scene.scene());
	}
	
	public String getTitle() {
		return stage.getTitle();
	}
	
	public Scene getScene() {
		return stage.getScene();
	}

}
