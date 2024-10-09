package com.carefoot.puckpicks.gui;

import java.awt.Taskbar;
import java.awt.Taskbar.Feature;
import java.awt.Toolkit;

import com.carefoot.puckpicks.gui.scenes.LoadingScene;
import com.carefoot.puckpicks.gui.scenes.PPScene;
import com.carefoot.puckpicks.main.PuckPicks;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PPApplication {
	
	private static final String APP_ICON_FILE = "icon.png"; 		// image file for application icon
	
	private final Stage stage;
	private final LoadingScene loading;
	private final StateManager stateManager;
	
	public PPApplication(Stage stage) {
		this.stage = stage;
		loading = new LoadingScene();
		stateManager = new StateManager();

		/* Constructing loading scene and enabling until first scene is set */
		loading.build();
		loading();

		/* Configuring application window */
		configureOSWindow();
	}
	
	/**
	 * Configures properties of the window such as icon, title, etc.
	 * Properties are OS-dependent
	 */
	private void configureOSWindow() {		
		/*
		 * Update properties that are not OS-dependent first
		 */
		setTitle("PuckPicks");
		setIcon(new Image(PuckPicks.getImageResource(APP_ICON_FILE)));
		
		if (Taskbar.isTaskbarSupported()) {// configure macOS and Linux taskbar (if applicable)
			Taskbar bar = Taskbar.getTaskbar();
			if (bar.isSupported(Feature.ICON_IMAGE))
				bar.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("icon.png")));
		}
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
	 * Sets the icon of the application window
	 * @param image Image object
	 */
	public void setIcon(Image image) {
		stage.getIcons().add(image);
	}

	/**
	 * Sets a new scene in the application.
	 * Loads the scene asynchronously if it hasn't been built yet.
	 * If the scene is built, immediately updates the application.
	 * @param scene PPScene object (does not have to be initialized)
	 */
	public void setScene(PPScene scene) {
		/* If the scene is considered navigable, log it for access with navigation back arrow */
		if (scene.isNavigable())
			stateManager.logState(scene);

		/* Build and update the scene */
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
	 * Sends the application back to the previously navigated scene.
	 * Removes this scene from the scene stack
	 */
	public void goBack() {
		PPScene previous = stateManager.getAndEjectPrevious();
		if (previous != null) 	// do nothing if no valid previous scene
			setScene(previous);
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
