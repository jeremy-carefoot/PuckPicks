package com.carefoot.puckpicks.gui;

import com.carefoot.puckpicks.gui.scenes.LoadingScene;
import com.carefoot.puckpicks.gui.scenes.PPScene;

import java.awt.Taskbar;
import java.awt.Toolkit;
import java.awt.Taskbar.Feature;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class PPApplication {
	
	private final Stage stage;
	private final LoadingScene loading;
	
	public PPApplication(Stage stage) {
		this.stage = stage;
		this.loading = new LoadingScene();
		loading.build();
		loading();
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
		setIcon(new Image(getClass().getClassLoader().getResourceAsStream("icon.png")));
		
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
